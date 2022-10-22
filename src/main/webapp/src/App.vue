<template>

  <div id="nav">
    <router-link to="/">Home</router-link>
    |
    <router-link to="/about">About</router-link>
    |
    <router-link to="/private/companies/create">Create Company</router-link>
    |
    <router-link to="/private/companies/list">List Companies</router-link>
    |
    <router-link to="/private/rate/create">Create Rate</router-link>
    |
    <router-link to="/private/home">Private Home</router-link>
    |
    <router-link to="/private/requisition/list">Smlouvy</router-link>
    |
    <router-link to="/private/requisition/create">Pridat smlouvu</router-link>
    |
    <router-link to="/private/project/create">Pridat projekt</router-link>

  </div>

  <Message :closable="false"  v-if="noPrimaryCompany" severity="warn">You do not have set primary company.</Message>

  <router-view/>

</template>

<script lang="ts" setup>
import Message from 'primevue/message'
import {useUserStore} from "@/stores/UserStore";
import {computed} from "vue";
const store = useUserStore()

const noPrimaryCompany = computed(()=> {
  return store.userDetail.company === undefined || store.userDetail.company === null
})

</script>

<style lang="scss">

</style>
