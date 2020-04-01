<template>
    <div>
        <b-table :items="folders" :fields="fields">
            <template v-slot:cell(actions)="data">

                <b-btn @click="loadFilesToPage(data.item.name)"
                       v-b-tooltip.hover title="View">
                    <b-icon-eye-fill></b-icon-eye-fill>
                </b-btn>
                |
                <b-btn v-b-modal.modal-editFolder
                       @click="selectFolderToEdit(data.item.name)"
                       v-b-tooltip.hover title="Edit">
                    <b-icon-pencil></b-icon-pencil>
                </b-btn>
                |
                <b-btn @click="deleteItem(data.item.name)"
                       v-b-tooltip.hover title="Delete">
                    <b-icon-trash-fill></b-icon-trash-fill>
                </b-btn>
                |
                <b-btn @click="encodeFolder(data.item.name)"
                       v-b-tooltip.hover title="Encode">
                    encode</b-btn>
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
                    "created",
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
            },
            encodeFolder(folderName) {
                UploadService.encodeFolder(folderName);
            }
        },
        mounted() {
           this.updateList();
        }
    }
</script>

<style scoped>
 table {
     color: rgb(214, 229, 239);
 }
</style>