<template>

  <create-timeline-form @itemCreated="itemCreatedHandler"></create-timeline-form>

  <list-timeline-table ref="createTimelineForm"></list-timeline-table>
</template>

<script lang="ts" setup>
import ListTimelineTable from "@/components/app/ListTimelineTable.vue";
import CreateTimelineForm from "@/components/app/CreateTimelineForm.vue";
import {ref} from "vue";

// Define component properties
const createTimelineForm = ref<any>(null)

// Define methods
function itemCreatedHandler(row: any) {
  createTimelineForm.value.reloadTable()
}

</script>

<style scoped>

</style>
