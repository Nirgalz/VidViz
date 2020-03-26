<template>
  <div class="home">
    <HelloWorld msg="yoyoyo"/>
    <TestMessage></TestMessage>
  </div>
</template>

<script>
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
