<template>
  <Panel header="Create Rate">
    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

      <div class="field">
        <label for="company">Company <span v-if="v$.company.required">*</span></label>
        <Dropdown id="company"
                  :class="{'p-invalid':v$.$invalid && submitted}"
                  v-model="v$.company.$model" :options="companies" optionLabel="name" :filter="true"
                  placeholder="Select a company" :showClear="true">
          <template #value="slotProps">
            <div v-if="slotProps.value">
              <div>{{ slotProps.value.companyName }}</div>
            </div>
            <span v-else>
                    {{ slotProps.placeholder }}
                </span>
          </template>
          <template #option="slotProps">
            <div>
              <div>{{ slotProps.option.companyName }} ({{ slotProps.option.email }})</div>
            </div>
          </template>
        </Dropdown>
        <small v-if="v$.$invalid && submitted"
               class="p-error">{{ v$.company.required.$message.replace('Value', 'Company') }}</small>
      </div>

      <div class="field">
        <label for="since">Plate od <span v-if="v$.since.required">*</span></label>
        <calendar id="since"
                  :class="{'p-invalid':v$.since.$invalid && submitted}"
                  v-model="formData.since"/>
        <small v-if="v$.$invalid && submitted"
               class="p-error">{{ v$.since.required.$message.replace('Value', 'Platne od') }}</small>
      </div>

      <input-field id="amount"
                   label="Castka za MD"
                   :vualidate="v$.amount"
                   :submitted="submitted"
                   v-model="formData.amount"/>

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

// Define types
type FormData = {
  company: any | null
  since: string | null,
  amount: number | null
}

// Define component data
const formData = reactive<FormData>({
  company: null,
  since: null,
  amount: null
})

const formRef = ref(null)
const submitted = ref<boolean>(false)

const rules = {
  company: {required},
  since: {required},
  amount: {required}
}

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define used properties
const router = useRouter()
const v$ = useVuelidate(rules, formData)
const companies = ref([])

// Define lifecycle hooks
onMounted(() => {
  loadCompanies()
})


// Define functions
function handleSubmit(isFormValid: boolean) {
  submitted.value = true
  if (isFormValid) {
    saveRate()
  } else {
    console.error('Form is not valid!')
  }
}

function loadCompanies() {
  axios?.get("/company/all?primary=true").then((response) => {
    if (response.status >= 200 && response.status <= 299) {
      companies.value = response.data
    } else {
      // message.error('Ulozeni spolecnosti se nezdarilo!')
      console.error(response)
    }
  })
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
