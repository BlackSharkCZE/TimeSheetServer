<template>
  <Panel header="Create Project">
    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

      <company-field
          id="company"
          label="Company"
          v-model="formData.company"
          :vualidate="v$.company"
          :submitted="submitted"
      ></company-field>

      <input-field id="name"
                   label="Project name"
                   :vualidate="v$.name"
                   :submitted="submitted"
                   v-model="formData.name"/>

      <Button type="submit" label="Pridat" class="mt-2"/>
    </form>

    {{ formData }}
  </Panel>
</template>

<script lang="ts" setup>
import Button from 'primevue/button'
import Panel from 'primevue/panel'
import Calendar from "primevue/calendar";
import Dropdown from "primevue/dropdown";

import {AxiosStatic} from "axios";
import {inject, onMounted, reactive, ref} from "vue";
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


// Define functions
function handleSubmit(isFormValid: boolean) {
  submitted.value = true
  if (isFormValid) {
    saveRate()
  } else {
    console.error('Form is not valid!')
  }
}

function saveRate() {
  let path = "/rate/create"

  axios?.post(path, buildData()).then((response) => {
    if (response.status >= 200 && response.status <= 299) {
      router.push({
        path: '/private/rate/list'
      })
    } else {
      // message.error('Ulozeni spolecnosti se nezdarilo!')
      console.error(response)
    }
  })
}

function buildData(): any {
  return {
    rate: formData.amount,
    validSince: formData.since,
    companyId: formData.company.id
  }
}

</script>

<style scoped>

</style>
