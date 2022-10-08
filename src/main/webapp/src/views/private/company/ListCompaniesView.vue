<template>

  <h1>View companies</h1>

</template>

<script lang="ts" setup>

import {inject, onMounted} from "vue";
import {AxiosStatic} from "axios";

const axios = inject<AxiosStatic>('axios')


onMounted(() => {
  axios?.post("/datatable/companies?sort=companyName&page=0&pageSize=25", {})
      .then((response) => {
        console.log('Response for companies: ', response)
      })
})


</script>

<style scoped>

</style>
