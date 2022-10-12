<template>
  <h1>create company form</h1>
</template>

<script lang="ts" setup>
import {AxiosStatic} from "axios";
import {inject, reactive, ref} from "vue";
import {useRouter} from "vue-router";

// Define types
type FormData = {
  ic: string | null
  companyName: string | null,
  dic: string | null,
  platceDph: boolean,
  primary: boolean,
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

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define used properties
const message = useMessage()
const router = useRouter()

// Define component data
const formData = reactive<FormData>({
  ic: '71857001',
  companyName: null,
  dic: null,
  platceDph: false,
  primary: false,
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
const formRef = ref<FormInst | null>(null)

const rules = {
  ic: {
    required: true,
    message: 'IC je povinny atribut.',
    trigger: 'blur'
  },
  companyName: {
    required: true,
    message: 'Jmeno spolecnosti je povinny atribut.',
    trigger: 'blur'
  },
  dic: {
    required: false
  },
  platceDph: {
    required: false
  },
  primary: {
    required: false
  },
  okres: {
    required: true,
    message: 'Okres je povinny atribut',
    trigger: 'blur'
  },
  obec: {
    required: true,
    message: 'Obec je povinny atribut.',
    trigger: 'blur'
  },
  castObce: {
    required: true,
    message: 'Cast obce je povinny atribut.',
    trigger: 'blur'
  },
  ulice: {
    required: true,
    message: 'Ulice je povinny atribut.',
    trigger: 'blur'
  },
  cisloDomu: {
    required: true,
    message: 'Cislo domu je povinny atribut.',
    trigger: 'blur'
  },
  psc: {
    required: true,
    message: 'PSC je povinny atribut.',
    trigger: 'blur'
  },
  email: {
    required: true,
    trigger: 'blur',
    type: 'email'
  },
  phone: {
    required: false
  },
  bankAccountNumber: {
    required: false
  }
}

// Define functions
function validateAndSend(e: MouseEvent) {
  e.preventDefault()
  formRef.value?.validate((errors) => {
    if (!errors) {
      saveCompany()
    } else {
      message.error('Formulář obsahuje chyby')
    }
  })
}

function saveCompany() {
  axios?.post('/company', buildData()).then((response) => {
    if (response.status >= 200 && response.status <= 299) {
      //TODO reset form
      router.push({
        path: '/private/companies/list'
      })
    } else {
      message.error('Ulozeni spolecnosti se nezdarilo!')
      console.error(response)
    }
  })
}

function buildData(): any {
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
    primaryAccount: formData.primary
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
      formData.primary = response.data.primary
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
