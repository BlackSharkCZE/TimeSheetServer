<template>
  <IsAdmin>
    <create-project-form @itemCreated="handleProjectCreated"></create-project-form>
  </IsAdmin>
  <list-project-table ref="projectTable"></list-project-table>
</template>

<script lang="ts" setup>
import CreateProjectForm from "@/components/app/CreateProjectForm";
import ListProjectTable from "@/components/app/ListProjectTable";
import {ref} from "vue";
import IsAdmin from "@/components/blocks/IsAdmin.vue";

// Define component properties
const projectTable = ref()

// Define function
function handleProjectCreated(data: any) {
  projectTable.value.reload()
}
</script>

<style scoped>

</style>
