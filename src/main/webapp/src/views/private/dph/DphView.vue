<template>

  <Panel header="Select month"
         class="mt-2">

    <form @submit.prevent="handleSubmit()" class="p-fluid">

      <div class="card">
        <div class="formgrid grid">

          <div class="field col-6 sm:col-4 md:col-3 lg:col-2 xl:col-1">
            <Dropdown v-model="formData.year" :options="computedYears"/>
          </div>

          <div class="field col-6 sm:col-4 md:col-3 lg:col-2 xl:col-1">
            <Dropdown v-model="formData.month" optionLabel="value" :options="computedMonths"/>
          </div>

          <div class="field col-6 sm:col-2 md:col-3 lg:col-2 xl:col-1">
            <Button type="submit" label="Select"/>
          </div>

          <div class="field col-6 sm:col-3 md:col-3 lg:col-2 xl:col-1">
            <Button icon="pi pi-download" type="button" label="Download" @click="processContentDownload()"/>
          </div>

        </div>
      </div>
    </form>
  </Panel>


  <DataTable
      class="mt-2"
      :rowHover="true"
      ref="dt"
      dataKey="id"
      :value="fii"
      :totalRecords="fii.length">

    <template #header>
      <div class="text-left">
        <h3 class="m-0">Issued invoices</h3>
      </div>
    </template>

    <Column field="number" header="NUMBER"></Column>
    <Column field="issueDate" header="ISSUE DATE"></Column>
    <Column field="vatPaymentDate" header="VAT PAYMENT DATE"></Column>
    <Column field="paymentDate" header="PAYMENT DATE"></Column>
    <Column field="issuerName" header="ISSUER"></Column>
    <Column field="recipientName" header="RECIPIENT"></Column>
    <Column header="PRICE">
      <template #body="{data}">
        {{ formatPrice(data.price) }}
      </template>
    </Column>
    <Column header="TOTAL PRICE">
      <template #body="{data}">
        {{ formatPrice(data.totalPrice) }}
      </template>
    </Column>
    <Column header="VAT">
      <template #body="{data}">
        {{ formatPrice(data.vatAmount) }}
      </template>
    </Column>
  </DataTable>


  <DataTable
      class="mt-2"
      :rowHover="true"
      ref="dt"
      dataKey="id"
      :value="fri"
      :totalRecords="fri.length">

    <template #header>
      <div class="text-left">
        <h3 class="m-0">Received invoices</h3>
      </div>
    </template>

    <Column field="number" header="NUMBER"></Column>
    <Column field="issueDate" header="ISSUE DATE"></Column>
    <Column field="vatPaymentDate" header="VAT PAYMENT DATE"></Column>
    <Column field="paymentDate" header="PAYMENT DATE"></Column>
    <Column field="issuerName" header="ISSUER"></Column>
    <Column field="recipientName" header="RECIPIENT"></Column>
    <Column header="PRICE">
      <template #body="{data}">
        {{ formatPrice(data.price) }}
      </template>
    </Column>
    <Column header="TOTAL PRICE">
      <template #body="{data}">
        {{ formatPrice(data.totalPrice) }}
      </template>
    </Column>
    <Column header="VAT">
      <template #body="{data}">
        {{ formatPrice(data.vatAmount) }}
      </template>
    </Column>
  </DataTable>

</template>

<script lang="ts" setup>
import {computed, inject, onMounted, reactive, Ref, ref} from "vue";
import {AxiosResponse, AxiosStatic} from "axios";
import moment from "moment";
import Dropdown from "primevue/dropdown";
import Panel from "primevue/panel";
import Button from "primevue/button";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import {formatPrice} from "@/services/FormatService";
import {useUserStore} from "@/stores/UserStore";
import {MonthType, months} from "@/components/blocks/Types";

// Define types

type FormData = {
  year: number
  month: MonthType
}

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define refs
const issuedInvoice = ref([])
const receivedInvoice = ref([])

const fii = ref<any[]>([])
const fri = ref<any[]>([])

const minYear = ref<number>(2000)
const maxYear = ref<number>(3000)

// Define reactive
const formData = reactive<FormData>({
  year: 2000,
  month: {index: 0, value: 'Leden'},
})

const store = useUserStore()


// Define lifecycle hooks
onMounted(() => {
  const m = moment().subtract(1, 'month')
  formData.month = {
    index: m.month(),
    value: months[m.month()]
  }
  axios?.get('/dph/issued-invoice').then(response => responseProcessor(response, issuedInvoice, 'i'))
  axios?.get('/dph/received-invoice').then(response => responseProcessor(response, receivedInvoice, 'r'))
})

// Define function
const responseProcessor = (response: AxiosResponse<any, any>, data: Ref, type: string): void => {
  if (response.status >= 200 && response.status <= 299) {
    data.value = response.data
    if (response.data.length > 0) {
      const lastRecord = moment(response.data[response.data.length - 1].issueDate)
      const firstRecord = moment(response.data[0].issueDate)
      minYear.value = Math.max(minYear.value, lastRecord.year())
      maxYear.value = Math.min(maxYear.value, firstRecord.year())
      formData.year = Math.max(maxYear.value)

      switch (type) {
        case 'i':
          fii.value = filterBySelectedDate(data.value);
          break;
        case 'r':
          fri.value = filterBySelectedDate(data.value);
          break;
      }
    }
  } else {
    console.error('Can not load issued invoice')
  }
}

function handleSubmit() {
  fii.value = filterBySelectedDate(issuedInvoice.value)
  fri.value = filterBySelectedDate(receivedInvoice.value)
}

function filterBySelectedDate(input: Array<any>): Array<any> {
  return input.filter(value => {
    const m = moment(value.issueDate)
    return m.month() == formData.month.index && m.year() == formData.year
  })
}

function processContentDownload() {

  const path = formData.year+'-'+(formData.month.index + 1).toString(10).padStart(2,'0')+'-01'

  axios?.get('/dph/download/' + path + '/' + store.userDetail.company, {
    responseType: 'blob'
  }).then(response => {
    const objectURL = URL.createObjectURL(response.data)
    const link = document.createElement('a')
    link.href = objectURL
    link.setAttribute('download', 'dph-' + path + '.zip')
    document.body.appendChild(link)
    link.click()
    link.remove()
    window.URL.revokeObjectURL(link.href)

  }).catch(error => {
    alert('Error while downloading file')
    console.error(error)
  })
}

// Define enumerated
const computedYears = computed(() => {
  let years = []
  for (let a = minYear.value; a <= maxYear.value; a++) {
    years.push(a)
  }
  return years
})

const computedMonths = computed(() => {
  return months.map((_item, _index) => {
    return {
      index: _index,
      value: _item
    } as MonthType
  })
})


</script>

<style scoped>

</style>
