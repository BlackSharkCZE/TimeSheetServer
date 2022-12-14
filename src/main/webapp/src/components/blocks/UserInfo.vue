<template>
  <div v-if="userInfo.userInfo.initialized">

    <div class="flex gap-2 flex-row flex-wrap card-container blue-container text-sm">
      <div class="align-items-center justify-content-center">
        {{month}}
      </div>
      <div class="align-items-center justify-content-center font-bold text-lg">
        {{ earning }}
      </div>

      <div class="align-items-center justify-content-center text-sm">
        {{ formatWorkTime(userInfo.allWorktimeSum) }} [hod]
      </div>

    </div>

  </div>
</template>

<script lang="ts" setup>

import {useUserInfo} from "@/stores/InfoStore";
import {formatPrice,formatWorkTime} from "@/services/FormatService";
import {computed} from "vue";
import moment from "moment";

const userInfo = useUserInfo()

// Define computed
const month = computed(() => {
  const mf = moment(userInfo.userInfo.fromTime)
  const mt = moment(userInfo.userInfo.toTime)
  return mf.format("MMMM YYYY")
})

const earning = computed(() => {
  return Math.round(userInfo.allEarningSum) + " Kč"
})


</script>

<style scoped>
.price {

}
</style>
