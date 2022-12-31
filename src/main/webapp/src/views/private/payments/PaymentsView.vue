<template>

  <import-payments @new-payments="handleNewPayments"></import-payments>
  <create-payments @new-payment="handleNewPayments"></create-payments>
  <list-payments-table ref="paymentTable"></list-payments-table>


</template>

<script lang="ts" setup>

import ImportPayments from "@/components/app/ImportPayments.vue";
import CreatePayments from "@/components/app/CreatePayments.vue";
import ListPaymentsTable from "@/components/app/ListPaymentsTable.vue";
import {PaymentType} from "@/components/blocks/Types";
import {ref} from "vue";

// Define components ref
const paymentTable = ref()

// Define function
function handleNewPayments(items: PaymentType[]) {
  console.log('New payments added', items)
  paymentTable.value.loadData()
}

</script>

<style scoped>

</style>
