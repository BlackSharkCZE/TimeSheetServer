<template>
  <remote-writers-form @itemCreated="handleItemCreated"></remote-writers-form>
  <list-remote-writers-table ref="writersTable"></list-remote-writers-table>

</template>

<script lang="ts" setup>
import {ref} from 'vue'

import ListRemoteWritersTable from "@/components/app/ListRemoteWritersTable.vue";
import RemoteWritersForm from "@/components/app/RemoteWritersForm.vue"

// Define component properties
const writersTable = ref()

// Define functions
function handleItemCreated(item: any) {
  writersTable.value.reload()
}

</script>

<style scoped>

</style>
