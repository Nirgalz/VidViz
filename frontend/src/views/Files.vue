<template>
  <div class="upload">

    <upload-files></upload-files>

    <Explorer></Explorer>
  </div>
</template>

<script>
import UploadFiles from "../components/UploadFiles";
import Explorer from "../components/Explorer";

export default {
  name: 'Files',
  components: {
    UploadFiles,
    Explorer
  }
}
</script>
