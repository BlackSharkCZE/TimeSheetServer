<template>
  <Panel header="Create Rate" :toggleable="true" :collapsed="true" class="mt-2 mb-2">
    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

      <div class="card">
        <div class="formgrid grid">
          <company-field id="company"
                         class="col-2"
                         v-model="formData.company"
                         :vualidate="v$.company"
                         :submitted="submitted"
                         label="Company"></company-field>

          <div class="field col-2">
            <label for="since">Plate od <span v-if="v$.since.required">*</span></label>
            <calendar id="since"
                      :class="{'p-invalid':v$.since.$invalid && submitted}"
                      v-model="formData.since"/>
            <small v-if="v$.$invalid && submitted"
                   class="p-error">{{ v$.since.required.$message.replace('Value', 'Platne od') }}</small>
          </div>

          <input-field id="amount"
                       class="col-2"
                       label="Castka za MD"
                       :vualidate="v$.amount"
                       :submitted="submitted"
                       v-model="formData.amount"/>

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
import Panel from 'primevue/panel'
import Calendar from "primevue/calendar";

import {AxiosStatic} from "axios";
import {inject, reactive, ref, defineEmits} from "vue";
import {useRouter} from "vue-router";
import useVuelidate from "@vuelidate/core";
import {required} from "@vuelidate/validators";
import InputField from "@/components/blocks/InputField.vue";
import CompanyField from "@/components/blocks/CompanyField.vue";

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

// Define emits
const emits = defineEmits(['itemCreated'])

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
  axios?.post("/rate/create", buildData()).then((response) => {
    if (response.status >= 200 && response.status <= 299) {
      emits('itemCreated', response.data)
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
