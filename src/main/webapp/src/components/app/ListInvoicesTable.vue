<template>

  <DataTable
      :rowHover="true"
      :page="pageIndex+1"
      ref="dt"
      dataKey="id"
      :value="items"
      :lazy="true"
      :paginator="true"
      :rows="pageSize"
      :totalRecords="totalRecords"
      filterDisplay="row"
      v-model:filters="filters"
      @page="onPage($event)"
      @filter="onFilter($event)"
      :loading="loading">

    <Column header="#">
      <template #body="{data}">
        <router-link :to="'/private/invoices/'+data.id">
          <i class="pi pi-folder-open"></i>
        </router-link>

      </template>
    </Column>

    <Column header="NUMBER" field="number">
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by number"/>
      </template>
    </Column>

    <Column header="ISSUE DATE" field="issueDate">
      <template #body="{data}">{{ getDate(data.issueDate) }}</template>
    </Column>

    <Column header="PAYMENT DATE" field="paymentDate">
      <template #body="{data}">{{ getDate(data.paymentDate) }}</template>
    </Column>

    <Column header="ISSUER" field="issuer">
      <template #body="{data}">{{ data.issuerCompany.companyName }}</template>
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by issuer"/>
      </template>
    </Column>

    <Column header="ISSUER" field="recipient">
      <template #body="{data}">{{ data.recipientCompany.companyName }}</template>
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by recipient"/>
      </template>
    </Column>

    <Column header="PRICE">
      <template #body="{data}">
        <div class="text-right">
          {{ calculateTotalPrice(data.items, 'price') }}
        </div>
      </template>
    </Column>

<Column header="VAT PRICE">
      <template #body="{data}">
        <div class="text-right">
          {{ calculateTotalPrice(data.items,'totalPrice') }}
        </div>
      </template>
    </Column>



  </DataTable>
</template>

<script lang="ts" setup>

import {defineExpose, inject, onMounted, ref} from 'vue'
import {AxiosStatic} from "axios";
import DataTable, {DataTableFilterEvent, DataTablePageEvent} from 'primevue/datatable';
import InputText from 'primevue/inputtext'
import Column from 'primevue/column';
import moment from "moment";

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define properties
const items = ref<any[]>([])
const loading = ref<boolean>(true)
const dt = ref(null);
const pageIndex = ref<number>(0)
const pageSize = ref<number>(25)
const totalRecords = ref<number>(0)
const filters = ref({
  number: {value: '', matchMode: 'contains'},
  issuer: {value: '', matchMode: 'contains'},
  recipient: {value: '', matchMode: 'contains'},

})

// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define methods
function calculateTotalPrice(items: any[], field: string): number {
  return items.reduce((curr, acc) => acc[field] + curr, 0).toFixed(2)
}

function reloadTable() {
  console.log('Reload table invoked!')
  loadData(mapFilterToDataTablePayload(filters.value))
}

function loadData(filterData: any = {}) {
  loading.value = true
  axios?.post("/datatable/invoices?sort=vatPaymentDate&page=" + pageIndex.value + "&pageSize=" + pageSize.value, filterData)
      .then((response) => {
        if (response.status === 200) {
          items.value = response.data.rows
        }
        totalRecords.value = response.data.paginator.totalRecords
        loading.value = false
      }).catch(error => {
    console.error('Can not load timeline', error)
    loading.value = false
  })
}

function getDay(row: any): string {
  return moment(row.fromTime).format("dddd")
}

function getTime(dateValue: string): string {
  return moment(dateValue).format("HH:mm")
}

function getDate(dateValue: string): string {
  return moment(dateValue).format("DD.MM.YYYY")
}

function onPage(event: DataTablePageEvent) {
  pageIndex.value = event.page
  loadData()
}

function onFilter(event: DataTableFilterEvent) {
  loadData(mapFilterToDataTablePayload(filters.value))
}

function mapFilterToDataTablePayload(vueFilter: any): any {
  const filter: any = {}
  const keys = Object.keys(vueFilter)

  console.log(vueFilter)

  for (let a = 0; a < keys.length; a++) {
    const key = keys[a]
    const {value: _value, matchMode} = vueFilter[keys[a]]
    if (_value != null && _value.length > 0) {

      switch (key) {
        case 'issuer':
          filter['issuerCompany'] = {
            type: getFilterType(matchMode),
            value: '%' + _value + '%',
            childKey: "companyName"
          }
          break
        case 'recipient':
          filter['recipientCompany'] = {
            type: getFilterType(matchMode),
            value: '%' + _value + '%',
            childKey: "companyName"
          }
          break;
        default:
          filter[key] = {
            type: getFilterType(matchMode),
            value: '%' + _value + '%'
          }
      }
    }
  }
  return filter
}

function getFilterType(vueType: string): string {
  switch (vueType.toUpperCase()) {
    case 'CONTAINS':
      return 'like'
    default:
      return '='
  }
}

// Define exposes
defineExpose({reloadTable})


</script>

<style scoped>

</style>
