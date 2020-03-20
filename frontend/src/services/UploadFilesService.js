import http from "../http-common";

class UploadFilesService {
    upload(files, onUploadProgress) {
        let formData = new FormData();

        for (let i = 0 ; i < files.length ; i++) {
            formData.append("file", files[i]);
        }

        return http.post("/upload", formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            },
            onUploadProgress
        });
    }

    getFiles() {
        return http.get("/files");
    }

    deleteAll() {
        return http.get("files/deleteall");
    }
}
export default new UploadFilesService();