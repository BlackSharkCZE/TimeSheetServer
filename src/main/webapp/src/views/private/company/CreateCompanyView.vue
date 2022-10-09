<template>

  <w-form class="pa2">
    <w-flex gap="1">
      <div class="xs4">
        <w-input
            v-model="formData.ic"
            class="mb4"
            label="IČ"
            shadow>
        </w-input>
      </div>
      <div class="xs1 pt4 text-left">
        <w-button @click="loadDataFromAres" :disabled="aresLoading">Load from ARES</w-button>
      </div>
    </w-flex>

    <w-flex gap="3">
      <div class="xs4">
        <w-input v-model="formData.companyName"
                 class="mb4"
                 label="Název společnosti"
                 shadow></w-input>
      </div>

      <div class="xs4">
        <w-input v-model="formData.dic"
                 class="mb4"
                 label="DIČ"
                 shadow></w-input>
      </div>
    </w-flex>

    <w-flex gap="3">
      <div class="xs2">
        <w-input v-model="formData.obec"
                 class="mb4"
                 label="Obec"
                 shadow></w-input>
      </div>
      <div class="xs2">
        <w-input v-model="formData.ulice"
                 class="mb4"
                 label="Ulice"
                 shadow></w-input>
      </div>
      <div class="xs2">
        <w-input v-model="formData.cisloDomu"
                 class="mb4"
                 label="Číslo domu"
                 shadow></w-input>
      </div>
      <div class="xs2">
        <w-input v-model="formData.castObce"
                 class="mb4"
                 label="Část obce"
                 shadow></w-input>
      </div>

      <div class="xs2">
        <w-input v-model="formData.okres"
                 class="mb4"
                 label="Okres"
                 shadow></w-input>
      </div>

    </w-flex>

  </w-form>

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
