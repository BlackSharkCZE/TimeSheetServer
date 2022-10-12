<template>
  <h2>Create company form</h2>
</template>

<script lang="ts" setup>
import {AxiosStatic} from "axios";
import {inject, reactive, ref} from "vue";

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
}

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define component data
const formData = reactive<FormData>({
  ic: null,
  companyName: null,
  dic: null,
  platceDph: false,
  primary: false,
  okres: null,
  obec: null,
  castObce: null,
  ulice: null,
  cisloDomu: null,
  psc: null
})


const aresLoading = ref<boolean>(false)

// Define functions
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


<style scoped lang="scss">


</style>
