<template>
    <div>
        <div id="search-bar">
            <div id="search-bar-input">

                <b-input @update="filterItems" v-model="searchText" placeholder="search video name"></b-input>
            </div>
        </div>
        <br>
        <b-table :items="displayedFolders"
                 :sort-by.sync="sortBy"
                 :sort-desc.sync="sortDesc"
                 :fields="fields"
                 :per-page="perPage"
                 :current-page="currentPage">
            <template v-slot:cell(actions)="data">
                <router-link :to="{name : 'Videos', params : {folder : data.item.name}}">
                    <b-btn v-b-tooltip.hover title="View">
                        <b-icon-eye-fill></b-icon-eye-fill>
                    </b-btn>
                </router-link>

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
                    encode
                </b-btn>
            </template>
        </b-table>
        <b-pagination
                v-model="currentPage"
                :total-rows="rows"
                :per-page="perPage"
                aria-controls="my-table"
                align="center"
        ></b-pagination>
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
                displayedFolders : [],
                fields: [
                    { key:"name", sortable: true},
                    { key:"itemNumbers", sortable: true},
                    { key:"created", sortable: true},
                    { key:"actions", sortable: false},
                ],
                editOldName: "",
                editNewName: "",
                perPage: 10,
                currentPage: 1,
                sortBy: 'created',
                sortDesc: false,
                searchText : ""
            }
        },
        methods: {
            filterItems() {
                this.displayedFolders = [];
                for (let i = 0 ; i < this.folders.length ; i++) {
                    if (this.folders[i].name.toLowerCase().includes(this.searchText.toLowerCase())) {
                        this.displayedFolders.push(this.folders[i]);
                    }
                }
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
                    this.displayedFolders = response.data;
                });
            },
            encodeFolder(folderName) {
                UploadService.encodeFolder(folderName);
            }
        },
        mounted() {
            this.updateList();
        },
        computed: {
            rows() {
                return this.folders.length
            }
        }
    }
</script>

<style scoped>
    table {
        color: rgb(214, 229, 239);
    }
    #search-bar {
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    #search-bar-input {
        width: 200px;
    }

</style>