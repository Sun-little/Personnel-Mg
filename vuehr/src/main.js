import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//api.js请求方法工具的引用(vue的插件形式)
import {postRequest} from "./utils/api";
import {postKeyValueRequest} from "./utils/api";
import {putRequest} from "./utils/api";
import {deleteRequest} from "./utils/api";
import {getRequest} from "./utils/api";
import {initMenu} from "./utils/menus";
//图标库
import echarts from "echarts";
//导入font-awesome图标库
import 'font-awesome/css/font-awesome.min.css'

Vue.prototype.$echarts = echarts
Vue.prototype.postRequest=postRequest;
Vue.prototype.postKeyValueRequest=postKeyValueRequest;
Vue.prototype.putRequest=putRequest;
Vue.prototype.deleteRequest=deleteRequest;
Vue.prototype.getRequest=getRequest;
//size全局配置所有控件默认为mini
Vue.use(ElementUI,{size:'small'});

Vue.config.productionTip = false
//vue的全局导航守卫(作用就像Java中的过滤器)
router.beforeEach((to,from,next)=>{
  if (to.path == '/') {
    next();
  } else {
    if (window.sessionStorage.getItem("user")) {
      initMenu(router, store);
      next();
    } else {
      //当你还没登录访问接口时,处理你完成登录后直接重定向你没登录前访问的接口上
      //initMenu(router, store);
      next('/?redirect=' + to.path);
    }
  }
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
