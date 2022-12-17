<template>

  <Dialog header="Header"
          v-model:visible="visible"
          @update:visible="handleChangeVisibility"
          :modal="true" position="top"
          :breakpoints="{'960px': '75vw', '640px': '90vw'}"
          :style="{width: '50vw'}">

    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">
      <div class="card">
        <div class="formgrid grid">

          <div class="field col-12 md:col-6 sm:col-6 lg:col-2">
            <label for="date">Date <span v-if="v$.date.required">*</span></label>
            <Calendar id="date"
                      dateFormat="dd.mm.yy"
                      :class="{'p-invalid':v$.date.$invalid && submitted}"
                      v-model="v$.date.$model"/>
            <small v-if="v$.date.$invalid && submitted"
                   class="p-error">{{ v$.date.required.$message.replace('Value', 'Date') }}</small>
          </div>

          <div class="field col-12 md:col-6 sm:col-6 lg:col-2">
            <label for="fromTime">From Time <span v-if="v$.fromTime.required">*</span></label>
            <Calendar id="fromTime"
                      dateFormat="HH:MM"
                      :class="{'p-invalid':v$.fromTime.$invalid && submitted}"
                      :timeOnly="true"
                      hourFormat="24"
                      :stepMinute="15"
                      v-model="v$.fromTime.$model"/>
            <small v-if="v$.fromTime.$invalid && submitted"
                   class="p-error">{{ v$.fromTime.required.$message.replace('Value', 'Time from') }}</small>
          </div>

          <div class="field col-12 md:col-6 sm:col-6 lg:col-2">
            <label for="toTime">To Time <span v-if="v$.toTime.required">*</span></label>
            <Calendar id="toTime"
                      dateFormat="HH:MM"
                      :class="{'p-invalid':v$.toTime.$invalid && submitted}"
                      :timeOnly="true"
                      hourFormat="24"
                      :stepMinute="15"
                      v-model="v$.toTime.$model"/>
            <small v-if="v$.toTime.$invalid && submitted"
                   class="p-error">{{ v$.toTime.required.$message.replace('Value', 'Time to') }}</small>
          </div>

          <div class="field col-12 md:col-6 sm:col-6 lg:col-4">
            <label for="toTime">Pause <span v-if="v$.toTime.required">*</span></label>
            <InputNumber v-model="v$.pause.$model"
                         id="pause"
                         :min="0"
                         :step="15"
                         :allowEmpty="false"
                         showButtons
                         buttonLayout="horizontal"
                         decrementButtonClass="p-button-danger"
                         incrementButtonClass="p-button-success"
                         incrementButtonIcon="pi pi-plus"
                         decrementButtonIcon="pi pi-minus"
                         suffix=" min"
                         mode="decimal"/>
            <small v-if="v$.toTime.$invalid && submitted"
                   class="p-error">{{ v$.toTime.required.$message.replace('Value', 'Pause') }}</small>
          </div>

          <project-field
              class="col-12 md:col-6 sm:col-6 lg:col-4"
              id="project"
              label="Project"
              v-model="v$.project.$model"
              :vualidate="v$.project"
              :submitted="submitted">
          </project-field>

          <input-field id="note"
                       class="field col-12 md:col-10 sm:col-10 lg:col-8"
                       label="Note"
                       :vualidate="v$.note"
                       :submitted="submitted"
                       v-model="formData.note"/>
        </div>
      </div>
    </form>

    <template #footer>
      <Button label="Cancel" icon="pi pi-times" class="p-button-text" @click="hideDialog()"/>
      <Button label="Save" icon="pi pi-check" autofocus @click="handleSubmit(!v$.$invalid)"/>
    </template>
  </Dialog>

</template>

<script lang="ts" setup>

import Dialog from 'primevue/dialog'
import Button from 'primevue/button'
import Calendar from 'primevue/calendar'
import InputNumber from 'primevue/inputnumber'

import {defineEmits, defineProps, inject, onMounted, reactive, ref, watch} from 'vue'
import {FormDataType, TimelineRules, TimelineType} from "@/components/blocks/TimelineDefs";
import InputField from "@/components/blocks/InputField.vue";
import ProjectField from "@/components/blocks/ProjectField.vue";

import {AxiosStatic} from "axios";
import useVuelidate from "@vuelidate/core";
import {useDataStore} from "@/stores/DataStore";

import {saveTimeline, updateTimeline} from "@/components/blocks/TimelineUtils";

// Define types
type PropsType = {
  display: boolean,
  row: TimelineType | null
}

const formData = reactive<FormDataType>({
  id: null,
  project: null,
  date: new Date(),
  fromTime: null,
  toTime: null,
  note: null,
  pause: 30
})

// Define stores
const dataStore = useDataStore()

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define props
const props = defineProps<PropsType>()

// Define emits
const emits = defineEmits(['update:display', 'update:row', 'updated'])

// Define lifecycle
onMounted(() => {
  visible.value = props.display
})

// Define refs
const visible = ref<boolean>(false)
const v$ = useVuelidate(TimelineRules, formData)
const submitted = ref<boolean>(false)
const errorMessage = ref<string | null>(null)

// Define watch
watch(() => props.display, (cur, prev) => {
  visible.value = cur
})
watch(() => props.row, (cur, prev) => {
  if (cur != null) {
    formData.project = dataStore.getProjects.filter(it => it.id == cur.projectId)[0]
    formData.note = cur.note
    formData.pause = cur.pause
    formData.fromTime = new Date(cur.fromTime)
    formData.date = new Date(cur.fromTime)
    formData.toTime = new Date(cur.toTime)
    formData.id = cur.id
  }
})

// Define functions
function hideDialog() {
  emits('update:row', null)
  emits('update:display', false)
}

function handleChangeVisibility(current: boolean) {
  emits('update:display', current)
}

function handleSubmit(isFormValid: boolean) {
  submitted.value = true
  if (isFormValid) {
    updateTimeline(axios, formData, (data) => {
      submitted.value = false
      emits('update:row', data.data)
      emits('update:display', false)
    }, (response) => {
      console.error('Can not update timeline', response)
    })
  } else {
    console.error('Form is not valid!')
  }
}

</script>

<style scoped>

</style>
