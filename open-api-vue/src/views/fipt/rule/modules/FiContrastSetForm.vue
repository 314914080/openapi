<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="20" >
            <a-form-model-item label="规则代码"  :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
              <a-input v-model="model.code" placeholder="请输入规则代码" :disabled="!!model.id" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="20" >
            <a-form-model-item label="规则名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" placeholder="请输入规则名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="20" >
            <a-form-model-item label="账套(只能选数据库直连类型)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dbType">
              <j-dict-select-tag type="list" v-model="model.dbType" dictCode="fi_dbset,dbname,dbname,dbtype='1'" placeholder="请选择账套" />
            </a-form-model-item>
          </a-col>
          <a-col :span="20" >
            <a-form-model-item label="sql语句" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sqltext">
              <a-input v-model="model.sqltext" placeholder="请输入sql语句" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="20" >
            <a-form-model-item label="代码或者名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="searchText">
              <a-input v-model="model.searchText" placeholder="代码或者名称" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="2" >
            <a-button @click="querySqlAction" type="primary" icon="left-circle-o">查询</a-button>
          </a-col>
        </a-row>

      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="规则设置明细" :key="refKeys[0]" :forceRender="true">
        <j-editable-table
          :ref="refKeys[0]"
          :loading="fiContrastSetdetailTable.loading"
          :columns="fiContrastSetdetailTable.columns"
          :dataSource="fiContrastSetdetailTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :actionButton="true">
        </j-editable-table>
      </a-tab-pane>
    </a-tabs>

  </a-spin>

</template>

<script>

  import { getAction } from '@api/manage'
  import { FormTypes,getRefPromise,VALIDATE_NO_PASSED } from '@/utils/JEditableTableUtil'
  import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
  import { validateDuplicateValue } from '@/utils/util'
  import JEditableTable from '@/components/jeecg/JEditableTable'

  export default {
    name: 'FiContrastSetForm',
    mixins: [JEditableTableModelMixin],
    components: {
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol2: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol2: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        model:{
        },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
          code: [
            { required: true, message: '请输入规则代码!'},
            { validator: (rule, value, callback) => validateDuplicateValue('fi_contrast_set', 'code', value, this.model.id, callback)},
          ],
           name: [
              { required: true, message: '请输入规则名称!'},
           ],
           dbType: [
              { required: true, message: '请输入账套!'},
           ],
           sqltext: [
              { required: true, message: '请输入sql语句!'},
           ],
        },
        refKeys: ['fiContrastSetdetail', ],
        tableKeys:['fiContrastSetdetail', ],
        activeKey: 'fiContrastSetdetail',
        // 规则设置明细
        fiContrastSetdetailTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '主数据代码',
              key: 'code',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '主数据名称',
              key: 'name',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '外部系统编码',
              key: 'externalcode',
              type: FormTypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            }

          ]
        },
        url: {
          add: "/setting/fiContrastSet/add",
          edit: "/setting/fiContrastSet/edit",
          queryById: "/setting/fiContrastSet/queryById",
          querySqlById: "/setting/fiContrastSet/querySqlById",
          fiContrastSetdetail: {
            list: '/setting/fiContrastSet/queryFiContrastSetdetailByMainId'
          },
        }
      }
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
    },
    methods: {
      addBefore(){
        this.fiContrastSetdetailTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      querySqlAction(){
        var id=" ";
        if(this.model.id){
          id=this.model.id;
        }
        console.log("ss"+this.model.sqltext);
        // 加载子表数据
        if (this.model.dbType&&this.model.sqltext) {
          let params = {id:id,dbType:this.model.dbType,sqlText:this.model.sqltext,searchText:this.model.searchText }
          this.fiContrastSetdetailTable.loading = true
          getAction(this.url.querySqlById, params).then(res => {
            let { result } = res
            let dataSource = []
            if (result) {
              if (Array.isArray(result)) {
                dataSource = result
              } else if (Array.isArray(result.records)) {
                dataSource = result.records
              }
            }else {
              if (!res.success) {
                this.$message.warning(res.message)
              }

            }
            this.fiContrastSetdetailTable.dataSource = dataSource
            typeof success === 'function' ? success(res) : ''
          }).finally(() => {
            this.fiContrastSetdetailTable.loading = false
          })
        }
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.fiContrastSetdetail.list, params, this.fiContrastSetdetailTable)
        }
      },
      //校验所有一对一子表表单
      validateSubForm(allValues){
          return new Promise((resolve,reject)=>{
            Promise.all([
            ]).then(() => {
              resolve(allValues)
            }).catch(e => {
              if (e.error === VALIDATE_NO_PASSED) {
                // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
                this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
              } else {
                console.error(e)
              }
            })
          })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          fiContrastSetdetailList: allValues.tablesValue[0].values,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },

    }
  }
</script>

<style scoped>
</style>