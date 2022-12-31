<template>

  <Panel header="Import odchozích plateb" class="mt-2 mb-2">
    <div class="card">
      <div class="formgrid grid">

        <div class="field col-12">
          <FileUpload name="file"
                      ref="fileUpload"
                      :showUploadButton="true"
                      :showCancelButton="true"
                      :multiple="true"
                      url="/payment/import/outgoing-payments"
                      @upload="uploadDone"
                      @select="fileSelected"
                      @before-send="beforeSendHandler"
                      @before-upload="beforeUploadHandler"/>
        </div>
      </div>
    </div>
  </Panel>
</template>

<script lang="ts" setup>
import Panel from 'primevue/panel'

import {AxiosStatic} from "axios";
import {defineEmits, inject, ref} from "vue";
import {useRouter} from "vue-router";

import FileUpload, {
  FileUploadBeforeSendEvent,
  FileUploadBeforeUploadEvent,
  FileUploadSelectEvent,
  FileUploadUploadEvent
} from "primevue/fileupload";
import Keycloak from "keycloak-js";
import {useUserStore} from "@/stores/UserStore";

// Define component refs
const fileUpload = ref<FileUpload>()

// Define injects
const keycloak = inject<Keycloak>("keycloak")

// Define private properties
let fileName: (string | null) = null

// Define emits
const emits = defineEmits(['newPayments'])


// Define functions
function fileSelected(event: FileUploadSelectEvent) {
  if (event.files.length > 0) {
    fileName = event.files[0].name
  } else {
    fileName = null
  }
}

function uploadDone(response: FileUploadUploadEvent) {
  if (response.xhr.status == 204) {
    console.log('Import OK')
    emits('newPayments', {})
    fileUpload.value.clear()

  } else {
    console.error('Import failed!')
  }
}

function beforeSendHandler(event: FileUploadBeforeSendEvent) {
  event.xhr.setRequestHeader('Authorization', 'Bearer ' + keycloak?.token)
}

function beforeUploadHandler(event: FileUploadBeforeUploadEvent) {
  if (fileName != null) {
    event.formData.set("fileName", fileName)
  }
}

</script>
