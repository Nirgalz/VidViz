<template>
    <div>
        <b-container>
            <b-row class="selectActions">
                <b-col>
                    <div id="videoControls"></div>
                    <b-btn v-if="!play"
                           @click="playPauseVideos(true)"
                           v-b-tooltip.hover title="Play [SPACEBAR]">
                        <b-icon-play-fill></b-icon-play-fill>
                    </b-btn>
                    <b-btn v-if="play"
                           @click="playPauseVideos(false)"
                           v-b-tooltip.hover title="Pause [SPACEBAR]">
                        <b-icon-pause-fill></b-icon-pause-fill>
                    </b-btn>
                    <b-form-input
                            @click="startVideoFrom()"
                            v-model="videoCurrentTime"
                            type="range"
                            step="0.0001"
                            min="0"
                            :max="videoDuration">
                    </b-form-input>
                </b-col>
                <b-col>
                    <b-btn @click="unSelect"
                           :disabled="isSelectedView || isHideView"
                           v-b-tooltip.hover title="Unselect all elements [C]">
                        <b-icon-unlock-fill></b-icon-unlock-fill>
                    </b-btn>
                    <b-btn :disabled="isHideView"
                           v-if="!isSelectedView"
                           @click="viewSelection(true)"
                           v-b-tooltip.hover title="View Selection [V]">
                        <b-icon-eye-fill></b-icon-eye-fill>
                    </b-btn>
                    <b-btn v-if="isSelectedView"
                           @click="viewSelection(false)"
                           v-b-tooltip.hover title="Un-view Selection [V]">
                        <b-icon-eye-slash-fill></b-icon-eye-slash-fill>
                    </b-btn>
                    <b-btn :disabled="isSelectedView"
                           v-if="!isHideView"
                           @click="hideSelection(true)"
                           v-b-tooltip.hover title="Hide Selection [B]">
                        <b-icon-eye-slash-fill></b-icon-eye-slash-fill>
                    </b-btn>
                    <b-btn v-if="isHideView"
                           @click="hideSelection(false)"
                           v-b-tooltip.hover title="Un-hide Selection [B]">
                        <b-icon-eye-fill></b-icon-eye-fill>
                    </b-btn>
                    <b-btn :disabled="isSelectedView || isHideView"
                           @click="downloadJson"
                           v-b-tooltip.hover title="Download json [J]">
                        <b-icon-download></b-icon-download>
                    </b-btn>
                    <b-btn :disabled="isSelectedView || isHideView"
                           @click="deleteSelection"
                           v-b-tooltip.hover title="Delete Selected [S]">
                        <b-icon-trash-fill></b-icon-trash-fill>
                    </b-btn>
                </b-col>
            </b-row>
        </b-container>
        <b-row class="videoContainer">
            <div v-for="(item, index) in displayedVideos"
                 :key="index"
                 class="videoBox"
                 :ref="'tile-'+index"
                 v-bind:style="[item.selected ? {'background-color' : '#28A745'} : {'background-color' : 'white'}]"
                 @click="selectTile(index)">
                <div>

                    <video ref="videoPlayer"
                           class="videoPlayers"
                           :src="item.url"
                           :width="videoWidth"
                           :height="videoHeight">

                    </video>
                </div>
            </div>
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
                videoWidth: 150,
                videoHeight: 150,
                selectedVideos: [],
                isSelectedView: false,
                isHideView: false,
                play: false,
                videoCurrentTime: 0,
                videoDuration: 0,
                refreshVideoFunc: null,
                startTime: 0,
            }
        },
        created() {
            window.addEventListener('keypress', this.doCommand);
        },
        destroyed() {
            window.removeEventListener('keypress', this.doCommand);
        },
        methods: {
            doCommand(e) {
                //let cmd = String.fromCharCode(e.keyCode).toLowerCase();
                e.preventDefault();
                console.log(e.keyCode);
                switch (e.keyCode) {
                    case 32 :
                        this.playPauseVideos(!this.play);
                        break;
                    case 99 :
                        if (!this.isHideView && !this.isSelectedView) {
                            this.unSelect();
                        }
                        break;
                    case 118 :
                        if (!this.isHideView && !this.isSelectedView) {
                            this.viewSelection(true);
                        } else {
                            this.viewSelection(false);
                        }
                        break;
                    case 98 :
                        if (!this.isHideView && !this.isSelectedView) {
                            this.hideSelection(true);
                        } else {
                            this.hideSelection(false);
                        }
                        break;
                    case 106 :
                        if (!this.isHideView && !this.isSelectedView) {
                            this.downloadJson();
                        }
                        break;
                    case 115 :
                        if (!this.isHideView && !this.isSelectedView) {
                            this.deleteSelection();
                        }
                        break;
                }
            },
            playPauseVideos(play) {
                this.videoDuration = this.players[0].duration;
                this.refreshVideoProgress(play);
                this.play = play;
                if (play) {
                    for (let i = 0; i < this.players.length; i++) {
                        this.players[i].play();
                    }
                } else {
                    for (let i = 0; i < this.players.length; i++) {
                        this.players[i].pause();
                    }
                }

            },
            startVideoFrom() {
                this.playPauseVideos(false);
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].currentTime = this.videoCurrentTime;
                }
                this.playPauseVideos(true);
            },
            updateVideoControls() {
                if (this.players.length > 0) {
                    this.videoCurrentTime = this.players[0].currentTime;
                    if (this.players[0].ended) {
                        this.play = false;
                    }
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
                let size = cell_size - 10;
                if (size < 150) {
                    size = 150;
                }

                for (let i = 0; i < this.players.length; i++) {
                    // this.players[i].player.width(size);
                    // this.players[i].player.height(size);
                    this.videoWidth = size;
                    this.videoHeight = size;
                }
            },
            selectTile(index) {
                let tile = "tile-" + index;
                if (this.files[index].selected) {
                    this.files[index].selected = false;
                    this.$refs[tile][0].style.backgroundColor = "white";
                } else {
                    this.files[index].selected = true;
                    this.$refs[tile][0].style.backgroundColor = "#28A745";
                }

            },
            unSelect() {
                for (let i = 0; i < this.files.length; i++) {
                    if (this.files[i].selected === true) {
                        this.files[i].selected = false;
                        this.$refs["tile-" + i][0].style.backgroundColor = "white";
                    }
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

                if (this.selectedVideos.length > 0) {
                    this.displayedVideos = this.selectedVideos;
                    this.changeVideoSize();
                }
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

                if (this.selectedVideos.length > 0) {
                    this.displayedVideos = this.selectedVideos;
                    this.changeVideoSize();
                }
            },
            deleteSelection() {
                this.selectedVideos = [];

                for (let i = 0; i < this.files.length; i++) {
                    if (this.files[i].selected) {
                        UploadService.deleteFile(this.files[i].id);
                        let tile = "tile-" + i;
                        this.$refs[tile][0].style.display = "none";
                    }
                }
                this.loadFiles();
            },
            downloadJson() {
                this.selectedVideos = [];
                for (let i = 0; i < this.files.length; i++) {
                    if (this.files[i].selected) {
                        this.selectedVideos.push(this.files[i])
                    }
                }
                for (let i = 0; i < this.selectedVideos.length; i++) {
                    window.open(this.selectedVideos[i].jsonUrl, "_blank");
                }
            },
            refreshVideoProgress(bool) {
                if (bool) {
                    this.refreshVideoFunc = window.setInterval(() => {
                        this.updateVideoControls()
                    }, 100)
                } else {
                    window.clearInterval(this.refreshVideoFunc);
                    this.refreshVideoFunc = null;
                }

            },
            loadFiles() {
                UploadService.getFiles(this.selectedFolder).then(response => {
                    this.files = response.data;
                    for (let i = 0; i < this.files.length; i++) {
                        this.files[i].selected = false;
                    }
                    this.displayedVideos = this.files;
                }).then(() => {
                    this.players = this.$refs.videoPlayer;
                    for (let i = 0; i < this.players.length; i++) {
                        this.players[i].volume = 0;
                    }
                });
            }
        },
        mounted() {
            this.loadFiles();
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
        padding: 5px 5px 0 5px;
    }
</style>