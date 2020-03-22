<template>
    <video-player  class="video-player-box"
                   ref="videoPlayer"
                   :options="playerOptions"
                   :playsinline="true"
                   >
    </video-player>
</template>

<script>
    export default {
        name: "VPlayer",
        data() {
            return {
                playerOptions: {
                    // videojs options
                    muted: true,
                    language: 'en',
                    sources: [{
                        type: "video/mp4",
                        src: "https://cdn.theguardian.tv/webM/2015/07/20/150716YesMen_synd_768k_vp8.webm"
                    }]
                }
            }
        },

        computed: {
            player() {
                return this.$refs.videoPlayer.player
            }
        }
    }
</script>

<style scoped>

</style>