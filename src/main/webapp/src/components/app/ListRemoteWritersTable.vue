<template>
  <DataTable
      :page="pageIndex+1"
      ref="dt"
      dataKey="id"
      :value="items"
      :paginator="true"
      :rows="pageSize"
      :totalRecords="totalRecords"
      filterDisplay="row"
      :loading="loading">

    <Column field="id" header="ID"></Column>
    <Column field="keyPattern" header="PATTERN"></Column>
    <Column field="remoteWriterList" header="WRITERS"></Column>
    <Column field="remoteWriterList" header="WRITERS"></Column>

  </DataTable>

</template>

<script lang="ts" setup>

import DataTable from "primevue/datatable";
import Column from "primevue/column";

import moment from "moment";

import {inject, onMounted, ref, defineExpose} from "vue";
import {AxiosStatic} from "axios";

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define properties
const items = ref<any[]>([])
const loading = ref<boolean>(true)
const dt = ref(null);
const pageIndex = ref<number>(0)
const pageSize = ref<number>(25)
const totalRecords = ref<number>(0)

// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define methods
function loadData(filterData: any = {}) {
  loading.value = true
  axios?.get("/remote-writer/list")
      .then((response) => {
        if (response.status === 200) {
          items.value = response.data
        }
        totalRecords.value = response.data.length
        loading.value = false
      }).catch(error => {
    console.error('Can not load writers settings', error)
    loading.value = false
  })
}

function reload() {
  loadData()
}

defineExpose({reload})

</script>

<style scoped>

</style>
