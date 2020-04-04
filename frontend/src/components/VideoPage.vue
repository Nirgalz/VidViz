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
                        <div>|</div>
                        <b-btn @click="decreaseSpeed"
                               v-b-tooltip.hover title="Decrease speed [NUMPAD -]">
                            <b-icon-dash></b-icon-dash>
                        </b-btn>
                        <b-form-input id="speed" v-model="playSpeed" @change="updateVideoSpeed"></b-form-input>
                        <b-btn  @click="increaseSpeed"
                                v-b-tooltip.hover title="Increase speed [NUMPAD +]">
                            <b-icon-plus></b-icon-plus>
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
                <b-col cols="1">
                    <H5>{{folderName}}</H5>
                    <b-btn v-if="isShowInfos" @click="showInfos()">
                        <b-icon-info></b-icon-info>
                    </b-btn>
                    <b-btn v-if="!isShowInfos" @click="showInfos()">
                        <b-icon-info-fill></b-icon-info-fill>
                    </b-btn>
                </b-col>
                <b-col>
                    <b-form-input v-model="textSearch"
                                  @focusin="isShortcutEnabled = false"
                                  @focusout="isShortcutEnabled = true"
                                  size="sm"
                                  placeholder="Search for file"
                                  @update="searchFile"></b-form-input>
                    <div id="filesControls">
                        <p>{{getSelectedVideosIds().length}} videos selected from {{videos.length}} videos </p>
                        <b-btn @click="unSelectAll"
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
                        |
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
                    </div>

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
                {{isShowInfos ? item.fileName : ""}}
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
                folderName: "",
                videos: [],
                videosHQ: [],
                displayedVideos: [],
                players: [],
                videoSize: 50,
                videoWidth: 150,
                videoHeight: 150,
                isSelectedView: false,
                isHideView: false,
                isShowInfos: false,
                play: false,
                videoCurrentTime: 0,
                videoDuration: 0,
                refreshVideoFunc: null,
                startTime: 0,
                textSearch: "",
                isShortcutEnabled : true,
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
                if (this.isShortcutEnabled)
                // console.log(e.keyCode);
                switch (e.keyCode) {
                    case 32 :
                        e.preventDefault();
                        this.playPauseVideos(!this.play);
                        break;
                    case 99 :
                        if (!this.isHideView && !this.isSelectedView) {
                            this.unSelectAll();
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
                    case 45 :
                        this.decreaseSpeed();
                        break;
                    case 43 :
                        this.increaseSpeed();
                        break;
                }
            },
            showInfos() {
                this.isShowInfos = !this.isShowInfos;
            },
            increaseSpeed() {
                this.playSpeed += 1;
                this.updateVideoSpeed();
            },
            decreaseSpeed() {
                this.playSpeed -= 1;
                this.playSpeed < 1 ? this.playSpeed = 1 : this.playSpeed;
                this.updateVideoSpeed();
            },
            updateVideoSpeed() {
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
                this.unSelectAll();
                for (let i = 0; i < this.videos.length; i++) {
                    if (this.videos[i].fileName.toLowerCase().includes(this.textSearch.toLowerCase())) {
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

                let nrows1 = Math.ceil(nrows_float);
                let ncols1 = Math.ceil(n / nrows1);
                while (nrows1 * ratio < ncols1) {
                    nrows1++;
                    ncols1 = Math.ceil(n / nrows1);
                }

                let cell_size1 = y / nrows1;
                let ncols2 = Math.ceil(ncols_float);
                let nrows2 = Math.ceil(n / ncols2);
                while (ncols2 < nrows2 * ratio) {
                    ncols2++;
                    nrows2 = Math.ceil(n / ncols2);
                }

                let cell_size2 = x / ncols2;
                let cell_size;
                if (cell_size1 < cell_size2) {
                    cell_size = cell_size2;
                } else {
                    cell_size = cell_size1;
                }

                let size = cell_size - 10;
                if (size < 150) {
                    size = 150;
                }
                this.videoWidth = size;
                this.videoHeight = size / this.ratio;
            },
            selectTile(index) {
                if (!this.isSelectedView && !this.isHideView) {
                    let tile = "tile-" + index;
                    if (this.videos[index].selected) {
                        this.videos[index].selected = false;
                        this.$refs[tile][0].style.backgroundColor = '#2d2d2d';
                    } else {
                        this.videos[index].selected = true;
                        this.$refs[tile][0].style.backgroundColor = 'rgb(103,117,127)';
                    }
                }
            },
            unSelectAll() {
                for (let i = 0; i < this.videos.length; i++) {
                    if (this.videos[i].selected === true) {
                        this.videos[i].selected = false;
                        this.$refs["tile-" + i][0].style.backgroundColor = '#2d2d2d';
                    }
                }
            },
            getVideos(isSelected, quality) {
                let videos = [];
                for (let i = 0; i < this.videos.length; i++) {
                    if (this.videos[i].selected === isSelected) {
                        if (quality === "LOW") {
                            videos.push(this.videos[i])
                        } else if (quality === "HQ") {
                            videos.push(this.videosHQ[i])
                        }
                    }
                }
                return videos;
            },
            getSelectedVideosIds() {
                let selectedVideosIds = [];
                for (let i = 0; i < this.videos.length; i++) {
                    if (this.videos[i].selected) {
                        selectedVideosIds.push(this.videos[i].id);
                    }
                }
                return selectedVideosIds;
            },
            viewSelection(bool) {
                this.isSelectedView = bool;
                let selectedVideosIds = this.getSelectedVideosIds();
                if (this.isSelectedView) {
                    if (selectedVideosIds.length > 0) {
                        if (selectedVideosIds.length <= 8) {
                            this.displayedVideos = this.getVideos(true, "HQ");
                        } else {
                            this.displayedVideos = this.getVideos(true, "LOW");
                        }
                    }
                } else {
                    this.displayedVideos = this.videos;
                }
                this.changeVideoSize();
            },
            hideSelection(bool) {
                this.isHideView = bool;
                let selectedVideosIds = this.getSelectedVideosIds();
                if (this.isHideView) {
                    if (selectedVideosIds.length > 0) {
                        if (selectedVideosIds.length <= 8) {
                            this.displayedVideos = this.getVideos(false, "HQ");
                        } else {
                            this.displayedVideos = this.getVideos(false, "LOW");
                        }
                    }
                } else {
                    this.displayedVideos = this.videos;
                }
                this.changeVideoSize();
            },
            deleteSelection() {
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
                for (let i = 0; i < this.videos.length; i++) {
                    if (this.videos[i].selected) {
                        let jsonUrl = this.videos[i].jsonUrl;
                        if (!jsonUrl.includes("/null")) {
                            window.open(this.videos[i].jsonUrl, "_blank");
                        } else console.log("no json attached to video")
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
                    this.videos = [];
                    this.videosHQ = [];
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
            this.folderName = this.$route.params.folder;
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

    #filesControls {
        display: flex;
        justify-content: flex-end;
    }

    #videoControls {
        display: flex;
    }
</style>