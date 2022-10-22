<template>

  <DataTable
      :hover="true"
      :page="pageIndex+1"
      ref="dt"
      dataKey="id"
      :value="timelines"
      :lazy="true"
      :paginator="true"
      :rows="pageSize"
      :totalRecords="totalRecords"
      filterDisplay="row"
      v-model:filters="filters"
      @page="onPage($event)"
      @filter="onFilter($event)"
      :loading="loading">

    <Column field="id" header="ID"></Column>

    <Column field="projectName" header="PROJECT NAME" filterMatchMode="startsWith" ref="projectName">
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by project name"/>
      </template>
    </Column>
    <Column field="note" header="NOTE">
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by note"/>
      </template>
    </Column>
    <Column field="fromTime" header="FROM"></Column>
    <Column field="toTime" header="TO"></Column>
    <Column field="workTime" header="WORKTIME"></Column>
    <Column field="pause" header="PAUSE"></Column>

  </DataTable>

</template>

<script lang="ts" setup>

import {inject, onMounted, ref} from 'vue'
import {AxiosStatic} from "axios";
import DataTable, {DataTableFilterEvent, DataTablePageEvent} from 'primevue/datatable';
import InputText from 'primevue/inputtext'
import Column from 'primevue/column';

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define properties
const timelines = ref<any[]>([])
const loading = ref<boolean>(true)
const dt = ref(null);
const pageIndex = ref<number>(0)
const pageSize = ref<number>(25)
const totalRecords = ref<number>(0)
const filters = ref({
  note: {value: '', matchMode: 'contains'},
  projectName: {value: '', matchMode: 'contains'}
})

// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define methods
function loadData(filterData: any = {}) {
  loading.value = true
  axios?.post("/datatable/timeline?sort=fromTime=" + pageIndex.value + "&pageSize=" + pageSize.value, filterData)
      .then((response) => {
        if (response.status === 200) {
          timelines.value = response.data.rows
        }
        totalRecords.value = response.data.paginator.totalRecords
        loading.value = false
      }).catch(error => {
    console.error('Can not load companies', error)
    loading.value = false
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

    if (_value.length>0) {
      filter[key] = {
        type: getFilterType(matchMode),
        value: '%'+_value+'%'
      }
    }
  }
  return filter
}

function getFilterType(vueType: string): string {
  switch(vueType.toUpperCase()) {
    case 'CONTAINS': return 'like'
    default: return '='
  }
}


</script>

<style scoped>

</style>
