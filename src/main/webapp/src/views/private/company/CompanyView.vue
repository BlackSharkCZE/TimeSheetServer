<template>
  <IsAdmin>
    <create-company-form @companyCreated="companyCreatedHandler"></create-company-form>
  </IsAdmin>
  <list-companies-table ref="companiesTable"></list-companies-table>

</template>

<script lang="ts" setup>
import CreateCompanyForm from '@/components/app/CreateCompanyForm.vue'
import ListCompaniesTable from "@/components/app/ListCompaniesTable.vue";
import {ref} from 'vue'
import IsAdmin from "@/components/blocks/IsAdmin.vue";

// Define template refs
const companiesTable = ref()

// Define properties
const formCollapsed = ref<boolean>(true)

// Define functions
function companyCreatedHandler(item: any) {
  companiesTable.value.reload()
}
</script>


<style scoped lang="scss">


</style>
