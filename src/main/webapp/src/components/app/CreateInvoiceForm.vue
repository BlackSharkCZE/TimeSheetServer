<template>

  <Panel header="Add invoice" class="mt-2 mb-2">
    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

      <div class="card">
        <div class="formgrid grid">
          <company-field id="issuer"
                         class="col-2"
                         v-model="v$.issuer.$model"
                         :vualidate="v$.issuer"
                         :submitted="submitted"
                         label="Issuer"></company-field>

          <company-field id="recipient"
                         class="col-2"
                         v-model="v$.recipient.$model"
                         :vualidate="v$.recipient"
                         :submitted="submitted"
                         label="Recipient"></company-field>

          <input-field id="invoiceNumber"
                       class="field col-2"
                       label="Invoice number"
                       :vualidate="v$.invoiceNumber"
                       :submitted="submitted"
                       v-model="v$.invoiceNumber.$model"/>

          <div class="field col-2">
            <label for="since">Issue date <span v-if="v$.issueDate.required">*</span></label>
            <calendar id="since"
                      :class="{'p-invalid':v$.issueDate.$invalid && submitted}"
                      v-model="v$.issueDate.$model"/>
            <small v-if="v$.issueDate.$invalid && submitted"
                   class="p-error">{{ v$.issueDate.required.$message.replace('Value', 'Issue date') }}</small>
          </div>

          <div class="field col-2">
            <label for="since">Vat payment date <span v-if="v$.vatPaymentDate.required">*</span></label>
            <calendar id="since"
                      :class="{'p-invalid':v$.vatPaymentDate.$invalid && submitted}"
                      v-model="v$.vatPaymentDate.$model"/>
            <small v-if="v$.vatPaymentDate.$invalid && submitted"
                   class="p-error">{{
                v$.vatPaymentDate.required.$message.replace('Value', 'Vat payment date')
              }}</small>
          </div>

          <div class="field col-2">
            <label for="since">Payment date <span v-if="v$.paymentDate.required">*</span></label>
            <calendar id="since"
                      :class="{'p-invalid':v$.paymentDate.$invalid && submitted}"
                      v-model="v$.paymentDate.$model"/>
            <small v-if="v$.paymentDate.$invalid && submitted"
                   class="p-error">{{ v$.paymentDate.required.$message.replace('Value', 'Vat payment date') }}</small>
          </div>


          <div class="field col-2">
            <FileUpload name="file"
                        ref="fileUpload"
                        mode="basic"
                        class="p-button-lg"
                        :showUploadButton="false"
                        :showCancelButton="false"
                        :multiple="false"
                        url="/invoice/upload2"
                        @upload="uploadDone"
                        @select="fileSelected"
                        @before-send="beforeSendHandler"
                        @before-upload="beforeUploadHandler"/>
          </div>

          <div class="field col-1">
            <label>&nbsp;</label>
            <Button type="submit" label="Add"/>
          </div>
        </div>
      </div>
    </form>
  </Panel>
</template>

<script lang="ts" setup>
import Button from 'primevue/button'
import Panel from 'primevue/panel'
import Calendar from "primevue/calendar";

import {AxiosStatic} from "axios";
import {inject, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import useVuelidate from "@vuelidate/core";
import {required} from "@vuelidate/validators";
import InputField from "@/components/blocks/InputField.vue";
import CompanyField from "@/components/blocks/CompanyField.vue";
import {Company} from "@/components/blocks/Types";
import FileUpload, {
  FileUploadBeforeSendEvent,
  FileUploadBeforeUploadEvent,
  FileUploadSelectEvent,
  FileUploadUploadEvent
} from "primevue/fileupload";
import moment from "moment/moment";
import Keycloak from "keycloak-js";

// Define types
type FormData = {
  issuer: Company | null
  recipient: Company | null
  invoiceNumber: string | null,
  issueDate: Date | null
  paymentDate: Date | null
  vatPaymentDate: Date | null
}

// Define component data
const formData = reactive<FormData>({
  issuer: null,
  recipient: null,
  invoiceNumber: null,
  issueDate: null,
  paymentDate: null,
  vatPaymentDate: null
})

const submitted = ref<boolean>(false)

const rules = {
  issuer: {required},
  recipient: {required},
  invoiceNumber: {required},
  issueDate: {required},
  paymentDate: {required},
  vatPaymentDate: {required}
}

// Inject dependencies
const axios = inject<AxiosStatic>('axios')
const keycloak = inject<Keycloak>("keycloak")

//npm Define used properties
const router = useRouter()
const v$ = useVuelidate(rules, formData)
const fileUpload = ref<FileUpload>()

let fileName: (string | null) = null

// Define functions
function fileSelected(event: FileUploadSelectEvent) {
  if (event.files.length > 0) {
    fileName = event.files[0].name
  } else {
    fileName = null
  }
}

function uploadDone(response: FileUploadUploadEvent) {
  const result = JSON.parse(response.xhr.response)
  console.log('New invoice ID: ', result.data.id)
  if (result.success) {
    router.push({
      name: "invoiceDetail",
      params: {
        number: result.data.id
      }
    })
  } else {
    console.error('Upload invoice failed!', response.message)
  }
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

function beforeSendHandler(event: FileUploadBeforeSendEvent) {
  event.xhr.setRequestHeader('Authorization', 'Bearer ' + keycloak?.token)
}

function beforeUploadHandler(event: FileUploadBeforeUploadEvent) {
  // event.xhr.open()
  event.formData.set("issuerID", (formData?.issuer?.id?.toString() as string))
  event.formData.set("recipientID", (formData?.recipient?.id?.toString() as string))
  event.formData.set("invoiceNumber", formData.invoiceNumber as string)
  event.formData.set("issueDate", moment(formData.issueDate).format("YYYY-MM-DD"))
  event.formData.set("paymentDate", moment(formData.paymentDate).format("YYYY-MM-DD"))
  event.formData.set("vatPaymentDate", moment(formData.vatPaymentDate).format("YYYY-MM-DD"))

  if (fileName != null) {
    event.formData.set("fileName", fileName)
  }

}

</script>

<style scoped>

</style>
