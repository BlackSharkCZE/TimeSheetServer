<template>
  <Menubar :model="items" v-if="store.userDetail.userName !== 'anonymous'">
    <template #start>
      <img alt="logo" src="/img/icon.png" height="36" class="mr-2">
    </template>
    <template #end>
      <UserInfo></UserInfo>

    </template>
  </Menubar>
  <Message :closable="false" v-if="noPrimaryCompany && store.userDetail.userName !== 'anonymous'" severity="warn">
    You do not have set primary company.
    <router-link style="color:#343434" to="/private/companies">Please add company here</router-link>
  </Message>
  <router-view/>
</template>

<script lang="ts" setup>
import Message from 'primevue/message'
import {useUserStore} from "@/stores/UserStore";
import {computed, inject, ref} from "vue";
import Menubar from "primevue/menubar";
import UserInfo from "@/components/blocks/UserInfo.vue";
import {useI18n} from "vue-i18n";
import {useUserInfo} from "@/stores/InfoStore";
import {AxiosStatic} from "axios";

const store = useUserStore()

const i18n = useI18n()

const noPrimaryCompany = computed(() => {
  return store.userDetail.company === undefined || store.userDetail.company === null
})

const items = ref([
  {
    label: i18n.t('dashboard'), icon: 'pi pi-fw pi-calendar', to: '/private/dashboard'
  },
  {
    label: 'Vyúčtování',
    icon: 'pi pi-fw pi-money-bill',
    items: [
      {label: 'Vyúčtování', to: '/private/billing'},
      {
        label: 'Faktury', items: [
          {label: 'Přehled faktur', to: '/private/invoices'},
          {label: 'Přidat fakturu', to: '/private/invoices-add'}
        ]
      },
      {label: 'DPH', to: '/private/dph'},
      {label: 'Daně', to: '/private/tax'},
    ]
  },
  {
    label: 'Nastavení', icon: 'pi pi-fw pi-wrench',
    items: [
      {label: 'Správa společností', to: '/private/companies'},
      {label: 'Správa objednávek', to: '/private/requisition'},
      {label: 'Správa projektů', to: '/private/project'},
      {label: 'Sazby', to: '/private/rate'},
      {label: 'Zápis do externích systémů', to: '/private/writers'}
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
