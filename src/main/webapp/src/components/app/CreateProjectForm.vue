<template>
  <Panel header="Create Project" :toggleable="true" :collapsed="true" class="mt-2">
    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">
      <div class="card">
        <div class="formgrid grid">
          <company-field
              class="col-2"
              id="company"
              label="Company"
              v-model="formData.company"
              :vualidate="v$.company"
              :submitted="submitted">
          </company-field>
          <input-field id="name"
                       class="col-2"
                       label="Project name"
                       :vualidate="v$.name"
                       :submitted="submitted"
                       v-model="formData.name"/>

          <div class="field col-1">
            <label>&nbsp;</label>
            <Button type="submit" label="Create" class="p-button-lg"/>
          </div>
        </div>
      </div>
    </form>
  </Panel>
</template>

<script lang="ts" setup>
import Button from 'primevue/button'
import Panel from 'primevue/panel'

import {AxiosStatic} from "axios";
import {defineEmits, inject, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import useVuelidate from "@vuelidate/core";
import {required} from "@vuelidate/validators";
import InputField from "@/components/blocks/InputField.vue";
import CompanyField from "@/components/blocks/CompanyField.vue";

// Define types
type FormData = {
  company: any | null
  name: string | null,
}

// Define component data
const formData = reactive<FormData>({
  company: null,
  name: null
})

const formRef = ref(null)
const submitted = ref<boolean>(false)

const rules = {
  company: {required},
  name: {required}
}

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define used properties
const router = useRouter()
const v$ = useVuelidate(rules, formData)

// Define emits
const emits = defineEmits(['itemCreated'])

// Define functions
function handleSubmit(isFormValid: boolean) {
  submitted.value = true
  if (isFormValid) {
    saveProject()
  } else {
    console.error('Form is not valid!')
  }
}

function saveProject() {
  let path = "/project"

  axios?.post(path, buildData()).then((response) => {
    if (response.status >= 200 && response.status <= 299) {

      if (response.data.id > 0) {
        emits('itemCreated', response.data)
      } else {
        console.error("Can not save project. Invalid response", response)
      }


    } else {
      // message.error('Ulozeni spolecnosti se nezdarilo!')
      console.error(response)
    }
  })
}

function buildData(): any {
  return {
    name: formData.name,
    companyID: formData.company.id,
    companyName: formData.company.companyName
  }
}

</script>

<style scoped>

</style>
