<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="type">
              <a-input v-model="model.type" placeholder="请输入类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目代码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemcode">
              <a-input v-model="model.itemcode" placeholder="请输入项目代码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itemname">
              <a-input v-model="model.itemname" placeholder="请输入项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="SQL字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fieldname">
              <a-input v-model="model.fieldname" placeholder="请输入SQL字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应规则" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contrastcode">
              <j-dict-select-tag type="list" v-model="model.contrastcode" dictCode="fi_contrast_set,name,id" placeholder="请选择对应规则" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bz">
              <a-input v-model="model.bz" placeholder="请输入备注字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主表ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bz">
              <a-input v-model="model.mainid" disabled  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'FiImReulsConfigItemForm',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        mainId: '',
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
           fieldname: [
              { required: true, message: '请输入SQL字段!'},
           ],
        },
        url: {
          add: "/setting/fiImReulsConfigItem/add",
          edit: "/setting/fiImReulsConfigItem/edit",
          queryById: "/setting/fiImReulsConfigItem/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add (pid) {
        console.log(pid);
        this.edit(this.modelDefault,pid);
      },
      edit (record,pid) {
        console.log(pid);
        this.model = Object.assign({}, record);
        this.visible = true;
        this.model.mainid=pid;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }

        })
      },
    }
  }
</script>