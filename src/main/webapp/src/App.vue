<template>
  <Menubar :model="items">
    <template #start>
      <img alt="logo" src="/img/icon.png" height="36" class="mr-2">
    </template>
    <template #end>
      <InputText placeholder="Search" type="text"/>
    </template>
  </Menubar>
  <Message :closable="false" v-if="noPrimaryCompany" severity="warn">
    You do not have set primary company.
    <router-link style="color:#343434" to="/private/companies">Please add company here</router-link>
  </Message>
  <router-view/>
</template>

<script lang="ts" setup>
import Message from 'primevue/message'
import {useUserStore} from "@/stores/UserStore";
import {computed, ref} from "vue";
import Menubar from "primevue/menubar";
import InputText from "primevue/inputtext";

const store = useUserStore()

const noPrimaryCompany = computed(() => {
  return store.userDetail.company === undefined || store.userDetail.company === null
})

const items = ref([
  {
    label: 'Dashboard', icon: 'pi pi-fw pi-video', to: '/private/dashboard'
  },
  {
    label: 'Invoice',
    icon: 'pi pi-fw pi-users',
    items: [
      {label: 'Billing', to: '/private/billing'},
      {label: 'Invoice list', to: '/private/invoices'}
    ]
  },
  {
    label: 'Configuration', icon: 'pi pi-fw pi-calendar',
    items: [
      {label: 'Manage companies', to: '/private/companies'},
      {label: 'Manage requisition', to: '/private/requisition'},
      {label: 'Manage projects', to: '/private/project'},
      {label: 'Manage rates', to: '/private/rate'},
      {label: 'Manage remote writers', to: '/private/writers'}
    ]
  }
])

</script>

<style lang="scss">

</style>
