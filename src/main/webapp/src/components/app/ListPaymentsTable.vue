<template>

  <DataTable
      :page="pageIndex+1"
      ref="dt"
      dataKey="id"
      :value="items"
      :paginator="true"
      :rows="pageSize"
      :rowHover="true"
      :totalRecords="totalRecords"
      :loading="loading">

    <Column field="paymentDate" header="Datum">
      <template #body="{data}">{{ formatDate(data.paymentDate) }}</template>
    </Column>
    <Column field="payment" header="Částka"></Column>
    <Column field="note" header="Poznámka"></Column>
  </DataTable>

</template>

<script lang="ts" setup>

import {defineExpose, inject, onMounted, ref} from 'vue'
import {Billing, PaymentType} from "@/components/blocks/Types";
import {buildMoment} from "@/components/blocks/Functions";
import {AxiosStatic} from "axios";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import {useUserStore} from "@/stores/UserStore";
import {formatDate} from "../../services/FormatService";

// Define store
const userStore = useUserStore()

// Define inject
const axios = inject<AxiosStatic>('axios')

// Define refs
const items = ref<PaymentType[]>([])
const loading = ref<boolean>(true)
const dt = ref(null);
const pageIndex = ref<number>(0)
const pageSize = ref<number>(25)
const totalRecords = ref<number>(0)

// Define lifecycle hooks
onMounted(() => {
  loadData()
})
// Define function
function loadData() {
  loading.value = true
  axios?.get('/payment/list?companyId=' + userStore.userDetail.company.id)
      .then((response) => {
        if (response.status === 200) {
          items.value = response.data
        }
        totalRecords.value = response.data.length
        loading.value = false
      }).catch(error => {
    console.error('Can not load payments', error)
    loading.value = false
  })
}


// Define expose
defineExpose({loadData})

</script>

<style scoped>

</style>
