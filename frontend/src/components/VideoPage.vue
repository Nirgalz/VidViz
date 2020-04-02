<template>
    <div>
        <b-container>
            <b-row class="selectActions">
                <b-col>
                    <div id="videoControls">
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
                        |
                        <b-btn @click="reduceSpeed">
                            <b-icon-dash></b-icon-dash>
                        </b-btn>
                        <b-form-input id="speed" v-model="playSpeed"></b-form-input>
                        <b-btn>
                            <b-icon-plus @click="increaseSpeed"></b-icon-plus>
                        </b-btn>
                        |
                        <!--                    <div id="videoControls"></div>-->
                        <b-btn @click="toggleAutoLoop"
                               :pressed="isAutoLoop"
                               v-b-tooltip.hover title="Loop [L]">
                            <b-icon-arrow-repeat></b-icon-arrow-repeat>
                        </b-btn>
                        <b-btn @click="toggleControls"
                               :pressed="isControlsVisible"
                               v-b-tooltip.hover title="Show controls [M]">
                            <b-icon-camera-video-fill></b-icon-camera-video-fill>
                        </b-btn>
                    </div>
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
                    <b-form-input v-model="textSearch" size="sm" placeholder="Search for file"
                                  @update="searchFile"></b-form-input>
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
                 v-bind:style="[item.selected ? {'background-color' : 'rgb(103,117,127)' } : {'background-color' : '#2d2d2d'}]"
                 @click="selectTile(index)">
                <div>

                    <video ref="videoPlayer"
                           class="videoPlayers"
                           loop
                           muted
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
        data() {
            return {
                videos: [],
                videosHQ: [],
                displayedVideos: [],
                players: [],
                videoSize: 50,
                videoWidth: 150,
                videoHeight: 150,
                selectedVideos: [],
                selectedHQVideos: [],
                isSelectedView: false,
                isHideView: false,
                play: false,
                videoCurrentTime: 0,
                videoDuration: 0,
                refreshVideoFunc: null,
                startTime: 0,
                textSearch: "",
                isAutoLoop: false,
                isControlsVisible: false,
                playSpeed: 1,
                ratio: 0
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
                console.log(e.keyCode);
                switch (e.keyCode) {
                    case 32 :
                        e.preventDefault();
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
                    case 108 :
                        this.toggleAutoLoop();
                        break;
                    case 109 :
                        this.toggleControls();
                        break;
                }
            },
            increaseSpeed() {
                this.playSpeed += 1;
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].playbackRate = this.playSpeed;
                }
            },
            reduceSpeed() {
                this.playSpeed -= 1;
                this.playSpeed < 1 ? this.playSpeed = 1 : this.playSpeed;
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].playbackRate = this.playSpeed;
                }
            },
            toggleAutoLoop() {
                this.isAutoLoop = !this.isAutoLoop;
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].loop = !this.players[i].loop;
                }
            },
            toggleControls() {
                this.isControlsVisible = !this.isControlsVisible;
                for (let i = 0; i < this.players.length; i++) {
                    this.players[i].controls = this.isControlsVisible;
                }
            },
            searchFile() {
                this.unSelect();
                for (let i = 0; i < this.videos.length; i++) {
                    if (this.videos[i].fileName.includes(this.textSearch)) {
                        this.selectTile(i);
                    }
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
//                 let nrows, ncols;
                let cell_size;
                if (cell_size1 < cell_size2) {
                    // nrows = nrows2;
                    // ncols = ncols2;
                    cell_size = cell_size2;
                } else {
                    // nrows = nrows1;
                    // ncols = ncols1;
                    cell_size = cell_size1;
                }
                // console.log(cell_size);
                // console.log(nrows);
                // console.log(ncols);
                let size = cell_size - 10;
                if (size < 150) {
                    size = 150;
                }
                this.videoWidth = size;
                this.videoHeight = size / this.ratio;
            },
            selectTile(index) {
                let tile = "tile-" + index;
                if (this.videos[index].selected) {
                    this.videos[index].selected = false;
                    this.$refs[tile][0].style.backgroundColor = '#2d2d2d';
                } else {
                    this.videos[index].selected = true;
                    this.$refs[tile][0].style.backgroundColor = 'rgb(103,117,127)';
                }

            },
            unSelect() {
                for (let i = 0; i < this.videos.length; i++) {
                    if (this.videos[i].selected === true) {
                        this.videos[i].selected = false;
                        this.$refs["tile-" + i][0].style.backgroundColor = '#2d2d2d';
                    }
                }
            },
            viewSelection(bool) {
                this.isSelectedView = bool;
                this.selectedVideos = [];
                this.selectedHQVideos = [];

                if (bool) {
                    for (let i = 0; i < this.videos.length; i++) {
                        if (this.videos[i].selected) {
                            this.selectedVideos.push(this.videos[i]);
                            this.selectedHQVideos.push(this.videosHQ[i]);
                        }
                    }
                } else this.selectedVideos = this.videos;

                if (this.selectedVideos.length > 0) {
                    if (this.selectedVideos.length <= 8){
                        this.displayedVideos = this.selectedHQVideos;
                    }
                    else {
                        this.displayedVideos = this.selectedVideos;
                    }
                    this.changeVideoSize();
                }
            },
            hideSelection(bool) {
                this.isHideView = bool;
                this.selectedVideos = [];
                this.selectedHQVideos = [];

                if (bool) {
                    for (let i = 0; i < this.videos.length; i++) {
                        if (!this.videos[i].selected) {
                            this.selectedVideos.push(this.videos[i]);
                            this.selectedHQVideos.push(this.videosHQ[i]);
                        }
                    }
                } else this.selectedVideos = this.videos;

                if (this.selectedVideos.length > 0) {
                    if (this.selectedVideos.length <= 8){
                        this.displayedVideos = this.selectedHQVideos;
                    }
                    else {
                        this.displayedVideos = this.selectedVideos;
                    }
                    this.changeVideoSize();
                }
            },
            deleteSelection() {
                this.selectedVideos = [];

                for (let i = 0; i < this.videos.length; i++) {
                    if (this.videos[i].selected) {
                        UploadService.deleteFile(this.videos[i].id);
                        let tile = "tile-" + i;
                        this.$refs[tile][0].style.display = "none";
                    }
                }
                this.loadFiles();
            },
            downloadJson() {
                this.selectedVideos = [];
                for (let i = 0; i < this.videos.length; i++) {
                    if (this.videos[i].selected) {
                        this.selectedVideos.push(this.videos[i])
                    }
                }
                for (let i = 0; i < this.selectedVideos.length; i++) {
                    let jsonUrl = this.selectedVideos[i].jsonUrl;
                    if (!jsonUrl.includes("/null")) {
                        window.open(this.selectedVideos[i].jsonUrl, "_blank");
                    }
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
                    let allVideos = response.data;

                    for (let i = 0; i < allVideos.length; i++) {
                        allVideos[i].selected = false;
                        if (allVideos[i].url.includes("/thumbnails")) {
                            this.videos.push(allVideos[i]);
                        } else {
                            this.videosHQ.push(allVideos[i]);
                        }
                    }
                    if (this.videos.length === 0) {
                        this.videos.push(this.videosHQ)
                    }

                    this.displayedVideos = this.videos;
                }).then(() => {
                    this.players = this.$refs.videoPlayer;
                    for (let i = 0; i < this.players.length; i++) {
                        this.players[i].volume = 0;
                    }
                    this.ratio = this.players[0].videoWidth / this.players[0].videoHeight;
                    this.changeVideoSize();
                });
            }
        },
        mounted() {
            this.selectedFolder = this.$route.params.folder;
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
        width: fit-content;
        height: fit-content;
        white-space: nowrap;
    }

    #speed {
        width: 50px;
    }


    #videoControls {
        display: flex;
    }
</style>