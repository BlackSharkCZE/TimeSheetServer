<template>

  <Panel header="Balance Overview" class="mt-2">

    <h2>
      Profit: <span :class="{'loss': profit<0, 'low': profit<10000}">{{formatPrice(profit)}}</span> ({{items.length}} měsíců) |
      <span :class="{'loss': yearProfit<0, 'low': yearProfit<10000}">{{formatPrice(yearProfit)}}</span> (12 měsíců)
    </h2>

    <DataTable
        headerStyle="color:red !important"
        :page="pageIndex+1"
        ref="dt"
        dataKey="id"
        :value="items"
        :paginator="true"
        :rows="pageSize"
        :rowHover="true"
        :totalRecords="totalRecords"
        :loading="loading">

      <Column field="month" header="Datum">
        <template #body="{data}">{{ formatDate(data.month) }}</template>
      </Column>
      <Column field="income" header="Příjem">
        <template #body="{data}">{{ formatPrice(data.income) }}</template>
      </Column>
      <Column field="outcome" header="Výdaj">
        <template #body="{data}">{{ formatPrice(data.outcome) }}</template>
      </Column>
      <Column header="Rozdíl">
        <template #body="{data}"><span :class="{'loss': (data.income + data.outcome)<0, 'low': (data.income + data.outcome)<10000}"> {{ formatPrice(data.income + data.outcome) }} </span>
        </template>
      </Column>

    </DataTable>

  </Panel>


</template>

<script lang="ts" setup>

import {inject, onMounted, ref} from "vue";
import {AxiosStatic} from "axios";
import Panel from "primevue/panel";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import {BalanceType} from "@/components/blocks/Types";
import {formatDate, formatPrice} from "../../services/FormatService";

// Define types

// Define inject
const axios = inject<AxiosStatic>('axios')

// Define refs

const items = ref<BalanceType[]>([])
const loading = ref<boolean>(true)
const dt = ref(null);
const pageIndex = ref<number>(0)
const pageSize = ref<number>(12)
const totalRecords = ref<number>(0)
const profit = ref<number>(0)
const yearProfit = ref<number>(0)


// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define functions
function loadData() {
  loading.value = true
  axios?.get("/balance").then(response => {
    if (response.status == 200 && response.data.success === true) {
      items.value = response.data.data
      totalRecords.value = response.data.data.length
      loading.value = false
      profit.value = items.value.map( item => item.income+item.outcome).reduce((acc, current) => acc + current,0.0)
      yearProfit.value = items.value.slice(0,12).map( item => item.income+item.outcome).reduce((acc, current) => acc + current,0.0)
    } else {
      console.error('Can not load balance from server', response)
      loading.value = false
    }
  })
}

</script>

<style scoped lang="scss">

.loss {
  font-weight: bold;
  color: var(--red-400) !important;
}
.low {
  color: var(--orange-400)
}

</style>
