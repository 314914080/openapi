<template>
<a-spin :spinning="confirmLoading">

   <a-tabs v-model="activeKey" @change="handleChangeTabs">
   <!--主表区域 -->
    <a-tab-pane tab="凭证导入规则设置" :key="refKeys[0]" :forceRender="true">
         <a-form-model ref="form" :model="model" :rules="validatorRules">
           <a-row>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="规则代码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="code">
                      <a-input v-model="model.code" placeholder="请输入规则代码" ></a-input>
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="规则描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
                      <a-input v-model="model.name" placeholder="请输入规则描述" ></a-input>
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="ERP账套" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erpDbType">
                      <j-dict-select-tag type="list" v-model="model.erpDbType"  dictCode="fi_dbset,dbname,id,dbtype='1'" placeholder="请选择ERP账套" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="财务账套" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fiDbType">
                      <j-dict-select-tag type="list" v-model="model.fiDbType"  dictCode="fi_dbset,dbname,id,dbtype='2'" placeholder="请选择财务账套" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="合并规则" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fiMergeType">
                      <j-dict-select-tag type="list" v-model="model.fiMergeType"  dictCode="fi_vouchermerge_type" placeholder="请选择合并规则" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="凭证字" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fiPz">
                      <a-input v-model="model.fiPz" placeholder="请输入凭证字" ></a-input>
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="凭证用户名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fiUser">
                      <a-input v-model="model.fiUser" placeholder="请输入凭证用户名" ></a-input>
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="借方类型字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jfiled">
                      <a-input v-model="model.jfiled" placeholder="请输入借方类型字段" ></a-input>
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="贷方类型字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dfiled">
                      <a-input v-model="model.dfiled" placeholder="请输入贷方类型字段" ></a-input>
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="查询类型(用于借贷方是否分开SQL查询)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sqltype">
                      <j-dict-select-tag type="list" v-model="model.sqltype"  dictCode="fi_querydata_type" placeholder="请选择查询类型(用于借贷方是否分开SQL查询)" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="查询语句1" :labelCol="labelCol2" :wrapperCol="wrapperCol2" prop="sqltext1">
                      <a-textarea v-model="model.sqltext1" rows="4" placeholder="请输入查询语句1" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :span="24">
                    <a-form-model-item label="查询语句2" :labelCol="labelCol2" :wrapperCol="wrapperCol2" prop="sqltext2">
                      <a-textarea v-model="model.sqltext2" rows="4" placeholder="请输入查询语句2" />
                    </a-form-model-item>
                  </a-col>
                  <a-col :xs="24" :sm="12">
                    <a-form-model-item label="启用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
                      <j-switch v-model="model.status"  ></j-switch>
                    </a-form-model-item>
                  </a-col>

                </a-row>
              </a-form-model>

     </a-tab-pane>
   <!--子表单区域 -->
     <a-tab-pane tab="借方科目设置" :key="refKeys[1]" :forceRender="true">
       <j-editable-table
         :ref="refKeys[1]"
         :loading="fiImReulsConfigJdetailTable.loading"
         :columns="fiImReulsConfigJdetailTable.columns"
         :dataSource="fiImReulsConfigJdetailTable.dataSource"
         :maxHeight="300"
         :rowNumber="true"
         :rowSelection="true"
         :actionButton="true">
         <template v-slot:action="props">
           <a @click="handleItems(props)">项目</a>

           <a-divider type="vertical" />
           <a @click="handleItems(props)">删除</a>
         </template>
       </j-editable-table>

       <fi-im-reuls-config-item-list ref="FiImReulsConfigItemList" ></fi-im-reuls-config-item-list>
     </a-tab-pane>

     <a-tab-pane tab="贷方科目设置" :key="refKeys[2]" :forceRender="true">
       <j-editable-table
         :ref="refKeys[2]"
         :loading="fiImReulsConfigDdetailTable.loading"
         :columns="fiImReulsConfigDdetailTable.columns"
         :dataSource="fiImReulsConfigDdetailTable.dataSource"
         :maxHeight="300"
         :rowNumber="true"
         :rowSelection="true"
         :actionButton="true">
         <template v-slot:action="props">
           <a @click="handleItems(props)">项目</a>

           <a-divider type="vertical" />
           <a @click="handleItems(props)">删除</a>
         </template>
       </j-editable-table>
     </a-tab-pane>

   </a-tabs>

 </a-spin>
</template>

<script>

import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
import { JEditableTableModelMixin } from '@/mixins/JEditableTableModelMixin'
import { validateDuplicateValue } from '@/utils/util'
import { VALIDATE_NO_PASSED, validateFormModelAndTables } from '@/utils/JEditableTableUtil'
import FiImReulsConfigItemList from '../FiImReulsConfigItemList'

