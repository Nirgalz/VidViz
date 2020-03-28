import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueVideoPlayer from 'vue-video-player'
import 'video.js/dist/video-js.css'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import VueFileAgent from 'vue-file-agent';
import 'vue-file-agent/dist/vue-file-agent.css';

Vue.config.productionTip = false;
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(VueVideoPlayer);
Vue.use(VueFileAgent);

new Vue({
  router,
  store,
  VueVideoPlayer,
  render: h => h(App)
}).$mount('#app');
