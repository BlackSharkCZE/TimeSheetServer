<template>

  <Panel :header="label" v-if="company">
    <div class="card">
      <div class="card-container flex align-items-left justify-content-start">
        <div class="p-2 mr-3">
          <BsLabel :text="company.companyName" label="Company name"/>
        </div>
        <div class="p-2 mr-3">
          <BsLabel :text="company.ic" label="IČ"/>
        </div>
        <div class="p-2">
          <BsLabel :text="convertBoolean(company?.platceDph, 'Ano', 'Ne')" label="Vat payer"/>
        </div>
        <div class="p-2">
          <BsLabel :text="company.dic" label="DIČ"/>
        </div>
      </div>
      <div class="card-container flex align-items-left justify-content-start">
        <div class="p-2 mr-3">
          <BsLabel :text="company.bankAccountNumber" label="Bank acount number"/>
        </div>
      </div>
      <div class="card-container flex align-items-left justify-content-start">
        <div class="p-2 mr-3">
          <BsLabel :text="company.email" label="e-mail"/>
        </div>
        <div class="p-2 mr-3">
          <BsLabel :text="company.phoneNumber" label="Phone Number"/>
        </div>
      </div>
      <div class="card-container flex align-items-left justify-content-start">
        <div class="p-2 mr-3">
          <BsLabel :text="company.ulice + ' ' + company.cisloDomu + ' / ' + company.obec + ' - ' + company.castObce" label="Address"/>
        </div>
      </div>
    </div>
  </Panel>
</template>

<script lang="ts" setup>
import {defineProps} from 'vue'
import {Company} from '@/components/blocks/Types'
import Panel from 'primevue/panel'
import BsLabel from "@/components/blocks/BsLabel.vue";

// Define component properties
interface Properties {
  company: Company,
  label: string

}

const props = defineProps<Properties>()


// Define functions
function convertBoolean(value:boolean, t:string, f:string) {
  if (value) return t; else return f
}

</script>

<style scoped>

</style>
