<template>
    <div>
        <div>
            <b-dropdown id="menu" class="m-md-2" no-caret>
                <template v-slot:button-content>
                    <b-icon-list></b-icon-list>
                </template>
                <b-dropdown-item @click="selectedAction = 'explorer'" :active="selectedAction === 'explorer'">
                    Explorer
                    <b-icon-folder></b-icon-folder>
                </b-dropdown-item>
                <b-dropdown-divider></b-dropdown-divider>
                <b-dropdown-item @click="selectedAction = 'import'" :active="selectedAction === 'import'">
                    Scan folder
                    <b-icon-folder-symlink></b-icon-folder-symlink>
                </b-dropdown-item>
                <b-dropdown-item @click="selectedAction = 'upload'" :active="selectedAction === 'upload'">
                    Upload
                    <b-icon-upload></b-icon-upload>
                </b-dropdown-item>
            </b-dropdown>
        </div>

        <div>
            <transition name="component-fade" mode="in-out">
                <import v-if="selectedAction === 'import'"></import>
            </transition>
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
    </div>
</template>

<script>
    import UploadFiles from "../components/UploadFiles";
    import Explorer from "../components/Explorer";
    import VideoPage from "../components/VideoPage";
    import Import from "../components/Import";

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
            Import,
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
    .filesActions{
        margin-bottom: 10px;
    }

    #menu {
        position: fixed;
        top : 0px;
        left: 0px;
        z-index: 1000;
    }

  /*.component-fade-enter-active, .component-fade-leave-active {*/
  /*  transition: opacity .3s ease;*/
  /*}*/
  /*.component-fade-enter, .component-fade-leave-to*/
  /*  !* .component-fade-leave-active below version 2.1.8 *! {*/
  /*  opacity: 0;*/
  /*}*/
</style>
