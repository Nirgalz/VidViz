<template>
  <div>
    <b-btn variant="success" @click="selectedAction = 'upload'">Upload</b-btn>
    <b-btn variant="info"  @click="selectedAction = 'explorer'">Explorer</b-btn>

    <upload-files v-if="selectedAction === 'upload'"></upload-files>

    <Explorer v-if="selectedAction === 'explorer'" v-on:loadFiles="loadVideoPage"></Explorer>

    <VideoPage v-if="selectedAction === 'videoPage'" :selectedFolder="selectedFolder"></VideoPage>
  </div>
</template>

<script>
import UploadFiles from "../components/UploadFiles";
import Explorer from "../components/Explorer";
import VideoPage from "../components/VideoPage";

export default {
  name: 'Files',
  data() {
    return {
      selectedAction: "",
      selectedFolder:"",
      files: []
    }
  },
  components: {
    UploadFiles,
    Explorer,
    VideoPage
  },
  methods: {
    loadVideoPage(selectedFolder) {
      this.selectedFolder = selectedFolder;
      this.selectedAction = "videoPage";
    }
  }
}
</script>
