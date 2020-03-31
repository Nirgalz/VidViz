<template>
    <div>
        <p v-if="folders.length === 0">
            No new folders detected
        </p>
        <p v-if="folders.length> 0">
            The following folders have been detected, import them ?
            <b-btn variant="success" @click="processFolders"><b-icon-arrow-right ></b-icon-arrow-right> </b-btn>
        </p>
        {{message}}
        <ul>
            <li v-for="folder in folders" :key="folder.name">
                {{folder}}
            </li>
        </ul>
    </div>
</template>

<script>
    import UploadService from "../services/UploadFilesService";

    export default {
        name: "Import",
        data() {
            return {
                folders: [],
                message: ""
            }
        },
        methods: {
            processFolders() {
                UploadService.processFolders().then(response => {
                    this.message = response.data.message;
                    this.getFolders();
                });
            },
            getFolders() {
                UploadService.getNewFolders().then(response => {
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

</style>