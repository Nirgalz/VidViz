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
            onUploadProgress
        });
    }

    getFolders() {
        return http.get("/folders");
    }

    getFiles(selectedFolder) {
        return http.get("/folder/"+selectedFolder);
    }

    deleteAll() {
        return http.get("files/deleteall");
    }
}
export default new UploadFilesService();