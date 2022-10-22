<template>
  <Panel header="Create Requisition">
    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

      <company-field
          id="company"
          label="Company"
          v-model="formData.company"
          :vualidate="v$.company"
          :submitted="submitted"></company-field>

      <div class="field">
        <FileUpload name="file"
                    ref="fileUpload"
                    :showUploadButton="false"
                    :showCancelButton="false"
                    :multiple="false"
                    url="/requisition/create"
                    @upload="uploadDone"
                    @select="fileSelected"
                    @before-upload="beforeUploadHandler"/>
      </div>


      <input-field id="note"
                   label="Note"
                   :vualidate="v$.note"
                   :submitted="submitted"
                   v-model="formData.note"/>

      <input-field id="orderNumber"
                   label="Order number"
                   :vualidate="v$.orderNumber"
                   :submitted="submitted"
                   v-model="formData.orderNumber"/>


      <div class="field">
        <label for="startDate">Start date <span v-if="v$.startDate.required">*</span></label>
        <calendar id="startDate"
                  dateFormat="yy-mm-dd"
                  :class="{'p-invalid':v$.startDate.$invalid && submitted}"
                  v-model="v$.startDate.$model"/>
        <small v-if="v$.startDate.$invalid && submitted"
               class="p-error">{{ v$.startDate.required.$message.replace('Value', 'Start date') }}</small>
      </div>

      <div class="field">
        <label for="endDate">End date <span v-if="v$.endDate.required">*</span></label>
        <calendar id="endDate"
                  dateFormat="yy-mm-dd"
                  :class="{'p-invalid':v$.endDate.$invalid && submitted}"
                  v-model="v$.endDate.$model"/>
        <small v-if="v$.endDate.$invalid && submitted"
               class="p-error">{{ v$.endDate.required.$message.replace('Value', 'End date') }}</small>
      </div>


      <Button type="submit" label="Pridat" class="mt-2"/>
    </form>

  </Panel>

  {{ formData }}

</template>

<script lang="ts" setup>
import Button from 'primevue/button'
import Panel from 'primevue/panel'
import Calendar from "primevue/calendar";
import FileUpload, {FileUploadBeforeUploadEvent, FileUploadSelectEvent} from "primevue/fileupload";

import {AxiosStatic} from "axios";
import {inject, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import useVuelidate from "@vuelidate/core";
import {required} from "@vuelidate/validators";
import InputField from "@/components/blocks/InputField.vue";
import moment from "moment";
import CompanyField from "@/components/blocks/CompanyField.vue";

// Define types
type FormData = {
  company: any | null,
  note: string | null,
  orderNumber: string | null,
  startDate: string | null,
  endDate: string | null

}

// Define component data
const formData = reactive<FormData>({
  company: null,
  note: null,
  orderNumber: null,
  startDate: null,
  endDate: null,

})

const formRef = ref(null)
const submitted = ref<boolean>(false)
let fileName: (string | null) = null

const rules = {
  company: {required},
  note: {required},
  orderNumber: {required},
  startDate: {required},
  endDate: {required},
}

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define used properties
const router = useRouter()
const v$ = useVuelidate(rules, formData)
const fileUpload = ref<FileUpload>()

// Define functions
function beforeUploadHandler(event: FileUploadBeforeUploadEvent) {
  event.formData.set("note", formData.note)
  event.formData.set("orderNumber", formData.orderNumber)
  event.formData.set("startDate", moment(formData.startDate).format("YYYY-MM-DD"))
  event.formData.set("endDate", moment(formData.endDate).format("YYYY-MM-DD"))
  event.formData.set("companyId", formData.company.id)

  if (fileName != null) {
    event.formData.set("fileName", fileName)
  }

}

function fileSelected(event: FileUploadSelectEvent) {
  if (event.files.length > 0) {
    fileName = event.files[0].name
  } else {
    fileName = null
  }
}

function uploadDone() {
  router.push({
    path: '/private/requisition/list'
  })
}

function handleSubmit(isFormValid: boolean) {
  submitted.value = true
  if (isFormValid) {
    fileUpload.value.upload()
    // saveRequisition()
  } else {
    console.error('Form is not valid!')
  }
}
</script>

<style scoped>

</style>
