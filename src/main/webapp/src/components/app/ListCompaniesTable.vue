<template>

  <Message v-if="companies.length==0" severity="info" :closable="false">There is not any company in database.</Message>

  <DataTable
      class="mt-2 mb-2"
      v-if="companies.length>0"
      :page="pageIndex+1"
      ref="dt"
      dataKey="id"
      :value="companies"
      :lazy="true"
      :paginator="true"
      :rows="pageSize"
      :totalRecords="totalRecords"
      filterDisplay="row"
      v-model:filters="filters"
      @page="onPage($event)"
      @filter="onFilter($event)"
      :loading="loading">

    <Column field="id" header="ID">
      <template #body="{data}">{{ data.id }}</template>
    </Column>
    <Column header="PRIMARY">
      <template #body="{data}">
        <span v-if="data.id == userStore.userDetail.company">YES</span>
        <span v-if="!userStore.hasPrimaryCompany">
          <i class="i-button pi cursor-pointer pi-pencil text-blue-500"
             @click="setAsPrimary(data)">
        </i>
          </span>
      </template>
    </Column>
    <Column field="companyName" header="COMPANY NAME" filterMatchMode="startsWith" ref="companyName">
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by company name"/>
      </template>
    </Column>
    <Column field="ic" header="IC">
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by company name"/>
      </template>
    </Column>
    <Column field="dic" header="DIC"></Column>
    <Column field="email" header="E-MAIL"></Column>

  </DataTable>

</template>

<script lang="ts" setup>

import {defineExpose, inject, onMounted, ref} from 'vue'
import {AxiosStatic} from "axios";
import DataTable, {DataTableFilterEvent, DataTablePageEvent} from 'primevue/datatable';
import InputText from 'primevue/inputtext'
import Column from 'primevue/column';
import Message from 'primevue/message';
import {useUserStore} from "@/stores/UserStore";

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define properties
const companies = ref<any[]>([])
const loading = ref<boolean>(true)
const dt = ref(null);
const pageIndex = ref<number>(0)
const pageSize = ref<number>(25)
const totalRecords = ref<number>(0)
const filters = ref({
  companyName: {value: '', matchMode: 'contains'},
  ic: {value: '', matchMode: 'contains'}
})
const userStore = useUserStore()

// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define methods
function loadData(filterData: any = {}) {
  loading.value = true
  axios?.post("/datatable/companies?sort=companyName&page=" + pageIndex.value + "&pageSize=" + pageSize.value, filterData)
      .then((response) => {
        if (response.status === 200) {
          companies.value = response.data.rows
        }
        totalRecords.value = response.data.paginator.totalRecords
        loading.value = false
      }).catch(error => {
    console.error('Can not load companies', error)
    loading.value = false
  })
}

function setAsPrimary(row: any) {
  axios?.put(`/company/primary/${row.id}`).then(res => {
    if (res.data.id > 0) {
      userStore.setCompany(res.data.id)
    }
  })
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
  for (let a = 0; a < keys.length; a++) {
    const key = keys[a]
    const {value: _value, matchMode} = vueFilter[keys[a]]

    if (_value.length > 0) {
      filter[key] = {
        type: getFilterType(matchMode),
        value: '%' + _value + '%'
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

function reload() {
  loadData(mapFilterToDataTablePayload(filters.value))
}

defineExpose({reload})

</script>

<style scoped>

</style>
