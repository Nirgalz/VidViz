<template>
    <div>
        <div v-if="!loading">
            <p v-if="folders.length === 0">
                No new folders detected
            </p>
            {{message}}
            <ul>
                <li v-for="folder in folders" :key="folder.name">
                    {{folder}}
                </li>
            </ul>
            <b-btn @click="getFolders">Refresh <b-icon-arrow-repeat></b-icon-arrow-repeat></b-btn>
        </div>

        <b-spinner v-if="loading" id="spinner" variant="secondary"></b-spinner>
    </div>
</template>

<script>
    import UploadService from "../services/UploadFilesService";

    export default {
        name: "Import",
        data() {
            return {
                folders: [],
                message: "",
                loading : false
            }
        },
        methods: {
            getFolders() {
                this.loading = true;
                UploadService.getNewFolders().then(response => {
                    this.loading = false;
                    console.log(this.loading);
                    this.folders = response.data;
                })
            }
        },
        mounted() {
            this.getFolders();
        }
    }
</script>

<style scoped>
    #spinner {
        position: absolute;
        top: 45px;
        left: 50%;
    }
</style>