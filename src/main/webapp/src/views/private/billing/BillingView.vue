<template>

  <h2>billing view</h2>

</template>

<script lang="ts" setup>
import {inject, onMounted, ref} from "vue";
import {AxiosStatic} from "axios";

// Define inject
const axios = inject<AxiosStatic>('axios')
// Define component properties
const billing = ref(null)
// Define lifecycle hooks
onMounted(() => {

  axios?.get('/billing/list').then(response => {
    console.log(response)
    if (response.status >= 200 && response.status <=299) {
      billing.value = response.data
    } else {
      console.error('Can not load billing from remote server!')
    }
  })

})
// Define methods



</script>

<style scoped>

</style>
