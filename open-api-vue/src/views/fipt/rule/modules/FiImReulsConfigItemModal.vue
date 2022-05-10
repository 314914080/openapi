<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <fi-im-reuls-config-item-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></fi-im-reuls-config-item-form>
  </j-modal>
</template>

<script>

  import FiImReulsConfigItemForm from './FiImReulsConfigItemForm'
  export default {
    name: 'FiImReulsConfigItemModal',
    components: {
      FiImReulsConfigItemForm
    },
    data () {
      return {
        title:'',
        width:800,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      add (pid) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add(pid);
        })
      },
      edit (record,pid) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record,pid);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.submitForm();
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>