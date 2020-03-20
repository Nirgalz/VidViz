<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="yoyoyo"/>
    <TestMessage></TestMessage>
    <upload-files></upload-files>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '../components/HelloWorld.vue'
import TestMessage from "../components/TestMessage";
import UploadFiles from "../components/UploadFiles";

export default {
  name: 'Home',
  components: {
    TestMessage,
    HelloWorld,
    UploadFiles
  }
}
</script>
