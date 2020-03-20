<template>
    <div>
        <b-btn variant="outline-primary" @click="callRestService()">CALL Spring Boot REST backend service</b-btn>
        <h3>{{ response }}</h3>
    </div>

</template>

<script>
    import axios from 'axios'

    export default {
        name: "TestMessage",

        data() {
            return {
                response: [],
                errors: []
            }
        },
        methods : {
            callRestService() {
                axios.get(`api/hello`)
                    .then(response => {
                        // JSON responses are automatically parsed.
                        this.response = response.data
                    })
                    .catch(e => {
                        this.errors.push(e)
                    })
            }
        }

    }
</script>

<style scoped>

</style>