<template>
  <create-rate-form @itemCreated="itemCreatedHandler"></create-rate-form>
  <list-rate-table ref="rateTable"></list-rate-table>
</template>

<script lang="ts" setup>

// Define component properties
import {ref} from "vue";
import CreateRateForm from "@/components/app/CreateRateForm";
import ListRateTable from "@/components/app/ListRateTable";

const rateTable = ref()
// Define methods
function itemCreatedHandler(data:any) {
  rateTable.value.reload()
}

</script>

<style scoped>

</style>
