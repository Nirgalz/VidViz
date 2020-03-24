<template>
    <div>
        <b-table hover :items="folders" @row-clicked="(item, index, event) => loadFilesToPage(item)"></b-table>
    </div>
</template>

<script>
    import UploadService from "../services/UploadFilesService";

    export default {
        name: "Explorer",
        data() {
            return {
                folders: []
            }
        },
        methods: {
            loadFilesToPage(item) {
                UploadService.getFiles(item.name).then(response => {
                    this.files = response.data;
                    this.$emit('loadFiles', item.name)
                });
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