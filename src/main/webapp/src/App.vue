<template>
  <MegaMenu :model="items"></MegaMenu>
  <Message :closable="false" v-if="noPrimaryCompany" severity="warn">
    You do not have set primary company.
    <router-link style="color:#343434" to="/private/companies/create">Please add company here</router-link>
  </Message>
  <router-view/>
</template>

<script lang="ts" setup>
import Message from 'primevue/message'
import {useUserStore} from "@/stores/UserStore";
import {computed, ref} from "vue";

import MegaMenu from 'primevue/megamenu';

const store = useUserStore()

const noPrimaryCompany = computed(() => {
  return store.userDetail.company === undefined || store.userDetail.company === null
})

const items = ref([
  {
    label: 'Dashboard', icon: 'pi pi-fw pi-video', to: '/private/dashboard'
  },
  {
    label: 'Fakturace', icon: 'pi pi-fw pi-users',
    items: [
      [
        {
          label: 'User 1',
          items: [{label: 'User 1.1', to: "/"}, {label: 'User 1.2', to: "/"}]
        },
        {
          label: 'User 2',
          items: [{label: 'User 2.1', to: "/"}, {label: 'User 2.2', to: "/"}]
        },
      ],
      [
        {
          label: 'User 3',
          items: [{label: 'User 3.1', to: "/"}, {label: 'User 3.2', to: "/"}]
        },
        {
          label: 'User 4',
          items: [{label: 'User 4.1', to: "/"}, {label: 'User 4.2', to: "/"}]
        }
      ],
      [
        {
          label: 'User 5', to: "/",
          items: [{label: 'User 5.1', to: "/"}, {label: 'User 5.2', to: "/"}]
        },
        {
          label: 'User 6', to: "/",
          items: [{label: 'User 6.1', to: "/"}, {label: 'User 6.2', to: "/"}]
        }
      ]
    ]
  },
  {
    label: 'Konfigurace', icon: 'pi pi-fw pi-calendar',
    items: [
      [
        {
          label: 'Společnosti',
          items: [
            {label: 'Správa společností', to: '/private/companies'}
          ]
        },
        {
          label: 'Smlouvy',
          items: [
            {label: 'Správa smluv', to: '/private/requisition'}
          ]
        },
        {
          label: 'Projekty',
          items: [
            {label: 'Správa projektů', to: '/private/project'}]
        },
        {
          label: 'Rates',
          items: [
            {label: 'Správa rates', to: '/private/rate'}
          ]
        }
      ],

    ]
  }
])

</script>

<style lang="scss">

</style>
