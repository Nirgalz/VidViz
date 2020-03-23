<template>
    <div>
        <b-row class="text-center">
            <b-col cols="2">
                <b-table hover :items="folders" @row-clicked="(item, index, event) => loadFiles(item)"></b-table>
            </b-col>
            <b-col>

            </b-col>
        </b-row>
        <b-row class="videoContainer">
            <b-card v-for="(item, index) in files" :key="item.name" class="videoBox">
                <div @click="playPauseVideos">
                    <video-player
                            class="video-player-box"
                            ref="videoPlayer"
                            :options="{
                        sources:[{src:item.url}],
                        controls:false,
                        width: 150,
                        height:150
                    }"
                    >
                    </video-player>
                </div>
                <b-btn variant="success" size="sm" :href="item.jsonUrl">get JSON {{index}}</b-btn>
            </b-card>
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
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].player.play();
                }
            },
            selectVideo(index) {
                console.log(index);
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
    .videoContainer {
        display: flex;
    }

    .videoBox {
        flex-direction: row;
    }
</style>