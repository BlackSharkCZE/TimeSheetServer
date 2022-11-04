<template>

  <Panel class="mt-2 mb-2">

    <Message v-if="errorMessage!=null" severity="error" :closable="false">{{ errorMessage }}</Message>

    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

      <div class="flex gap-2 flex-row flex-wrap card-container blue-container">
        <div class="align-items-center justify-content-center">
          <gem-project-field
              :vualidate="v$"
              :submitted="submitted"
              v-model:rootProject="v$.rootProject.$model"
              v-model:project="v$.project.$model"
              v-model:tag="v$.tag.$model"></gem-project-field>
        </div>

        <div class="align-items-center justify-content-center">
          <input-field id="pattern"
                       label="Pattern"
                       :vualidate="v$.pattern"
                       :submitted="submitted"
                       v-model="formData.pattern">
          </input-field>
        </div>

        <div class="align-items-center justify-content-center">
          <label for="writers">Writers <span v-if="v$.writers.required">*</span></label>
          <AutoComplete
              id="writers"
              v-model="v$.writers.$model" :multiple="true"
              @complete="filterWriter($event)"
              :class="{'p-invalid':v$.writers.$invalid && submitted}"
              :dropdown="true"
              :suggestions="filteredTypes"></AutoComplete>
          <small v-if="v$.writers.$invalid && submitted"
                 class="p-error">{{ v$.writers.required.$message.replace('Value', 'Writers') }}</small>
        </div>

        <div class="align-items-center justify-content-center">
          <input-field id="description"
                       label="Description"
                       :vualidate="v$.description"
                       :submitted="submitted"
                       v-model="v$.description.$model">
          </input-field>
        </div>

        <div class="align-items-center justify-content-center">
          <label>&nbsp;</label>
          <Button type="submit" label="Add" class="p-button-lg p-button-success"/>
        </div>
      </div>

    </form>
  </Panel>
</template>

<script lang="ts" setup>
import Button from 'primevue/button'
import Message from 'primevue/message'
import Panel from 'primevue/panel'
import AutoComplete, {AutoCompleteCompleteEvent} from 'primevue/autocomplete'

import {AxiosStatic} from "axios";
import {defineEmits, inject, onMounted, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import useVuelidate from "@vuelidate/core";
import {required} from "@vuelidate/validators";
import GemProjectField from "@/components/blocks/GemProjectField.vue";
import InputField from "@/components/blocks/InputField.vue";

import {GemProject} from "@/components/blocks/Types";

// Define types
type FormData = {
  rootProject: GemProject | null,
  project: GemProject | null,
  tag: GemProject | null,
  pattern: string | null,
  description: string | null,
  writers: Array<string> | null
}

// Define component data
const formData = reactive<FormData>({
  rootProject: null,
  project: null,
  tag: null,
  pattern: null,
  description: null,
  writers: []
})

const formRef = ref(null)
const submitted = ref<boolean>(false)
const fromTimeSent = ref<Date>(new Date())
const errorMessage = ref<string | null>(null)
const writerTypes = ref()
const filteredTypes = ref()


const rules = {
  rootProject: {required},
  project: {required},
  tag: {},
  pattern: {required},
  description: {required},
  writers: {required, minLength: 1}
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
  axios?.get("/remote-writer/types")
      .then(response => {
        if (response.status >= 200 && response.status <= 299) {
          writerTypes.value = response.data
        } else {
          console.error('Can not load remote writers types')
        }
      })
})

// Define functions
function filterWriter(event: AutoCompleteCompleteEvent) {
  if (!event.query.trim().length) {
    filteredTypes.value = [...writerTypes.value]
  } else {
    filteredTypes.value = writerTypes.value.filter((writer: string) => {
      return writer.toLowerCase().startsWith(event.query.toLowerCase());
    });
  }
}

function handleSubmit(isFormValid: boolean) {
  submitted.value = true
  v$.value.$validate().then(result => {
    console.log('Validation result : ', v$.value.$errors)

  })
  if (isFormValid) {
    saveSettings()
  } else {
    console.error('Form is not valid!', v$)
  }
}

function saveSettings() {
  let path = "/remote-writer"
  errorMessage.value = null
  axios?.post(path, buildData()).then((response) => {
    if (response.status >= 200 && response.status <= 299) {
      if (response.data.id > 0) {
        emits('itemCreated', response.data)
        submitted.value = false
        clearFormData()
      } else {

        if (response.data.success == false) {
          // this is custom error response with any business error
          errorMessage.value = response.data.list[0].message
        }
      }
    } else {
      console.error(response)
    }
  })
}

function clearFormData() {
  console.log('clear form data')
}

function buildData(): any {
  const x = {
    projectRootId: formData.rootProject?.key,
    projectId: formData.project?.key,
    keyPattern: formData.pattern,
    remoteWriterList: formData.writers?.join(";"),
    tagId: formData.tag?.key
  }
  return x
}

</script>

<style scoped>

</style>
