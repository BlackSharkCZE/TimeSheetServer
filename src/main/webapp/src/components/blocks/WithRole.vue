<template>
  <div v-if="hasAnyRole"><slot></slot></div>
  <div v-else><slot name="withoutRole"></slot></div>

</template>

<script lang="ts" setup>

import Keycloak from "keycloak-js";
import {computed, inject, defineProps} from "vue";
import {useUserStore} from "@/stores/UserStore";

// Define types
type PropsType = {
  roles: string[]
}

// Define properties
const props = defineProps<PropsType>()
const userStore = useUserStore()


// Define computed
const hasAnyRole = computed(() => {
  const x = props.roles.filter(role => {
    return userStore.hasRole(role)
  }).length > 0

  console.log('Require role found: ', x)

  return x

})
// Define function

</script>

<style scoped>

</style>
