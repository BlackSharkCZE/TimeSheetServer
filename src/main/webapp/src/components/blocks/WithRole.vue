<template>
  <div v-if="hasAnyRole"><slot></slot></div>
  <div v-else><slot name="withoutRole"></slot></div>

</template>

<script lang="ts" setup>

import Keycloak from "keycloak-js";
import {computed, inject, defineProps} from "vue";

// Define types
type PropsType = {
  roles: string[]
}

// Define properties
const props = defineProps<PropsType>()

// Define inject
const keycloak = inject<Keycloak>('keycloak')

// Define computed
const hasAnyRole = computed(() => {
  console.log()
  return props.roles.filter(role => {
    return keycloak?.hasRealmRole(role)
  }).length > 0


})
// Define function

</script>

<style scoped>

</style>
