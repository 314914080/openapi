<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="规则代码">
              <a-input placeholder="请输入规则代码" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="规则描述">
              <a-input placeholder="请输入规则描述" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    
    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('凭证导入规则设置')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
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
            @click="uploadFile(text)">
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
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <fiImReulsConfig-modal ref="modalForm" @ok="modalFormOk"></fiImReulsConfig-modal>
  </a-card>
</template>

<script>

  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import FiImReulsConfigModal from './modules/FiImReulsConfigModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'

  export default {
    name: "FiImReulsConfigList",
    mixins:[JeecgListMixin],
    components: {
      FiImReulsConfigModal
    },
    data () {
      return {
        description: '凭证导入规则设置管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'规则代码',
            align:"center",
            dataIndex: 'code'
          },
          {
            title:'规则描述',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'ERP账套',
            align:"center",
            dataIndex: 'erpDbType_dictText'
          },
          {
            title:'财务账套',
            align:"center",
            dataIndex: 'fiDbType_dictText'
          },
          {
            title:'合并规则',
            align:"center",
            dataIndex: 'fiMergeType_dictText'
          },
          {
            title:'凭证字',
            align:"center",
            dataIndex: 'fiPz'
          },
          {
            title:'凭证用户名',
            align:"center",
            dataIndex: 'fiUser'
          },
          {
            title:'借方类型字段',
            align:"center",
            dataIndex: 'jfiled'
          },
          {
            title:'贷方类型字段',
            align:"center",
            dataIndex: 'dfiled'
          },
          {
            title:'启用',
            align:"center",
            dataIndex: 'status',
            customRender: (text) => (!text ? "" : (text == "Y" ? "是" : "否"))
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          list: "/setting/fiImReulsConfig/list",
          delete: "/setting/fiImReulsConfig/delete",
          deleteBatch: "/setting/fiImReulsConfig/deleteBatch",
          exportXlsUrl: "/setting/fiImReulsConfig/exportXls",
          importExcelUrl: "setting/fiImReulsConfig/importExcel",
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
      }
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'code',text:'规则代码',dictCode:''})
        fieldList.push({type:'string',value:'name',text:'规则描述',dictCode:''})
        fieldList.push({type:'string',value:'erpDbType',text:'ERP账套',dictCode:'fi_dbset,dbname,id'})
        fieldList.push({type:'string',value:'fiDbType',text:'财务账套',dictCode:'fi_dbset,dbname,id'})
        fieldList.push({type:'string',value:'fiMergeType',text:'合并规则',dictCode:'fi_vouchermerge_type'})
        fieldList.push({type:'string',value:'fiPz',text:'凭证字',dictCode:''})
        fieldList.push({type:'string',value:'fiUser',text:'凭证用户名',dictCode:''})
        fieldList.push({type:'string',value:'jfiled',text:'借方类型字段',dictCode:''})
        fieldList.push({type:'string',value:'dfiled',text:'贷方类型字段',dictCode:''})
        fieldList.push({type:'string',value:'sqltype',text:'查询类型(用于借贷方是否分开SQL查询)',dictCode:'fi_querydata_type'})
        fieldList.push({type:'string',value:'sqltext1',text:'查询语句1',dictCode:''})
        fieldList.push({type:'string',value:'sqltext2',text:'查询语句2',dictCode:''})
        fieldList.push({type:'switch',value:'status',text:'启用'})
        this.superFieldList = fieldList
      }

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>