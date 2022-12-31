<template>

  <Panel header="Earn History"
         class="mt-2">
    <Chart type="bar" :data="basicData" :options="stackedOptions" :height="90"/>
  </Panel>


</template>

<script lang="ts" setup>

import {inject, onMounted, ref} from "vue";
import {AxiosStatic} from "axios";
import moment from "moment/moment";
import Chart from "primevue/chart";
import Panel from "primevue/panel";

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
const basicData = ref({})

const stackedOptions = ref({
  plugins: {
    tooltip: {
      mode: 'index',
      intersect: false,
      callbacks: {
        footer: (tooltipItems: any[]) => {
          let sum = 0
          tooltipItems.forEach(function(tooltipItem) {
            sum += tooltipItem.parsed.y;
          });
          return 'Sum: ' + sum + " Kc"

        }
      }
    },
    legend: {
      labels: {
        color: '#495057'
      }
    }
  },
  scales: {
    x: {
      stacked: true,
      ticks: {
        color: '#495057'
      },
      grid: {
        color: '#ebedef'
      }
    },
    y: {
      stacked: true,
      ticks: {
        color: '#495057'
      },
      grid: {
        color: '#ebedef'
      }
    }
  }
})


// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define functions
function loadData() {
  axios?.get("/statistics").then(response => {
    if (response.status == 200) {
      dataSet.value = response.data

      let x = new Map<string, StatValueType[]>()

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

      const _labels = [...x.keys()]
      const companies: string[] = []
      _labels.forEach(it => x.get(it)?.forEach(item => companies.push(item.company)))
      const uniqCompanies: string[] = [...new Set(companies)]

      const colors = ['#42A5F5', '#FFA726', '#78909C', '#EC407A', '#AB47BC', '#42A5F5', '#7E57C2', '#66BB6A', '#FFCA28', '#26A69A']
      let colorIndex = 0

      const _datasets = uniqCompanies.map(companyName => {
        const _data: number[] = []
        _labels.forEach(label => {
          _data.push(
              x.get(label)?.filter(item => item.company == companyName).map(item => item.earning).reduce((acc, current) => acc + current, 0.0) || 0.0
          )
        })
        return {
          label: companyName,
          backgroundColor: colors[colorIndex++],
          data: _data
        }
      })

      basicData.value = {
        labels: _labels,
        datasets: _datasets
      }
    } else {
      console.error('Can not load earn history from server', response)
    }
  })
}

</script>

<style scoped>

</style>
