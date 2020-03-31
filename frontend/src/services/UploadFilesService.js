import http from "../http-common";

class UploadFilesService {
    upload(files, page, onUploadProgress) {
        let formData = new FormData();

        for (let i = 0 ; i < files.length ; i++) {
            formData.append("file", files[i]);

        }

        formData.append("pageName", page);

        return http.post("/upload", formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            },
            maxContentLength: 100000000,
            maxBodyLength: 1000000000,
            onUploadProgress
        })
    }

    getFolders() {
        return http.get("/folders");
    }

    getFiles(selectedFolder) {
        return http.get("/folder/"+selectedFolder);
    }

    deleteFolder(folderName) {
        return http.get("/action/folders/delete/"+folderName);
    }

    editFolder(oldName, newName) {
        let formData = new FormData();
        formData.append("oldName", oldName);
        formData.append("newName", newName);
        return http.post("/action/folders/rename/", formData)
    }

    deleteAll() {
        return http.get("files/deleteall");
    }

    getFile(folderName, fileName) {
        return http.get("files/"+folderName+"/"+fileName);
    }

    deleteFile(id) {
        let formData = new FormData();
        formData.append("id", id);
        return http.post("action/files/delete", formData)
    }

    getNewFolders() {
        return http.get("action/folders/templist");
    }

    processFolders() {
        return http.get("action/folders/process");
    }
}
export default new UploadFilesService();