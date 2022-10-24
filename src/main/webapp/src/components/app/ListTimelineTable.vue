<template>

  <DataTable
      :rowHover="true"
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


    <Column header="Day">
      <template #body="{data}">{{ getDay(data) }}</template>
    </Column>

    <Column header="Date">
      <template #body="{data}">{{ getDate(data.fromTime) }}</template>
    </Column>

    <Column header="From time">
      <template #body="{data}">{{ getTime(data.fromTime) }}</template>
    </Column>

    <Column header="To time">
      <template #body="{data}">{{ getTime(data.toTime) }}</template>
    </Column>

    <Column field="pause" header="Pause"></Column>
    <Column field="workTime" header="Worktime"></Column>

    <Column field="projectName" header="Project" filterMatchMode="startsWith" ref="projectName">
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by project name"/>
      </template>
    </Column>

    <Column field="note" header="Note">
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by note"/>
      </template>
    </Column>
    <Column field="pause" header="Pause"></Column>
  </DataTable>
</template>

<script lang="ts" setup>

import {inject, onMounted, ref} from 'vue'
import {AxiosStatic} from "axios";
import DataTable, {DataTableFilterEvent, DataTablePageEvent} from 'primevue/datatable';
import InputText from 'primevue/inputtext'
import Column from 'primevue/column';
import moment from "moment";

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
  axios?.post("/datatable/timeline?sort=fromTime&page=" + pageIndex.value + "&pageSize=" + pageSize.value, filterData)
      .then((response) => {
        if (response.status === 200) {
          timelines.value = response.data.rows
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

  for (let a = 0; a < keys.length; a++) {
    const key = keys[a]
    const {value: _value, matchMode} = vueFilter[keys[a]]
    if (_value.length > 0) {
      if (key === 'projectName') {
        filter['projectEntity'] = {
          type: getFilterType(matchMode),
          value: '%' + _value + '%',
          childKey: 'name'
        }
      } else {
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


</script>

<style scoped>

</style>
