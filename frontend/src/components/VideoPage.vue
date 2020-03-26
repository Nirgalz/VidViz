<template>
    <div>
        <b-form-input @change="changeVideoSize" v-model="videoSize" type="range" min="0" max="100"></b-form-input>
        <b-row class="videoContainer">
            <b-card v-for="(item) in files" :key="item.name" class="videoBox">
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
                <b-btn variant="success" size="sm" :href="item.jsonUrl"><b-icon-download></b-icon-download></b-btn>
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
                players: [],
                videoSize: 50
            }
        },
        methods: {
            playPauseVideos() {
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].player.play();
                }
            },
            changeVideoSize() {
                console.log(this.videoSize)
                for (let i = 0 ; i < this.players.length ; i++){
                    this.players[i].player.width(10 * this.videoSize);
                    this.players[i].player.height(10 * this.videoSize);
                }
            },
        },
        mounted() {
            UploadService.getFiles(this.selectedFolder).then(response => {
                this.files = response.data;
            }).then( () => {
                    this.players = this.$refs.videoPlayer;
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