export default {
 name: 'FiImReulsConfigForml',
 mixins: [JEditableTableModelMixin],
 components: {
   FiImReulsConfigItemList
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
     // 新增时子表默认添加几行空数据
     addDefaultRowNum: 1,
     model:{
     },
        validatorRules: {
           code: [
              { required: true, message: '请输入规则代码!'},
              { validator: (rule, value, callback) => validateDuplicateValue('fi_im_reuls_config', 'code', value, this.model.id, callback)},
           ],
           erpDbType: [
              { required: true, message: '请输入ERP账套!'},
           ],
           fiDbType: [
              { required: true, message: '请输入财务账套!'},
           ],
           fiMergeType: [
              { required: true, message: '请输入合并规则!'},
           ],
           fiPz: [
              { required: true, message: '请输入凭证字!'},
           ],
           jfiled: [
              { required: true, message: '请输入借方类型字段!'},
           ],
           dfiled: [
              { required: true, message: '请输入贷方类型字段!'},
           ],
           sqltype: [
              { required: true, message: '请输入查询类型(用于借贷方是否分开SQL查询)!'},
           ],
        },
     refKeys: ['fiImReulsConfig','fiImReulsConfigJdetail', 'fiImReulsConfigDdetail', ],
     tableKeys:['fiImReulsConfigJdetail', 'fiImReulsConfigDdetail', ],
     activeKey: 'fiImReulsConfig',
     // 借方科目设置
     fiImReulsConfigJdetailTable: {
       loading: false,
       dataSource: [],
       columns: [
         {
           title: '类型代码',
           key: 'code',
           type: FormTypes.input,
           width:"200px",
           placeholder: '请输入${title}',
           defaultValue:'',
         },
         {
           title: '类型名称',
           key: 'name',
           type: FormTypes.input,
           width:"200px",
           placeholder: '请输入${title}',
           defaultValue:'',
         },
         {
           title: '科目代码',
           key: 'kmcode',
           type: FormTypes.input,
           width:"200px",
           placeholder: '请输入${title}',
           defaultValue:'',
         },
         {
           title: '科目名称',
           key: 'kmname',
           type: FormTypes.input,
           width:"200px",
           placeholder: '请输入${title}',
           defaultValue:'',
         },
           {
           title: '操作',
           key: 'action',
           width: '8%',
           type: FormTypes.slot, // 定义该列为 自定义插值列
           slotName: 'action' // slot 的名称，对应 v-slot 冒号后面和等号前面的内容
         },

       ]
     },
     // 贷方科目设置
     fiImReulsConfigDdetailTable: {
       loading: false,
       dataSource: [],
       columns: [
         {
           title: '类型代码',
           key: 'code',
           type: FormTypes.input,
           width:"200px",
           placeholder: '请输入${title}',
           defaultValue:'',
         },
         {
           title: '类型名称',
           key: 'name',
           type: FormTypes.input,
           width:"200px",
           placeholder: '请输入${title}',
           defaultValue:'',
         },
         {
           title: '科目代码',
           key: 'kmcode',
           type: FormTypes.input,
           width:"200px",
           placeholder: '请输入${title}',
           defaultValue:'',
         },
         {
           title: '科目名称',
           key: 'kmname',
           type: FormTypes.input,
           width:"200px",
           placeholder: '请输入${title}',
           defaultValue:'',
         },
         {
           title: '操作',
           key: 'action',
           width: '8%',
           type: FormTypes.slot, // 定义该列为 自定义插值列
           slotName: 'action' // slot 的名称，对应 v-slot 冒号后面和等号前面的内容
         },
       ]
     },
     url: {
       add: "/setting/fiImReulsConfig/add",
       edit: "/setting/fiImReulsConfig/edit",
        fiImReulsConfig: {
         list: '/setting/fiImReulsConfig/queryById'
        },
       fiImReulsConfigJdetail: {
         list: '/setting/fiImReulsConfig/queryFiImReulsConfigJdetailByMainId'
       },
       fiImReulsConfigDdetail: {
         list: '/setting/fiImReulsConfig/queryFiImReulsConfigDdetailByMainId'
       },
     }
   }
 },
 methods: {
   getAllTable() {
     let values = this.tableKeys.map(key => getRefPromise(this, key))
     return Promise.all(values)
   },
   /** 调用完edit()方法之后会自动调用此方法 */
   editAfter() {
     this.$nextTick(() => {
     })
     // 加载子表数据
     if (this.model.id) {
       let params = { id: this.model.id }
       this.requestSubTableData(this.url.fiImReulsConfigJdetail.list, params, this.fiImReulsConfigJdetailTable)
       this.requestSubTableData(this.url.fiImReulsConfigDdetail.list, params, this.fiImReulsConfigDdetailTable)
     }
   },
   handleItems(record){
     console.log(record);
     this.$refs.FiImReulsConfigItemList.edit(record);
   },
   //校验所有一对一子表表单
   validateSubForm(allValues){
     return new Promise((resolve,reject)=>{
       Promise.all([
       ]).then(() => {
         resolve(allValues)
       }).catch(e => {
         reject(e)
       })
     })
   },
   /** 整理成formData */
   classifyIntoFormData(allValues) {
     let main = Object.assign(this.model, allValues.formValue)
     return {
       ...main, // 展开
       fiImReulsConfigJdetailList: allValues.tablesValue[0].values,
       fiImReulsConfigDdetailList: allValues.tablesValue[1].values,
     }
   },
    /** 确定按钮点击事件 */
     handleOk() {
       /** 触发表单验证 */
       this.getAllTable().then(tables => {
          return validateFormModelAndTables(this.$refs.form,this.model, tables)
       }).then(allValues => {
         /** 一次性验证一对一的所有子表 */
         return this.validateSubForm(allValues)
       }).then(allValues => {
         if (typeof this.classifyIntoFormData !== 'function') {
           throw this.throwNotFunction('classifyIntoFormData')
         }
         console.log("this.classifyIntoFormData",typeof this.classifyIntoFormData)
         let formData = this.classifyIntoFormData(allValues)

         // 发起请求
         return this.request(formData)
       }).catch(e => {
         if (e.error === VALIDATE_NO_PASSED) {
           // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
           this.activeKey = e.index == null ? this.refKeys[0] : this.refKeys[e.index+1]
         } else {
           console.error(e)
         }
       })
     },
   validateError(msg){
     this.$message.error(msg)
   },
 close() {
   this.visible = false
   this.$emit('close')
   this.$refs.form.clearValidate();
 },

 }
}
</script>

<style scoped>
</style>