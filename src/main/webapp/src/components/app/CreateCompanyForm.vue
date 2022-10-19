<template>
  <Panel header="Create company">
    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

      <div class="field">
        <label for="ic">IC</label>
        <div class="p-inputgroup" id="ic">
          <InputText
              :class="{'p-invalid':v$.ic.$invalid}"
              class="p-inputtext-sm" v-model="v$.ic.$model"/>
          <Button icon="pi pi-search" class="p-button-warning" @click="loadDataFromAres"/>
        </div>
        <small v-if="v$.ic.$invalid && submitted"
               class="p-error">{{ v$.ic.required.$message.replace('Value', 'IC') }}</small>
      </div>
      <input-field id="companyName"
                   label="Company Name 2"
                   :vualidate="v$.companyName"
                   :submitted="submitted"
                   v-model="formData.companyName">
      </input-field>

      <input-field id="dic"
                   label="DIC"
                   :vualidate="v$.dic"
                   :submitted="submitted"
                   v-model="formData.dic"/>

      <input-field id="okres"
                   label="Okres"
                   :vualidate="v$.okres"
                   :submitted="submitted"
                   v-model="formData.okres"/>

      <input-field id="obec"
                   label="Obec"
                   :vualidate="v$.obec"
                   :submitted="submitted"
                   v-model="formData.obec"/>

      <input-field id="castObce"
                   label="Cast obce"
                   :vualidate="v$.castObce"
                   :submitted="submitted"
                   v-model="formData.castObce"/>
      <input-field id="ulice"
                   label="Ulice"
                   :vualidate="v$.ulice"
                   :submitted="submitted"
                   v-model="formData.ulice"/>

      <input-field id="cisloDomu"
                   label="Cislo Domu"
                   :vualidate="v$.cisloDomu"
                   :submitted="submitted"
                   v-model="formData.cisloDomu"/>

      <input-field id="email"
                   label="E-mail"
                   :vualidate="v$.email"
                   :submitted="submitted"
                   v-model="formData.email"/>

      <input-field id="phone"
                   label="Phone"
                   :vualidate="v$.phone"
                   :submitted="submitted"
                   v-model="formData.phone"/>

      <input-field id="bankAccountNumber"
                   label="Cislo uctu"
                   :vualidate="v$.bankAccountNumber"
                   :submitted="submitted"
                   v-model="formData.bankAccountNumber"/>

      <div class="field">
        Platce DPH:
        <InputSwitch v-model="formData.platceDph"/>
      </div>

      <Button type="submit" label="Submit" class="mt-2"/>
    </form>

  </Panel>

</template>

<script lang="ts" setup>

import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import Panel from 'primevue/panel'
import InputSwitch from 'primevue/inputswitch'

import {AxiosStatic} from "axios";
import {inject, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import useVuelidate from "@vuelidate/core";
import {email, required} from "@vuelidate/validators";
import InputField from "@/components/blocks/InputField.vue";
import {useUserStore} from "@/stores/UserStore";

// Define types
type FormData = {
  ic: string | null
  companyName: string | null,
  dic: string | null,
  platceDph: boolean,
  okres: string | null,
  obec: string | null,
  castObce: string | null,
  ulice: string | null,
  cisloDomu: string | null,
  psc: string | null,
  email: string | null,
  phone: string | null,
  bankAccountNumber: string | null
}

// Define component data
const formData = reactive<FormData>({
  ic: '71857001',
  companyName: null,
  dic: null,
  platceDph: false,
  okres: null,
  obec: null,
  castObce: null,
  ulice: null,
  cisloDomu: null,
  psc: null,
  email: null,
  phone: null,
  bankAccountNumber: null
})

const aresLoading = ref<boolean>(false)
const formRef = ref(null)
const submitted = ref<boolean>(false)

const rules = {
  // ic: {required: helpers.withMessage('IC je povinny parametr.', required), minLength: minLength(2)}
  ic: {required},
  companyName: {required, $autoDirty: true},
  dic: {required},
  okres: {required},
  obec: {required},
  castObce: {required},
  ulice: {required},
  cisloDomu: {required},
  psc: {required},
  email: {required, email},
  phone: {required},
  bankAccountNumber: {required}
}

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define used properties
const router = useRouter()
const v$ = useVuelidate(rules, formData)
const userStore = useUserStore()


// Define functions
function handleSubmit(isFormValid: boolean) {
  submitted.value = true
  if (isFormValid) {
    saveCompany()
  }
}

function saveCompany() {
  const primary = !userStore.hasPrimaryCompany
  let path = "/company"
  if (primary) {
    path = "/company/primary"
  }
  axios?.post(path, buildData()).then((response) => {
    if (response.status >= 200 && response.status <= 299) {
      if (primary)
        userStore.setCompany(response.data)
      router.push({
        path: '/private/companies/list'
      })
    } else {
      // message.error('Ulozeni spolecnosti se nezdarilo!')
      console.error(response)
    }
  })
}

function buildData(): any {

  const primary = !userStore.hasPrimaryCompany

  return {
    ic: formData.ic,
    companyName: formData.companyName,
    dic: formData.dic,
    okres: formData.okres,
    obec: formData.obec,
    castObce: formData.castObce,
    ulice: formData.ulice,
    cisloDomu: formData.cisloDomu,
    ps: formData.psc,
    email: formData.email,
    phoneNumber: formData.phone,
    bankAccountNumber: formData.bankAccountNumber,
    platceDph: formData.platceDph,
    primaryAccount: primary
  }
}

function loadDataFromAres() {
  aresLoading.value = true
  axios?.get(`/ares/` + formData.ic).then(response => {
    aresLoading.value = false
    if (response.status === 200) {
      formData.ic = response.data.ic
      formData.companyName = response.data.companyName
      formData.dic = response.data.dic
      formData.platceDph = response.data.platceDph
      formData.okres = response.data.okres
      formData.obec = response.data.obec
      formData.castObce = response.data.castObce
      formData.ulice = response.data.ulice
      formData.cisloDomu = response.data.cisloDomu
      formData.psc = response.data.psc
    } else {
      console.error('Can not load ARES info for the IC', response)
    }
  })
}
</script>

<style scoped>

</style>
