<template>
  <div v-if="userInfo.userInfo.initialized">

    <div class="flex gap-2 flex-row flex-wrap card-container blue-container">
      <div class="align-items-center justify-content-center">
        {{userInfo.userInfo.fromTime}}-{{userInfo.userInfo.toTime}}
      </div>
      <div class="align-items-center justify-content-center">
        {{ formatPrice(userInfo.allEarningSum)}}
      </div>

      <div class="align-items-center justify-content-center">
        {{ formatWorkTime(userInfo.allWorktimeSum)}}
      </div>


    </div>

  </div>
</template>

<script lang="ts" setup>

import {useUserInfo} from "@/stores/InfoStore";
import {formatPrice,formatWorkTime} from "@/services/FormatService";

const userInfo = useUserInfo()

</script>

<style scoped>

</style>
