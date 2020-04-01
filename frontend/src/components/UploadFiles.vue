<template>
    <div>
        <div v-if="currentFile" class="progress">
            <div
                    class="progress-bar progress-bar-info progress-bar-striped"
                    role="progressbar"
                    :aria-valuenow="progress"
                    aria-valuemin="0"
                    aria-valuemax="100"
                    :style="{ width: progress + '%' }"
            >
                {{ progress }}%
            </div>
        </div>

        <b-container>
            <b-row>
                <b-col>
                    <b-form-file
                            v-model="uploadFiles"
                            :state="Boolean(uploadFiles)"
                            placeholder="Choose a file or drop it here..."
                            drop-placeholder="Drop file here..."
                            multiple
                            plain
                            ref="files-input"
                    ></b-form-file>
                    <ul>
                        <li v-for="file in uploadFiles" :key="file.message">
                            {{file.name}}
                        </li>
                    </ul>
                </b-col>
                <b-col>
                    <b-form-input v-model="pageName" placeholder="Enter the page name"></b-form-input>


                    <b-btn :disabled="isUploadBtnAvailable()" @click="upload">
                        Upload
                    </b-btn>
                    <div class="alert" role="alert">{{ message }}</div>
                </b-col>
            </b-row>


        </b-container>


    </div>
</template>

<script>
    import UploadService from "../services/UploadFilesService";

    export default {
        name: "upload-files",
        data() {
            return {
                uploadFiles: [],
                pageName: "",
                currentFile: undefined,
                progress: 0,
                message: "",

            };
        },
        methods: {
            isUploadBtnAvailable() {
                return !this.uploadFiles.length > 0 || this.pageName === "";
            },
            refreshFilesList(){
                console.log(this.uploadFiles);
               // this.$refs['files-input'].reset();

            },
            upload() {
                this.progress = 0;
                UploadService.upload(this.uploadFiles, this.pageName, event => {
                    this.progress = Math.round((100 * event.loaded) / event.total);
                })
                    .then(response => {
                        this.message = response.data.message;
                        //return UploadService.getFiles();
                    })
                    .then(files => {
                        this.files = files.data;
                    })
                    .catch(() => {
                        this.progress = 0;
                        this.message = "Could not upload the file!";

                    });

                this.uploadFiles = [];
            }
        }
    };
</script>

<style scoped>
    ::-webkit-input-placeholder {
        text-align: center;
    }

    :-moz-placeholder { /* Firefox 18- */
        text-align: center;
    }

    ::-moz-placeholder { /* Firefox 19+ */
        text-align: center;
    }

    :-ms-input-placeholder {
        text-align: center;
    }
</style>