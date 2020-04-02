<template>
  <div id="app">
    <b-dropdown id="menu" class="m-md-2" no-caret>
      <template v-slot:button-content>
        <b-icon-list></b-icon-list>
      </template>
      <b-dropdown-item>
        <router-link to="/explorer">
          Explorer
          <b-icon-folder></b-icon-folder></router-link>
      </b-dropdown-item>

      <b-dropdown-divider></b-dropdown-divider>
      <b-dropdown-item >
        <router-link to="/scan">
          Scan folder
          <b-icon-folder-symlink></b-icon-folder-symlink>
        </router-link>

      </b-dropdown-item>
      <b-dropdown-item>
        <router-link to="/upload">
          Upload
          <b-icon-upload></b-icon-upload>
        </router-link>

      </b-dropdown-item>
    </b-dropdown>
    <router-view/>
  </div>
</template>

<style>
  body, html {
    padding: 0;
    margin: 0;
    width: 100%;
    min-height: 100%;
    background-color: #2d2d2d;
  }

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: rgb(214, 229, 239);
  background-color: #2d2d2d;
}

  #menu {
    position: fixed;
    top: 0px;
    left: 0px;
    z-index: 1000;
  }

#nav {
  /*padding: 30px;*/
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
</style>
