<template>

  <h2>Earn History Graph</h2>

  <div>{{ dataSource }}</div>


</template>

<script lang="ts" setup>

import {inject, onMounted, ref} from "vue";
import {AxiosStatic} from "axios";
import moment from "moment/moment";

// Define types
type EarnType = {
  uuid: string,
  issuerId: number,
  issueDate: Date,
  paymentDate: Date,
  price: number,
  totalPrice: number,
  recipientId: number,
  recipientName: string
}

type StatValueType = {
  earning: number,
  company: string
}

type StatType = {
  key: string,
  value: StatValueType[]
}

// Define inject
const axios = inject<AxiosStatic>('axios')

// Define refs
const dataSet = ref<EarnType[]>([])
const dataSource = ref<Map<string, StatValueType[]>>(new Map<string, StatValueType[]>())

// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define functions
function loadData() {
  axios?.get("/statistics").then(response => {
    if (response.status == 200) {
      dataSet.value = response.data

      let x = new Map<string,StatValueType[]>()

      dataSet.value.forEach(item => {
        const m = moment(item.paymentDate.toString())
        const key = m.format("YYYY-MM")
        if (!x.has(key)) {
          x.set(key, [{
            earning: item.price,
            company: item.recipientName
          } as StatValueType])
        } else {
          const a = x.get(key)
          if (a) {
            a.push({
              earning: item.price,
              company: item.recipientName
            } as StatValueType)
          }
        }
      })
      dataSource.value = x
      console.log(dataSource.value)

    } else {
      console.error('Can not load earn history from server', response)
    }
  })
}

</script>

<style scoped>

</style>
