<template>
    <div>
        <b-row class="text-center">
            <b-col>
                <b-table  hover :items="folders" @row-clicked="(item, index, event) => loadFiles(item)"></b-table>
            </b-col>
            <b-col cols="8">
                <b-table  hover :items="files"></b-table>
                <div v-for="item in files" :key="item.name">
                    <video-player
                            ref="videoPlayer"
                            :options="{sources:[{src:item.url}]}"
                    >
                    </video-player>
                </div>
            </b-col>
        </b-row>

    </div>
</template>

<script>
import UploadService from "../services/UploadFilesService";
    export default {
        name: "Explorer",
        data() {
            return {
                folders: [],
                files: [],
                selectedFolder: ""
            }
        },
        methods: {
            selectFolder(item) {
                this.selectedFolder = item.name;
            },
            loadFiles(item) {
                UploadService.getFiles(item.name).then(response => {
                    this.files = response.data;
                });
            }
        },
        mounted() {
            UploadService.getFolders().then(response => {
                this.folders = response.data;
            });
        }
    }
</script>

<style scoped>

</style>