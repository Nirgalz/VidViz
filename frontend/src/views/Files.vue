<template>
  <div class="upload">

    <upload-files></upload-files>
  </div>
</template>

<script>
import UploadFiles from "../components/UploadFiles";

export default {
  name: 'Files',
  components: {
    UploadFiles
  }
}
</script>
