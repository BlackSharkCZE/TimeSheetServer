<template>

  <Panel class="mt-2 mb-2">

    <Message v-if="errorMessage!=null" severity="error" :closable="false">{{ errorMessage }}</Message>

    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">
      <div class="card">
        <div class="formgrid grid">
          <div class="field col-1">
            <label for="date">Date <span v-if="v$.date.required">*</span></label>
            <Calendar id="date"
                      dateFormat="dd.mm.yy"
                      :class="{'p-invalid':v$.date.$invalid && submitted}"
                      v-model="v$.date.$model"/>
            <small v-if="v$.date.$invalid && submitted"
                   class="p-error">{{ v$.date.required.$message.replace('Value', 'Date') }}</small>
          </div>

          <div class="field col-1">
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

          <div class="field col-1">
            <label for="toTime">From Time <span v-if="v$.toTime.required">*</span></label>
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

          <div class="field col-1">
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
              class="col-2"
              id="project"
              label="Project"
              v-model="formData.project"
              :vualidate="v$.project"
              :submitted="submitted">
          </project-field>

          <input-field id="note"
                       label="Note"
                       :vualidate="v$.note"
                       :submitted="submitted"
                       v-model="formData.note"/>

          <div class="field col-1">
            <label>&nbsp;</label>
            <Button type="submit" label="Pridat" class="p-button-lg"/>
          </div>

        </div>
      </div>
    </form>
  </Panel>
</template>

<script lang="ts" setup>
import Button from 'primevue/button'
import Calendar from 'primevue/calendar'
import InputNumber from 'primevue/inputnumber'
import Message from 'primevue/message'
import Panel from 'primevue/panel'

import {AxiosStatic} from "axios";
import {inject, onMounted, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import useVuelidate from "@vuelidate/core";
import {minValue, required} from "@vuelidate/validators";
import InputField from "@/components/blocks/InputField.vue";
import ProjectField from "@/components/blocks/ProjectField.vue";

// Define types
type FormData = {
  project: any | null,
  date: Date | null,
  fromTime: Date | null,
  toTime: Date | null,
  note: string | null,
  pause: number
}

// Define component data
const formData = reactive<FormData>({
  project: null,
  date: new Date(),
  fromTime: null,
  toTime: null,
  note: null,
  pause: 30
})

const formRef = ref(null)
const submitted = ref<boolean>(false)
const fromTimeSent = ref<Date>(new Date())
const errorMessage = ref<string | null>(null)

const rules = {
  project: {required},
  note: {required},
  date: {required},
  fromTime: {required},
  toTime: {required},
  pause: {required, minValue: minValue(0)},
}

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define used properties
const router = useRouter()
const v$ = useVuelidate(rules, formData)

// Define emits
const emits = defineEmits(['itemCreated'])

// Define lifecycle hooks
onMounted(() => {
  const fromTime = new Date()
  fromTime.setHours(6, 30)
  formData.fromTime = fromTime

  const toTime = new Date()
  toTime.setHours(15, 30)
  formData.toTime = toTime
})

// Define functions
function handleSubmit(isFormValid: boolean) {
  submitted.value = true
  if (isFormValid) {
    saveTimeline()
  } else {
    console.error('Form is not valid!')
  }
}

function saveTimeline() {
  let path = "/timeline"
  errorMessage.value = null
  axios?.post(path, buildData()).then((response) => {
    if (response.status >= 200 && response.status <= 299) {

      console.log('Response data: ', response.data)

      if (response.data.itemID > 0) {
        emits('itemCreated', response.data)
        submitted.value = false
        clearFormData()


      } else {

        if (response.data.success == false) {
          // this is custom error response with any business error
          errorMessage.value = response.data.list[0].message
        }

        console.error("Can not save timeline. Invalid response", response)
      }
    } else {
      // message.error('Ulozeni spolecnosti se nezdarilo!')
      console.error(response)
    }
  })
}

function clearFormData() {
  formData.fromTime = formData.toTime
  if (formData.pause > 0) {
    formData.pause = 0
  }
  formData.toTime = null
  formData.note = null
}

function buildData(): any {

  const fromTime: Date = new Date(formData.date!!)
  fromTime.setHours(formData.fromTime!!.getHours(), formData.fromTime!!.getMinutes())
  const toTime: Date = new Date(formData.date!!)
  toTime.setHours(formData.toTime!!.getHours(), formData.toTime!!.getMinutes())

  const x = {
    projectId: formData.project.id,
    fromTime: fromTime.toISOString(),
    toTime: toTime.toISOString(),
    pause: formData.pause,
    workTime: 0,
    note: formData.note
  }
  return x
}

</script>

<style scoped>

</style>
