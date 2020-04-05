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
    component: Explorer,
    meta : {
      title: "Explorer"
    }
  },
  {
    path: '/upload',
    name: 'Upload',
    component: UploadFiles,
    meta : {
      title: "Upload"
    }
  },
  {
    path: '/scan',
    name: 'Scan',
    component: Import,
    meta : {
      title: "Scan folder"
    }
  },
  {
    path: '/video/:folder',
    name: 'Videos',
    component: VideoPage,
    meta : {
      title: "Videos view"
    }
  },
]


const router = new VueRouter({
  mode: 'history',
  routes
})
router.afterEach((to) => {
  if (to.meta && to.meta.title) {
    document.title = to.meta.title + ' | VidViz';
  }
});

export default router
