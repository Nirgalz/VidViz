<template>
    <div>
        <b-table  hover :items="folders"></b-table>
    </div>
</template>

<script>

    import UploadService from "../services/UploadFilesService";
    export default {
        name: "FolderList",
        data() {
            return {
                folders: []
            }
        },
        mounted() {
            UploadService.getFolders().then(response => {
                this.folders = response.data;
            });
        }
    }
</script>

<style scoped>

</style>