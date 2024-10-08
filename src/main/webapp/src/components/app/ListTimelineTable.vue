<template>
  <ConfirmDialog></ConfirmDialog>
  <Message v-if="timelines.length==0" severity="info" :closable="false">There is not any timeline in database.</Message>
  <Message v-if="error.show" severity="error" :closable="true" @close="error.show=false">{{ error.message }}:
    {{ error.show }}
  </Message>
  <Message v-if="info.show" severity="info" :closable="true" @close="info.show=false">{{ info.message }}</Message>

  <TimelineRowEdit
      @update:row="handleRowUpdate"
      v-model:display="displayEdit" v-model:row="selectedRow"></TimelineRowEdit>

  <DataTable
      class="p-datatable-sm"
      :auto-layout="true"
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

    <Column header="#">
      <template #body="{data}">
        <i class="i-button pi cursor-pointer pi-pencil text-blue-500"
           @click="editItem(data)">
        </i>
      </template>
    </Column>

    <Column header="Day">
      <template #body="{data}">{{ getDay(data) }}</template>
    </Column>

    <Column header="Date">
      <template #body="{data}">{{ getDate(data.fromTime) }}</template>
    </Column>

    <Column header="From">
      <template #body="{data}">{{ getTime(data.fromTime) }}</template>
    </Column>

    <Column header="To">
      <template #body="{data}">{{ getTime(data.toTime) }}</template>
    </Column>

    <Column field="pause" header="Pause"></Column>
    <Column field="workTime" header="Worktime"></Column>

    <Column field="projectName" header="Project" filterMatchMode="startsWith" :showFilterMenu="false" ref="projectName">
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Project name"/>
      </template>
    </Column>

    <Column field="note" header="Note" :showFilterMenu="false">
      <template #filter="{filterModel,filterCallback}">
        <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                   class="p-column-filter p-inputtext-sm"
                   placeholder="Search by note"/>
      </template>
    </Column>

    <Column field="writers" header="Writers">
      <template #body="{data}">
        <writers-marker :settings="data.remoteWriteTimestamp || null" :writers="data.writers || null"
                        @click="writeRowToRemote(data)"></writers-marker>
      </template>
    </Column>

  </DataTable>
</template>

<script lang="ts" setup>

import {defineExpose, inject, onMounted, reactive, ref} from 'vue'
import {AxiosStatic} from "axios";
import DataTable, {DataTableFilterEvent, DataTablePageEvent} from 'primevue/datatable';
import InputText from 'primevue/inputtext'
import Message from 'primevue/message'
import Column from 'primevue/column';
import moment from "moment";
import WritersMarker from "@/components/blocks/WritersMarker.vue";
import ConfirmDialog from "primevue/confirmdialog";
import {ErrorType} from "@/components/blocks/Types";
import TimelineRowEdit from "@/components/blocks/TimelineRowEdit.vue";
import {TimelineType} from "@/components/blocks/TimelineDefs";

// Define types
type RestResponse = {
  success: boolean,
  message: string | null,
  data: any | null,
  code: number | null
}

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define properties
const timelines = ref<TimelineType[]>([])
const loading = ref<boolean>(true)
const dt = ref(null);
const pageIndex = ref<number>(0)
const pageSize = ref<number>(25)
const totalRecords = ref<number>(0)
const filters = ref({
  note: {value: '', matchMode: 'contains'},
  projectName: {value: '', matchMode: 'contains'}
})
const displayEdit = ref<boolean>(false)
const selectedRow = ref<any | null>(null)


const error = reactive<ErrorType>({show: false, message: null})
const info = reactive<ErrorType>({show: false, message: null})

// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define methods

function handleRowUpdate(row: TimelineType) {
  loadData()
}

function reloadTable() {
  loadData(mapFilterToDataTablePayload(filters.value))
}

function writeRowToRemote(data: any) {
  const id = data.id
  axios?.post("/timeline/remote-write/" + id, {}).then(response => {
    // TODO update parser according to real response from backend Map<String, RestResponse<RemoteWriteTimestampEntity?>?>
    if (response.status === 200) {
      const result = response.data
      const writers = [...Object.keys(result)]
      let success = true

      const im: string[] = []
      const em: string[] = []

      writers.forEach(item => {
        const sItem = result[item]
        if (sItem != undefined && sItem != null) {
          success = success && sItem.success
          if (sItem.success) {
            im.push("Write to " + item + " success")
          } else {
            em.push("Write to " + item + " failed with message: \" " + sItem.message + "\"")
          }
        } else {
          em.push("Response for writer " + item + " missing!")
        }
      })

      info.show = im.length > 0
      info.message = im.join(". ")

      error.show = em.length > 0
      error.message = em.join(". ")
      loadData()

    } else {
      error.show = true
      error.message = "Write to remote writers failed! " + response.data
    }
  })
}

function editItem(row: any) {
  selectedRow.value = row
  displayEdit.value = true
}

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
  if (keys === undefined || keys === null || keys.length === 0) {
    return filter
  }
  for (let a = 0; a < keys.length; a++) {
    const key = keys[a]
    const {value: _value, matchMode} = vueFilter[keys[a]]
    if (_value !== null && _value.length > 0) {
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

// Define exposes
defineExpose({reloadTable})


</script>

<style scoped>

</style>
