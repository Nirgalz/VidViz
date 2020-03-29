<template>
    <div>
        <b-table hover :items="folders" :fields="fields">
            <template v-slot:cell(actions)="data">
                <b-btn variant="success" @click="loadFilesToPage(data.item.name)">
                    View
                    <b-icon-eye-fill></b-icon-eye-fill>
                </b-btn>
                |
                <b-btn variant="info" v-b-modal.modal-editFolder @click="selectFolderToEdit(data.item.name)">
                    Edit
                    <b-icon-pencil></b-icon-pencil>
                </b-btn>
                |
                <b-btn variant="danger" @click="deleteItem(data.item.name)">
                    Delete
                    <b-icon-x-circle-fill></b-icon-x-circle-fill>
                </b-btn>
            </template>
        </b-table>
        <b-modal id="modal-editFolder" title="Edit" @ok="editItem">
            <b-form-input v-model="editNewName" placeholder="Enter a new folder name"></b-form-input>
        </b-modal>
    </div>
</template>

<script>
    import UploadService from "../services/UploadFilesService";

    export default {
        name: "Explorer",
        data() {
            return {
                folders: [],
                fields: [
                    "name",
                    "itemNumbers",
                    "actions"
                ],
                editOldName: "",
                editNewName: ""
            }
        },
        methods: {
            loadFilesToPage(item) {
                this.$emit('loadFiles', item)
            },
            selectFolderToEdit(name) {
                this.editOldName = name;
            },
            editItem() {
                UploadService.editFolder(this.editOldName, this.editNewName).then(this.updateList);
            },
            deleteItem(item) {
                UploadService.deleteFolder(item).then(this.updateList);
            },
            updateList() {
                UploadService.getFolders().then(response => {
                    this.folders = response.data;
                });
            }
        },
        mounted() {
           this.updateList();
        }
    }
</script>

<style scoped>

</style>