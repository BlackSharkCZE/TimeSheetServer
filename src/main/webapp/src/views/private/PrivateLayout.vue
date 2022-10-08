<template>
  <div class="private-page">
    <h2>this is private page</h2>
    <h3>Hello: {{ store.userDetail.userName }}</h3>

    <hr/>

    <div> User has role USER: {{ keycloak.hasRealmRole('USER') }}</div>


    <hr/>


    <p>
      {{ keycloak }}
    </p>

    <hr/>

    <p>
      Store: {{ store.userDetail }}
    </p>

  </div>

  <router-view name="private"></router-view>

</template>

<script lang="ts" setup>

import {inject, onMounted} from "vue";
import Keycloak from "keycloak-js";

import {useUserStore} from "@/stores/UserStore";

const store = useUserStore()
const keycloak = inject<Keycloak>('keycloak')

/*onMounted(() => {
})*/


</script>

<style scoped>

</style>
