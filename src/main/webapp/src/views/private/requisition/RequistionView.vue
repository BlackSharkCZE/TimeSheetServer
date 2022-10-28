<template>
  <create-requisition-form @itemCreated="handleItemCreated"></create-requisition-form>
  <list-requisition-table ref="requisitionTable"></list-requisition-table>
</template>

<script lang="ts" setup>

import CreateRequisitionForm from "@/components/app/CreateRequisitionForm.vue";
import ListRequisitionTable from "@/components/app/ListRequisitionTable.vue";
import {ref} from "vue";

// Define component props
const requisitionTable = ref()

function handleItemCreated(data: any) {
  requisitionTable.value.reload()
}

</script>

<style scoped>

</style>
