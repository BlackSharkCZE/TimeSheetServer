<template>

  <div class="mt-2">
    <div class="flex align-items-left justify-content-start gap-2">
      <div class="flex-grow-1">
        <company-card label="Issuer" :company="invoice?.issuerCompany"></company-card>
      </div>
      <div class="flex-grow-1">
        <company-card label="Recipient" :company="invoice?.recipientCompany"></company-card>
      </div>
    </div>

    <div v-if="invoice != null">
      <invoice-items
          @itemCreated="itemCreatedHandler"
          :items="items"
          :invoice-number="invoice.number"
          :invoice-id="parseInt(router.currentRoute.value.params.number)"></invoice-items>
    </div>

    <div class="mt-2 text-right">
        <span class="p-buttonset">
            <Button label="Back" icon="pi pi-backward" @click="router.back()"/>
            <Button label="Download" icon="pi pi-download" @click="downloadInvoice()"/>
        </span>
    </div>
  </div>

</template>

<script lang="ts" setup>
import {inject, onMounted, ref} from 'vue'
import {useRouter} from "vue-router";
import {AxiosStatic} from "axios";
import CompanyCard from "@/components/blocks/CompanyCard.vue";
import InvoiceItems from "@/components/blocks/InvoiceItems.vue";
import {InvoiceItem} from "@/components/blocks/Types";
import Button from "primevue/button";


// Define component properties
const router = useRouter()
const invoice = ref(null)
const items = ref<InvoiceItem[]>([])

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define lifecycle hooks
onMounted(() => {
  reloadData()
})

// Define methods
function itemCreatedHandler() {
  reloadData()
}

function reloadData() {
  const invoiceNumber = router.currentRoute.value.params.number
  const url = '/invoice/detail/' + invoiceNumber
  axios?.get(url).then(response => {
    if (response.status >= 200 && response.status <= 299) {
      invoice.value = response.data
      items.value = response.data.items
    } else {
      console.error('Can not load invoice with ID ', invoiceNumber)
    }
  })
}

function downloadInvoice() {
  axios?.get('/invoice/' + invoice.value?.id + '/pdf', {
    responseType: 'blob'
  }).then(response => {

    const objectURL = URL.createObjectURL(response.data)
    const link = document.createElement('a')
    link.href = objectURL
    link.setAttribute('download', invoice.value.number + '.pdf')
    document.body.appendChild(link)
    link.click()
    link.remove()
    window.URL.revokeObjectURL(link.href)

  })
}

</script>

<style scoped>

</style>
