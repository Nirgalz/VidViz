<template>
    <div>
        <b-container>
            <b-row class="selectActions">
                <b-col>
                    <!--                    <b-form-input @change="changeVideoSize" v-model="videoSize" type="range" min="0"-->
                    <!--                                  max="100"></b-form-input>-->
                    <b-btn @click="changeVideoSize">window size</b-btn>
                </b-col>
                <b-col>
                    <b-btn :disabled="isHideView" v-if="!isSelectedView" variant="info" @click="viewSelection(true)">
                        View Selection
                        <b-icon-eye-fill></b-icon-eye-fill>
                    </b-btn>
                    <b-btn v-if="isSelectedView" variant="info" @click="viewSelection(false)">Un-view Selection
                        <b-icon-eye-slash-fill></b-icon-eye-slash-fill>
                    </b-btn>
                    |
                    <b-btn :disabled="isSelectedView" v-if="!isHideView" variant="warning" @click="hideSelection(true)">
                        Hide Selection
                        <b-icon-eye-slash-fill></b-icon-eye-slash-fill>
                    </b-btn>
                    <b-btn v-if="isHideView" variant="warning" @click="hideSelection(false)">Un-hide Selection
                        <b-icon-eye-fill></b-icon-eye-fill>
                    </b-btn>
                    |
                    <b-btn :disabled="isSelectedView || isHideView" variant="danger">Delete Selected
                        <b-icon-x-circle-fill></b-icon-x-circle-fill>
                    </b-btn>
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
                        width: videoWitdh,
                        height:videoHeight
                    }"
                    >
                    </video-player>
                </div>

                <div class="videoActions">
                    id : {{item.id}} |
                    <b-btn variant="success" size="sm" :href="item.jsonUrl">
                        json
                        <b-icon-download></b-icon-download>
                    </b-btn>
                    |
                    <b-form-checkbox v-show="!isSelectedView && !isHideView" :id="item.name"
                                     v-model="item.selected"></b-form-checkbox>
                </div>

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
                videoWitdh: 150,
                videoHeight: 150,
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
                let topInterfaceSize = 180;

                let x = window.innerWidth;
                let y = window.innerHeight - topInterfaceSize;
                let n = this.displayedVideos.length;
                let ratio = x / y;
                let ncols_float = Math.sqrt(n * ratio);
                let nrows_float = n / ncols_float;

// Find best option filling the whole height
                let nrows1 = Math.ceil(nrows_float);
                let ncols1 = Math.ceil(n / nrows1);
                while (nrows1 * ratio < ncols1) {
                    nrows1++;
                    ncols1 = Math.ceil(n / nrows1);
                }
                let cell_size1 = y / nrows1;

// Find best option filling the whole width
                let ncols2 = Math.ceil(ncols_float);
                let nrows2 = Math.ceil(n / ncols2);
                while (ncols2 < nrows2 * ratio) {
                    ncols2++;
                    nrows2 = Math.ceil(n / ncols2);
                }
                let cell_size2 = x / ncols2;

// Find the best values
                let nrows, ncols, cell_size;
                if (cell_size1 < cell_size2) {
                    nrows = nrows2;
                    ncols = ncols2;
                    cell_size = cell_size2;
                } else {
                    nrows = nrows1;
                    ncols = ncols1;
                    cell_size = cell_size1;
                }
                console.log(cell_size);
                console.log(nrows);
                console.log(ncols);
                let size = cell_size - 70;
                if (size < 150) {
                    size =150;
                }

                for (let i = 0; i < this.players.length; i++) {
                    // this.players[i].player.width(size);
                    // this.players[i].player.height(size);
                    this.videoWitdh = size;
                    this.videoHeight = size;
                }
            },
            viewSelection(bool) {
                this.isSelectedView = bool;
                this.selectedVideos = [];
                if (bool) {
                    for (let i = 0; i < this.files.length; i++) {
                        if (this.files[i].selected) {
                            this.selectedVideos.push(this.files[i])
                        }
                    }
                } else this.selectedVideos = this.files;
                this.displayedVideos = this.selectedVideos;
                this.changeVideoSize();
            },
            hideSelection(bool) {
                this.isHideView = bool;
                this.selectedVideos = [];
                if (bool) {
                    for (let i = 0; i < this.files.length; i++) {
                        if (!this.files[i].selected) {
                            this.selectedVideos.push(this.files[i])
                        }
                    }
                } else this.selectedVideos = this.files;
                this.displayedVideos = this.selectedVideos;
                this.refreshPlayersList();
                this.changeVideoSize();
            },
            refreshPlayersList() {
                this.players = this.$refs.videoPlayer;
            }
        },
        mounted() {
            UploadService.getFiles(this.selectedFolder).then(response => {
                this.files = response.data;
                for (let i = 0; i < this.files.length; i++) {
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
    .selectActions {
        margin-bottom: 10px;
    }

    .videoContainer, .videoActions {
        display: flex;
    }

    .videoBox {
        flex-direction: row;
    }
</style>