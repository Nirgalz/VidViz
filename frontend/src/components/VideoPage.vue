<template>
    <div>
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
        name: "VideoPage",
        props: {
            selectedFolder: String
        },
        data() {
            return {
                files: [],
                players: []
            }
        },
        methods: {
            playPauseVideos() {
                this.players = this.$refs.videoPlayer;
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].player.play();
                }
            }
            ,
            selectVideo(index) {
                console.log(index);
            }
            ,
        },
        mounted() {
            console.log(this.selectedFolder);
            UploadService.getFiles(this.selectedFolder).then(response => {
                this.files = response.data;
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