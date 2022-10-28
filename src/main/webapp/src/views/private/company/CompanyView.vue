<template>

  <create-company-form @companyCreated="companyCreatedHandler"></create-company-form>
  <list-companies-table ref="companiesTable"></list-companies-table>

</template>

<script lang="ts" setup>
import CreateCompanyForm from '@/components/app/CreateCompanyForm.vue'
import ListCompaniesTable from "@/components/app/ListCompaniesTable.vue";
import {ref} from 'vue'

const companiesTable = ref()

// Define functions
function companyCreatedHandler(item: any) {
  companiesTable.value.reload()
}
</script>


<style scoped lang="scss">


</style>
