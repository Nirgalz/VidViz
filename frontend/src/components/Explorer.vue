<template>
    <div>
        <b-row class="text-center">
            <b-col cols="2">
                <b-table  hover :items="folders" @row-clicked="(item, index, event) => loadFiles(item)"></b-table>
            </b-col>
            <b-col>
                <b-btn variant="success" @click="playPauseVideos"> > </b-btn>
                <div v-for="item in files" :key="item.name">
                    <video-player
                            ref="videoPlayer"
                            :options="{
                        sources:[{src:item.url}],
                        controls:false
                    }"
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
                selectedFolder: "",
                players: []
            }
        },
        methods: {
            playPauseVideos() {
                this.players = this.$refs.videoPlayer;
                console.log(this.players);
                for ( let i = 0 ; i < this.players.length ; i++) {
                    console.log(this.players[i].player);
                    this.players[i].player.play();
                }
            },
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