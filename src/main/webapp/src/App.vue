<template>
  <Menubar :model="items">
    <template #start>
      <img alt="logo" src="/img/icon.png" height="36" class="mr-2">
    </template>
    <template #end>
      <UserInfo></UserInfo>
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
import UserInfo from "@/components/blocks/UserInfo.vue";

const store = useUserStore()

const noPrimaryCompany = computed(() => {
  return store.userDetail.company === undefined || store.userDetail.company === null
})

const items = ref([
  {
    label: 'Dashboard', icon: 'pi pi-fw pi-calendar', to: '/private/dashboard'
  },
  {
    label: 'Accounting',
    icon: 'pi pi-fw pi-money-bill',
    items: [
      {label: 'Billing', to: '/private/billing'},
      {
        label: 'Invoices', items: [
          {label: 'List invoice', to: '/private/invoices'},
          {label: 'Upload invoice', to: '/private/invoices-add'}
        ]
      },
      {label: 'DPH', to: '/private/dph'}

    ]
  },
  {
    label: 'Configuration', icon: 'pi pi-fw pi-wrench',
    items: [
      {label: 'Manage companies', to: '/private/companies'},
      {label: 'Manage requisition', to: '/private/requisition'},
      {label: 'Manage projects', to: '/private/project'},
      {label: 'Manage rates', to: '/private/rate'},
      {label: 'Manage remote writers', to: '/private/writers'}
    ]
  },
  {
    label: 'Statistic', icon: 'pi pi-fw pi-wrench',
    items: [
      {label: 'Money flow', to: '/private/money-flow'},
      {label: 'Payments', to: '/private/payments'}
    ]
  }

])

</script>

<style lang="scss">

</style>
