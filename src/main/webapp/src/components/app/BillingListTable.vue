<template>
  <ConfirmDialog></ConfirmDialog>
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
    <Column field="companyName" header="COMPANY"></Column>
    <Column header="WORK TIME">
      <template #body="{data}"><div class="text-right">{{formatWorkTime(data.workTime)}}</div></template>
    </Column>
    <Column field="earn" header="EARN">
      <template #body="{data}"><div class="text-right">{{formatPrice(data.earn)}}</div></template>
    </Column>
    <Column field="note" header="NOTE"></Column>
    <Column header="#">
      <template #body="{data}">
        <Button class="p-button-sm" type="button" @click="handleClick(data)">Create Invoice</Button>
      </template>
    </Column>
  </DataTable>

</template>

<script lang="ts" setup>

import moment from "moment";
import ConfirmDialog from "primevue/confirmdialog";
import DataTable from "primevue/datatable";
import Button from "primevue/button";
import Column from "primevue/column";
import {inject, onMounted, ref} from "vue";
import {AxiosStatic} from "axios";
import {Billing} from "@/components/blocks/Types";
import {formatWorkTime, formatPrice} from "@/services/FormatService";
import ClickIcon from "@/components/blocks/ClickIcon.vue";
import {useConfirm} from "primevue/useconfirm";
import {useRouter} from "vue-router";

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define properties
const items = ref<Billing[]>([])
const loading = ref<boolean>(true)
const dt = ref(null);
const pageIndex = ref<number>(0)
const pageSize = ref<number>(25)
const totalRecords = ref<number>(0)
const confirm = useConfirm()
const router = useRouter()

// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define methods
function handleClick(input: Billing) {

  confirm.require({
    message: 'Do you want generate invoice for ' + input.companyName + ' with description ' + input.note + ' and price ' + formatPrice(input.earn),
    header: input.companyName + ': Generate Invoice',
    icon: 'pi pi-question-circle',
    accept: () => {
      generateInvoice(input)
    }
  })
}

function generateInvoice(input: Billing) {
  const issueDate = moment().subtract(1,'months').endOf('month').format('YYYY-MM-DD');
  axios?.get('/invoice/v2/generate?companyID='+input.companyId+'&issueDate='+issueDate)
      .then(response=>{
        if (response.status === 200) {
          const invoice = response.data.data
          router.push({
            path: '/private/invoices' + invoice.id
          })
        } else {
          console.error('Can not generate invoice!')
        }
      })
}

function loadData(filterData: any = {}) {
  loading.value = true
  axios?.get("/billing/list")
      .then((response) => {
        if (response.status === 200) {
          items.value = response.data
        }
        totalRecords.value = response.data.length
        loading.value = false
      }).catch(error => {
    console.error('Can not load billing', error)
    loading.value = false
  })
}



</script>

<style scoped>

</style>
