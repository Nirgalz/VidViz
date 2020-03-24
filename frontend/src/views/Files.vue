<template>
    <div>

        <b-btn variant="success" @click="selectedAction = 'upload'">
            <b-icon-upload></b-icon-upload>
        </b-btn>
        |
        <b-btn variant="info" @click="selectedAction = 'explorer'">
            <b-icon-folder></b-icon-folder>
        </b-btn>
        <transition name="component-fade" mode="in-out">
            <upload-files v-if="selectedAction === 'upload'"></upload-files>
        </transition>
        <transition name="component-fade" mode="in-out">
            <Explorer v-if="selectedAction === 'explorer'" v-on:loadFiles="loadVideoPage"></Explorer>
        </transition>
        <transition name="component-fade" mode="in-out">
            <VideoPage v-if="selectedAction === 'videoPage'" :selectedFolder="selectedFolder"></VideoPage>
        </transition>
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
                selectedAction: String,
                selectedFolder: String,
                files: Array
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
<style scoped>
  /*.component-fade-enter-active, .component-fade-leave-active {*/
  /*  transition: opacity .3s ease;*/
  /*}*/
  /*.component-fade-enter, .component-fade-leave-to*/
  /*  !* .component-fade-leave-active below version 2.1.8 *! {*/
  /*  opacity: 0;*/
  /*}*/
</style>
