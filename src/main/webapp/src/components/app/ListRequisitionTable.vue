<template>

  <DataTable
      :rowHover="true"
      :page="pageIndex+1"
      ref="dt"
      dataKey="id"
      :value="requisitions"
      :paginator="true"
      :rows="pageSize"
      :totalRecords="totalRecords"
      filterDisplay="row"
      :loading="loading">

    <Column field="id" header="ID"></Column>
    <Column field="companyName" header="COMPANY NAME" filterMatchMode="startsWith" ref="companyName"></Column>
    <Column field="startDate" header="Platne od">
      <template #body="{data}">{{ formatTimestamp(data.startDate) }}</template>
    </Column>
    <Column field="endDate" header="Termin dokonceni">
      <template #body="{data}">{{ formatTimestamp(data.endDate) }}</template>
    </Column>
    <Column field="note" header="NOTE"></Column>
    <Column field="documentName" header="DOCUMENT"></Column>
  </DataTable>

</template>

<script lang="ts" setup>

import DataTable from "primevue/datatable";
import Column from "primevue/column";
import moment from "moment";
import {defineExpose, inject, onMounted, ref} from "vue";
import {AxiosStatic} from "axios";

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define properties
const requisitions = ref<any[]>([])
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
function formatTimestamp(input: string): string {
  return moment(input).format('DD.MM.YYYY')
}

function loadData(filterData: any = {}) {
  loading.value = true
  axios?.get("/requisition")
      .then((response) => {
        if (response.status === 200) {
          requisitions.value = response.data
        }
        totalRecords.value = response.data.length
        loading.value = false
      }).catch(error => {
    console.error('Can not load requisition', error)
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
