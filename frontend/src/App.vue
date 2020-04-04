<template>
    <div id="app">
        <b-dropdown id="menu" class="m-md-2" no-caret>
            <template v-slot:button-content>
                <b-icon-list></b-icon-list>
            </template>
            <b-dropdown-item class="dropItem">
                <router-link to="/explorer">
                    <b-icon-folder></b-icon-folder>
                    Explorer
                </router-link>
            </b-dropdown-item>

            <b-dropdown-divider></b-dropdown-divider>
            <b-dropdown-item class="dropItem">
                <router-link to="/scan">
                    <b-icon-folder-symlink></b-icon-folder-symlink>
                    Scan folder
                </router-link>

            </b-dropdown-item>
            <b-dropdown-item class="dropItem">
                <router-link to="/upload">
                    <b-icon-upload></b-icon-upload>
                    Upload
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

    ul {
        background: #87939a !important;
        border: 1px solid #d6e5ef !important;
        box-shadow: 0 2px 2px 0 rgba(0, 0, 0, .14), 0 3px 1px -2px rgba(0, 0, 0, .2), 0 1px 5px 0 rgba(0, 0, 0, .12) !important;

    }

    .dropItem:hover {
        background: #d6e5ef !important;
        color: #2d2d2d !important;
    }

    a {
        font-weight: bold;
        color: #d6e5ef !important;
        text-decoration: none !important;
    }

    a:hover {
        color: #2d2d2d !important;
    }

</style>
