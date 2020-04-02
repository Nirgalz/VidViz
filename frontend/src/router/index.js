import Vue from 'vue'
import VueRouter from 'vue-router'
import Explorer from "../components/Explorer";
import UploadFiles from "../components/UploadFiles";
import Import from "../components/Import";
import VideoPage from "../components/VideoPage";
Vue.use(VueRouter)


const routes = [
  {
    path: '/explorer',
    name: 'Explorer',
    component: Explorer
  },
  {
    path: '/upload',
    name: 'Upload',
    component: UploadFiles
  },
  {
    path: '/scan',
    name: 'Scan',
    component: Import
  },
  {
    path: '/video/:folder',
    name: 'Videos',
    component: VideoPage
  },
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
