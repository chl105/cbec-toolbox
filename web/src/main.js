import Vue from "vue";
import App from "./App.vue";
import router from "./router/index";
import ViewUI from "view-design";
import "view-design/dist/styles/iview.css";
import store from "./store/index";
import axios from "axios";
import VueClipboard from "vue-clipboard2";

Vue.use(ViewUI);
Vue.config.productionTip = false;
Vue.use(VueClipboard);

axios.interceptors.request.use((config) => {
  if (store.state.tokenHeader) {
    config.headers.Authorization = store.state.tokenHeader;
  }
  return config;
});
axios.defaults.timeout = 30000;

// 添加点击空白处的指令
Vue.directive('click-outside', {
  bind: function (el, binding, vnode) {
    el.clickOutsideEvent = function (event) {
      // here I check that click was outside the el and his childrens
      if (!(el == event.target || el.contains(event.target))) {
        // and if it did, call method provided in attribute value
        vnode.context[binding.expression](event)
      }
    }
    document.body.addEventListener('click', el.clickOutsideEvent)
  },
  unbind: function (el) {
    document.body.removeEventListener('click', el.clickOutsideEvent)
  }
})

new Vue({
  router: router,
  store,
  render: (h) => h(App),
}).$mount("#app");