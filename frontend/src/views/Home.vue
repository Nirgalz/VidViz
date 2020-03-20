<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="yoyoyo"/>
    <TestMessage></TestMessage>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '../components/HelloWorld.vue'
import TestMessage from "../components/TestMessage";

export default {
  name: 'Home',
  components: {
    TestMessage,
    HelloWorld
  }
}
</script>
