<template>
  <j-modal
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel">
     <fi-im-reuls-config-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"/>
 </j-modal>
</template>

<script>

  import FiImReulsConfigForm from './FiImReulsConfigForm'

  export default {
    name: 'FiImReulsConfigModal',
    components: {
      FiImReulsConfigForm
    },
    data() {
      return {
        title:'',
        visible: false,
        disableSubmit: false
      }
    },
    methods:{
      add () {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add();
        })
      },
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.handleOk();
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

<style scoped>
</style>