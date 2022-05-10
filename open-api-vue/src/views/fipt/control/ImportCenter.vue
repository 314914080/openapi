<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="开始日期">
              <j-date :show-time="true" date-format="YYYY-MM-DD" placeholder="请选择业务开始日期" v-model="queryParam.startDate"></j-date>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束日期">
              <j-date :show-time="true" date-format="YYYY-MM-DD" placeholder="请选择业务结束日期" v-model="queryParam.endDate"></j-date>
            </a-form-item>
          </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="导入规则">
                <j-dict-select-tag placeholder="导入规则" v-model="queryParam.code"  dictCode="fi_im_reuls_config,name,code"/>
              </a-form-item>
            </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" :disabled="butdisabled" @click="execute" icon="search">执行</a-button>
            </span>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" :disabled="butdisabled" @click="clearlog" icon="stop">清理日志</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <a-tabs defaultActiveKey="2">
      <a-tab-pane tab="消息" key="2">
        <textarea style="width:100%;font-size: 16px;font-weight:500" :rows="20"  v-model="msg"  disabled="false">
        </textarea>
      </a-tab-pane>
    </a-tabs>

<!--    <a-tabs defaultActiveKey="1">-->
<!--      <a-tab-pane tab="response" key="1">-->
<!--        <textarea style="width:100%;font-size: 16px;font-weight:500" :rows="10" v-html="resultJson" readOnly>-->
<!--        </textarea>-->
<!--      </a-tab-pane>-->
<!--    </a-tabs>-->
  </a-card>
</template>
<script>
import { axios } from '@/utils/request'
import { ACCESS_TOKEN } from "@/store/mutation-types"
import Vue from 'vue'
import { getAction, postAction } from '@api/manage'
import { filterObj } from '@/utils/util'
import store from '@/store/'
export default {
  name: 'ImportCenter',
  data(){
    return {
      url: {
        execute: "/importCenter/VImport/execute",
      },
      queryParam: {},
      msg:"",
      paramJson:"",
      stopTimer:false,
      websock: null,
      butdisabled:false,
      lockReconnect:false,
      heartCheck:null,
      columns:[],
      resultJson:{},
      requestMethod:"POST"
    }
  },
  mounted() {
    //初始化websocket
    this.initWebSocket()
  },
  destroyed: function () { // 离开页面生命周期函数
    this.websock.close();
  },
  methods: {
    onSearch (value) {
      let that = this
      if(!value){
        that.$message.error("请填写路径")
        return false
      }
      this.resultJson = {};
      axios({
        url: value,
        method: this.requestMethod,
        data: this.paramJson
      }).then((res) => {
        console.log(res)
        this.resultJson = res
      }).catch((err) => {
        that.$message.error("请求异常："+err)
      })
    },
    initWebSocket: function () {
      // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
      var userId = store.getters.userInfo.id;
      var url = window._CONFIG['domianURL'].replace("https://","wss://").replace("http://","ws://")+"/websocketFI/"+userId;
      console.log(url);
      this.websock = new WebSocket(url);
      this.websock.onopen = this.websocketOnopen;
      this.websock.onerror = this.websocketOnerror;
      this.websock.onmessage = this.websocketOnmessage;
      this.websock.onclose = this.websocketOnclose;
    },
    websocketOnopen: function () {
      console.log("WebSocket连接成功");
      this.msg=this.msg+'ws服务连接成功'+'\n';
      //心跳检测重置
      //this.heartCheck.reset().start();
    },
    websocketOnerror: function (e) {
      console.log("WebSocketFI连接发生错误");
      this.msg=this.msg+'ws服务连接错误请重新打开'+'\n';
    },
    reconnect() {

    },
    websocketOnmessage: function (e) {
      console.log("-----接收消息-------",e.data);

      this.msg=    this.msg+e.data+'\n';
      if(e.data=="=>OK"){
        console.log(111);
        this.butdisabled=false;
      }
      //心跳检测重置
      //this.heartCheck.reset().start();
    },
    websocketOnclose: function (e) {
      this.msg=this.msg+'ws服务连接断开请重新打开页面'+'\n';
      console.log("connection closed (" + e + ")");
      if(e){
        console.log("connection closed (" + e.code + ")");
      }
      this.reconnect();
    },
    websocketSend(text) { // 数据发送
      try {
        this.websock.send(text);
      } catch (err) {
        console.log("send failed (" + err.code + ")");
      }
    },
    execute(){
      if(this.websock.readyState!=1){
        this.initWebSocket();

      }
      this.butdisabled=true;
      if(!this.url.execute){
        this.$message.error("请设置url.execute属性!")
        return
      }
      var params = this.getQueryParams();//查询条件
      this.butdisabled=true;
      postAction(this.url.execute, params).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
        }else{
          this.$message.warning(res.message)
        }
      }).finally(() => {
      })

    } ,clearlog(){

      this.msg="";
    }
    , getQueryParams(){
      //高级查询器
      let sqp = {}
      if(this.superQueryParams){
        sqp['superQueryParams']=encodeURI(this.superQueryParams)
        sqp['superQueryMatchType'] = this.superQueryMatchType
      }
      var param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
      param.userId= store.getters.userInfo.id;
      delete param.birthdayRange; //范围参数不传递后台
      return filterObj(param);
    },
    changeVal(e){
      try {
        let json = e.target.value;
        if(json.indexOf(",}")>0){
          json = json.replace(",}","}");
        }
        this.paramJson = JSON.parse(json);
      }catch (e) {
        console.log(e);
        this.$message.error("非法的JSON字符串")
      }
    },
    handleChange(value) {
      this.requestMethod = value;
    },
    created () {
      const token = Vue.ls.get(ACCESS_TOKEN);
      this.headers = {"X-Access-Token":token};

    }
  }
}
</script>