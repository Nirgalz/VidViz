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

    deleteAll() {
        return http.get("files/deleteall");
    }

    getFile(folderName, fileName) {
        return http.get("files/"+folderName+"/"+fileName);
    }
}
export default new UploadFilesService();