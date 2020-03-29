<template>
    <div>
        <b-container>
            <b-row class="videoActions">
                <b-col>
                    <b-form-input @change="changeVideoSize" v-model="videoSize" type="range" min="0"
                                  max="100"></b-form-input>
                </b-col>
                <b-col>
                    <b-btn :disabled="isHideView" v-if="!isSelectedView" variant="info" @click="viewSelection(true)">View Selection <b-icon-eye-fill></b-icon-eye-fill></b-btn>
                    <b-btn v-if="isSelectedView" variant="info" @click="viewSelection(false)">Un-view Selection <b-icon-eye-slash-fill></b-icon-eye-slash-fill></b-btn>
                    |
                    <b-btn :disabled="isSelectedView" v-if="!isHideView" variant="warning" @click="hideSelection(true)" >Hide Selection <b-icon-eye-slash-fill></b-icon-eye-slash-fill></b-btn>
                    <b-btn v-if="isHideView" variant="warning" @click="hideSelection(false)" >Un-hide Selection <b-icon-eye--fill></b-icon-eye--fill></b-btn>
                    |
                    <b-btn :disabled="isSelectedView || isHideView" variant="danger">Delete Selected <b-icon-x-circle-fill></b-icon-x-circle-fill></b-btn>
                </b-col>
            </b-row>
        </b-container>
        <b-row class="videoContainer">
                <b-card v-for="(item, index) in displayedVideos" :key="index" class="videoBox" :id="'tile-'+index">
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
                    {{item.id}}
                    <b-btn variant="success" size="sm" :href="item.jsonUrl">
                        json
                        <b-icon-download></b-icon-download>
                    </b-btn>
                        <b-form-checkbox v-show="!isSelectedView && !isHideView" :id="item.name" v-model="item.selected"></b-form-checkbox>
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
                displayedVideos: [],
                players: [],
                videoSize: 50,
                selectedVideos: [],
                isSelectedView: false,
                isHideView: false
            }
        },
        methods: {
            playPauseVideos() {
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].player.play();
                }
            },
            changeVideoSize() {
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].player.width(10 * this.videoSize);
                    this.players[i].player.height(10 * this.videoSize);
                }
            },
            viewSelection(bool) {
                this.isSelectedView = bool;
                this.selectedVideos = [];
                if (bool){
                    for (let i = 0 ; i < this.files.length ; i++) {
                        if (this.files[i].selected) {
                            this.selectedVideos.push(this.files[i])
                        }
                    }
                }
                else this.selectedVideos = this.files;
                this.displayedVideos = this.selectedVideos;
            },
            hideSelection(bool) {
                this.isHideView = bool;
                this.selectedVideos = [];
                if (bool){
                    for (let i = 0 ; i < this.files.length ; i++) {
                        if (!this.files[i].selected) {
                            this.selectedVideos.push(this.files[i])
                        }
                    }
                }
                else this.selectedVideos = this.files;
                this.displayedVideos = this.selectedVideos;
            }
        },
        mounted() {
            UploadService.getFiles(this.selectedFolder).then(response => {
                this.files = response.data;
                for (let i = 0 ; i < this.files.length ; i++) {
                    this.files[i].selected = false;
                    this.files[i].id = i;
                }
                this.displayedVideos = this.files;
            }).then(() => {
                this.players = this.$refs.videoPlayer;
            });
        }
    }
</script>

<style scoped>
    .videoActions {
        margin-bottom: 10px;
    }

    .videoContainer {
        display: flex;
    }

    .videoBox {
        flex-direction: row;
    }
</style>