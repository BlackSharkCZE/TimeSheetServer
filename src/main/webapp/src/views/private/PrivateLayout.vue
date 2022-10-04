<template>
  <div class="private-page">
    <h2>this is private page</h2>
    <h3>Hello: {{ keycloak.subject }}</h3>

    <hr />

    <div> User has role USER: {{keycloak.hasRealmRole('USER')}}</div>


    <hr />


    <p>
      {{keycloak}}
    </p>
  </div>

  <router-view name="private"></router-view>

</template>

<script lang="ts" setup>

import {inject, onMounted} from "vue";
import Keycloak from "keycloak-js";

const keycloak = inject<Keycloak>('keycloak')

onMounted(() => {
  console.log('PrivateLayout Mounted!')
})


</script>

<style scoped>

</style>
