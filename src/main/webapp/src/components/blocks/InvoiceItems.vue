<template>
  <ConfirmDialog></ConfirmDialog>
  <Panel header="Add invoice item"
         class="mt-2"
         :collapsed="items.length>0"
         :toggleable="true">

    <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

      <div class="card">
        <div class="formgrid grid">
          <input-field id="orderNumber"
                       class="field col-2"
                       label="Order number"
                       :vualidate="v$.orderNumber"
                       :submitted="submitted"
                       v-model="v$.orderNumber.$model"/>

          <input-field id="note"
                       class="field col-4"
                       label="Note"
                       :vualidate="v$.note"
                       :submitted="submitted"
                       v-model="v$.note.$model"/>

          <input-field-num id="amount"
                           class="field col-1"
                           label="amount"
                           :vualidate="v$.amount"
                           :submitted="submitted"
                           v-model="v$.amount.$model"/>

          <input-field-num id="vatrate"
                           suffix="%"
                           class="field col-1"
                           label="Vat rate"
                           :vualidate="v$.vatRate"
                           :submitted="submitted"
                           v-model="v$.vatRate.$model"/>

          <div class="field col-1">
            <label>&nbsp;</label>
            <Button type="submit" label="Pridat"/>
          </div>
        </div>
      </div>
    </form>
  </Panel>

  <Card class="mt-2" v-if="items && items.length>0">
    <template #content>
      <div class="grid font-bold">
        <div class="col-1">#</div>
        <div class="col-1">no.</div>
        <div class="col-2">ORDER NUMBER</div>
        <div class="col-5">NOTE</div>
        <div class="col-1 text-right">PRICE</div>
        <div class="col-2 text-right">PRICE + VAT</div>
      </div>
      <div v-for="(i, index) in props.items" v-bind:key="i.id" class="grid border-top-1">
        <div class="col-1">
          <i class="i-button pi text-red-900 cursor-pointer"
             :class="props.icon || 'pi-trash'"
             @click="deleteItem(i, index)">
          </i>
        </div>
        <div class="col-1">{{ index + 1 }}</div>
        <div class="col-2">{{ i.requisition?.customerNumber || 'n/a' }}</div>
        <div class="col-5">{{ i.requisition?.note || i.note }}</div>
        <div class="col-1 text-right">{{ i.price.toFixed(2) }}</div>
        <div class="col-2 text-right">{{ i.totalPrice.toFixed(2) }}</div>
      </div>
    </template>
  </Card>

</template>

<script lang="ts" setup>
import {computed, defineEmits, defineProps, inject, onMounted, reactive, ref} from 'vue'
import {InvoiceItem} from "@/components/blocks/Types";
import Card from "primevue/card";
import Panel from "primevue/panel";
import Button from "primevue/button";
import ConfirmDialog from "primevue/confirmdialog";
import {minValue, required} from "@vuelidate/validators";
import useVuelidate from "@vuelidate/core";
import InputField from "@/components/blocks/InputField.vue";
import InputFieldNum from "@/components/blocks/InputFieldNum.vue";
import {AxiosStatic} from "axios";
import {useConfirm} from "primevue/useconfirm";

// Define emits
const emits = defineEmits(['itemCreated'])

// Define type
interface Properties {
  items: InvoiceItem[]
  invoiceId: number,
  invoiceNumber: string,
}

type FormData = {
  orderNumber: any | null
  note: string | null,
  amount: number
  vatRate: number
}

const rules = {
  orderNumber: {required},
  note: {required},
  amount: {required, minValue: minValue(1)},
  vatRate: {required, minValue: minValue(0)}
}

// Define component properties
const props = defineProps<Properties>()

// Define component values
const invoiceItem = ref()
const axios = inject<AxiosStatic>('axios')

const formData = reactive<FormData>({
  orderNumber: null,
  note: null,
  amount: 0,
  vatRate: 21
})

const formRef = ref(null)
const submitted = ref<boolean>(false)
const v$ = useVuelidate(rules, formData)
const confirm = useConfirm()

// Define lifecycle hooks
onMounted(() => {
  if (props.items != null && props.items.length > 0) {
    invoiceItem.value = props.items
  }
})

// Define functions
function deleteItem(item: InvoiceItem, index: number) {

  confirm.require({
    message: 'Do you want delete item ' + (item?.requisition?.note || item.note),
    header: 'Delete item no. ' + (index + 1),
    icon: 'pi pi-exclamation-triangle',
    accept: () => {
      callRemoteDelete(item)
    }
  })
}

function callRemoteDelete(item: InvoiceItem) {

  axios?.delete(`/invoice/detail/${props.invoiceNumber}/delete-item/${item.id}`)
      .then(response => {
        if (response.status >= 200 && response.status <= 299) {
          emits('itemCreated', response.data)
        } else {
          console.log('Can not delete item from invoice!')
        }
      })
}

function handleSubmit(isFormValid: boolean) {
  submitted.value = true
  if (isFormValid) {
    saveItem()
  } else {
    console.error('Form is not valid!')
  }
}

function saveItem() {
  axios?.post("/invoice/detail/" + props.invoiceId + '/add-item', buildData()).then((response: any) => {
    if (response.status >= 200 && response.status <= 299) {
      emits('itemCreated', response.data)
      clearForm()
    } else {
      // message.error('Ulozeni spolecnosti se nezdarilo!')
      console.error(response)
    }
  })
}

function clearForm() {
  formData.note = null
  formData.amount = 0
  formData.vatRate = 21
  formData.orderNumber = null
  submitted.value = false
}

function buildData(): any {
  const pvv: number = ((formData.vatRate / 100.0) + 1.0) * formData.amount
  return {
    description: formData.note,
    price: formData.amount,
    vatRate: formData.vatRate,
    vatAmount: pvv - formData.amount,
    priceWithVat: pvv
  }
}

</script>

<style scoped>

</style>
