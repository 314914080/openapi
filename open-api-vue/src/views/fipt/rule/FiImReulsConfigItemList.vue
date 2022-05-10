<template>
  <a-drawer
    title="科目核算项目设置"
    :width="drawerWidth"
    @close="onClose"
    :visible="visible">
    <div
      :style="{
        padding:'10px',
        border: '1px solid #e9e9e9',
        background: '#fff',
      }">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="主表ID" >
              <a-input placeholder="请输入主表ID" v-model="queryParam.mainid" disabled ></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="科目">
              <a-input placeholder="科目" v-model="queryParam.kmdm"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    </div>
    <fi-im-reuls-config-item-modal ref="modalForm" @ok="modalFormOk"></fi-im-reuls-config-item-modal>
  </a-drawer>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import FiImReulsConfigItemModal from './modules/FiImReulsConfigItemModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { getPermissionRuleList, queryPermissionRule } from '@api/api'
  import { getAction } from '@api/manage'

  export default {
    name: 'FiImReulsConfigItemList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      FiImReulsConfigItemModal
    },
    data () {
      return {
        description: '科目核算项目管理页面',
        drawerWidth: 680,
        visible: false,
        queryParam: {},
        mainId: '',
        form: this.$form.createForm(this),
        loading: false,
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          // {
          //   title:'类型',
          //   align:"center",
          //   dataIndex: 'type'
          //
          // },
          {
            title:'项目代码',
            align:"center",
            width: 40,
            dataIndex: 'itemcode'
          },
          {
            title:'项目名称',
            align:"center",
            width: 60,
            dataIndex: 'itemname'
          },
          {
            title:'SQL字段',
            align:"center",
            width: 50,
            dataIndex: 'fieldname'
          },
          {
            title:'对应规则',
            align:"center",
            width: 80,
            dataIndex: 'contrastcode_dictText'
          },
          {
            title:'备注字段',
            align:"center",
            width: 60,
            dataIndex: 'bz'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/setting/fiImReulsConfigItem/list",
          delete: "/setting/fiImReulsConfigItem/delete",
          deleteBatch: "/setting/fiImReulsConfigItem/deleteBatch",
          exportXlsUrl: "/setting/fiImReulsConfigItem/exportXls",
          importExcelUrl: "setting/fiImReulsConfigItem/importExcel",

        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      handleAdd: function () {
        this.$refs.modalForm.add(this.queryParam.mainid);
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleEdit: function (record) {
        this.$refs.modalForm.edit(record,this.queryParam.mainid);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
      },
      onClose() {
        this.visible = false
      },
      loadData(arg) {
        console.log(this.queryParam.mainid);
        if(this.queryParam.mainid==null){
          return ;
        }
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
            this.dataSource = res.result.records||res.result;
            if(res.result.total)
            {
              this.ipagination.total = res.result.total;
            }else{
              this.ipagination.total = 0;
            }
            //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          }else{
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.loading = false
        })

      },
      edit(record) {
        if (record.id) {
          this.visible = true
          this.mainId = record.rowId
        }
        console.log(record.getValue().kmcode);
        this.queryParam = {}

        this.queryParam.mainid = record.rowId
        this.queryParam.kmdm=record.getValue().kmcode
        this.visible = true
        this.loadData(1);
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'type',text:'类型',dictCode:''})
        fieldList.push({type:'string',value:'itemcode',text:'项目代码',dictCode:''})
        fieldList.push({type:'string',value:'itemname',text:'项目名称',dictCode:''})
        fieldList.push({type:'string',value:'fieldname',text:'SQL字段',dictCode:''})
        fieldList.push({type:'string',value:'contrastcode',text:'对应规则',dictCode:'fi_contrast_set,name,code'})
        fieldList.push({type:'string',value:'bz',text:'备注字段',dictCode:''})
        fieldList.push({type:'string',value:'mainid',text:'主表ID',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>