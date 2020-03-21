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

<!--        <label class="btn btn-default">-->
<!--            <input type="file" ref="file" @change="selectFile"/>-->
<!--        </label>-->
        <b-container>
            <b-form-file
                    v-model="uploadFiles"
                    :state="Boolean(uploadFiles)"
                    placeholder="Choose a file or drop it here..."
                    drop-placeholder="Drop file here..."
                    multiple
                    ref="file"
            ></b-form-file>

            <b-form-input v-model="pageName" placeholder="Enter the page name"></b-form-input>

            <!--        <div class="mt-3">Selected file: {{ selectedFiles ? selectedFiles.name : '' }}</div>-->

            <b-btn variant="success" :disabled="isUploadBtnAvailable()" @click="upload">
                Upload
            </b-btn>

            <div class="alert alert-light" role="alert">{{ message }}</div>

            <b-btn variant="danger" @click="deleteAll">Delete all files</b-btn>
<!--            <div class="card">-->
<!--                <div class="card-header">List of Files</div>-->
<!--                <ul class="list-group list-group-flush">-->
<!--                    <li-->
<!--                            class="list-group-item"-->
<!--                            v-for="(file, index) in files"-->
<!--                            :key="index"-->
<!--                    >-->
<!--                        <a :href="file.url">{{ file.name }}</a>-->
<!--                    </li>-->
<!--                </ul>-->
<!--            </div>-->
        </b-container>

        <b-row class="text-center">
            <b-col>
                <FolderList></FolderList>
            </b-col>
            <b-col cols="8">2 of 3 (wider)</b-col>
            <b-col>3 of 3</b-col>
        </b-row>

    </div>
</template>

<script>
    import UploadService from "../services/UploadFilesService";
    import FolderList from "./FolderList";

    export default {
        name: "upload-files",
        components: {FolderList},
        data() {
            return {
                uploadFiles: [],
                pageName: "",
                selectedFiles: undefined,
                currentFile: undefined,
                progress: 0,
                message: "",

                files: []
            };
        },
        methods: {
            isUploadBtnAvailable() {
                return !this.uploadFiles.length > 0 || this.pageName === "";
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
                        this.currentFile = undefined;
                    });

                this.selectedFiles = undefined;
            },
            deleteAll() {
                UploadService.deleteAll()
                    .then(
                        this.files = []
                    )
                   .catch(()=>{
                        this.message = "could not delete the files"
                })
            }
        }
        // ,
        // mounted() {
        //     UploadService.getFiles().then(response => {
        //         this.files = response.data;
        //     });
        // }
    };
</script>

<style scoped>
    ::-webkit-input-placeholder {
        text-align: center;
    }

    :-moz-placeholder { /* Firefox 18- */
        text-align: center;
    }

    ::-moz-placeholder {  /* Firefox 19+ */
        text-align: center;
    }

    :-ms-input-placeholder {
        text-align: center;
    }
</style>