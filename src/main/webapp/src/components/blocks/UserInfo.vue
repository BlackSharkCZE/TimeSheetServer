<template>
  <div>

    <div class="flex gap-2 flex-row flex-wrap card-container blue-container text-sm">
      <div v-if="userInfo.userInfo.initialized" class="align-items-center justify-content-center">
        {{ month }}
      </div>
      <div v-if="userInfo.userInfo.initialized" class="align-items-center justify-content-center font-bold text-lg">
        {{ earning }}
      </div>

      <div v-if="userInfo.userInfo.initialized" class="align-items-center justify-content-center text-sm">
        {{ formatWorkTime(userInfo.allWorktimeSum) }} [hod]
      </div>
      <div v-if="userStore.userDetail.userName" class="align-items-center justify-content-center text-sm">
        <strong>{{ userStore.userDetail.userName }}</strong>
      </div>
      <div class="align-items-center justify-content-center text-sm">
        <i class="pi-sign-out pi text-cyan-200 cursor-pointer" @click="handleLogout"></i>
      </div>

    </div>

  </div>
</template>

<script lang="ts" setup>

import {UserInfo, useUserInfo} from "@/stores/InfoStore";
import {formatWorkTime} from "@/services/FormatService";
import {computed, inject} from "vue";
import moment from "moment";
import {useRouter} from "vue-router";
import {useUserStore} from "@/stores/UserStore";
import {AxiosStatic} from "axios";

const router = useRouter()
const userInfo = useUserInfo()

const userStore = useUserStore()

// Define computed
const month = computed(() => {
  const mf = moment(userInfo.userInfo.fromTime)
  const mt = moment(userInfo.userInfo.toTime)
  return mf.format("MMMM YYYY")
})

const earning = computed(() => {
  return Math.round(userInfo.allEarningSum) + " Kč"
})

const axios = inject<AxiosStatic>('axios')

const handleLogout = () => {
  document.cookie = "quarkus-credential=; Max-Age=0"
  userStore.logout()
  router.push("/login")
}

if (!userInfo.userInfo.initialized) {
  const from = moment().add(0, 'month').startOf('month').format('YYYY-MM-DD')
  const to = moment().add(0, 'month').endOf('month').format('YYYY-MM-DD')

  axios.get(`/timeline/earning?from=${from}&to=${to}`).then(response => {
    if (response.status == 200) {
      const dataToStore = {
        initialized: true,
        fromTime: from,
        toTime: to,
        earnings: response.data
      } as UserInfo
      userInfo.storeUserInfo(dataToStore)
    }
  })

} else {
  console.log('UserInfo already initialized!')
}

</script>

<style scoped>
.price {

}
</style>
