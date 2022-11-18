<template>

  <Panel header="Select month"
         class="mt-2">

    <form @submit.prevent="handleSubmit()" class="p-fluid">

      <div class="card">
        <div class="formgrid grid">

          <div class="field col-2">
            <Dropdown v-model="formData.year" :options="computedYears"/>
          </div>

          <div class="field col-2">
            <Dropdown v-model="formData.month" optionLabel="value" :options="computedMonths"/>
          </div>

          <div class="field col-2">

            <Button type="submit" label="Select"/>
          </div>
        </div>
      </div>
    </form>
  </Panel>


  <h2>DPH view</h2>
  <div>
    MinYear: {{ minYear }}
  </div>
  <div>
    MaxYear: {{ maxYear }}
  </div>
  <div>
    FormData: {{ formData }}
  </div>

  <div>
    <h3>Issued</h3>
    {{ fii }}
  </div>
  <div>
    <h3>Received</h3>
    {{ fri }}
  </div>

  <ul>
    <li v-for="i in (maxYear-minYear+1)" v-bind:key="i">{{ minYear + i - 1 }}</li>
  </ul>


</template>

<script lang="ts" setup>
import {computed, inject, onMounted, reactive, Ref, ref} from "vue";
import {AxiosResponse, AxiosStatic} from "axios";
import moment from "moment";
import Dropdown from "primevue/dropdown";
import Panel from "primevue/panel";
import Button from "primevue/button";

// Define types
type MonthType = {
  index: number,
  value: string
}
type FormData = {
  year: number
  month: MonthType
}

const months = ['Leden', 'Únor', 'Březen', 'Duben', 'Květen', 'Červen', 'Červenec', 'Srpen', 'Září', 'Říjen', 'Listopad', 'Prosinec']

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define refs
const issuedInvoice = ref([])
const receivedInvoice = ref([])

const fii = ref<any[]>([])
const fri = ref<any[]>([])

const minYear = ref<number>(2000)
const maxYear = ref<number>(3000)

// Define reactive
const formData = reactive<FormData>({
  year: 2000,
  month: {index: 0, value: 'Leden'},
})


// Define lifecycle hooks
onMounted(() => {
  const m = moment().subtract(1, 'month')
  formData.month = {
    index: m.month(),
    value: months[m.month()]
  }
  axios?.get('/dph/issued-invoice').then(response => responseProcessor(response, issuedInvoice, 'i'))
  axios?.get('/dph/received-invoice').then(response => responseProcessor(response, receivedInvoice, 'r'))
})

// Define function
const responseProcessor = (response: AxiosResponse<any, any>, data: Ref, type: string): void => {
  if (response.status >= 200 && response.status <= 299) {
    data.value = response.data
    if (response.data.length > 0) {
      const lastRecord = moment(response.data[response.data.length - 1].issueDate)
      const firstRecord = moment(response.data[0].issueDate)
      minYear.value = Math.max(minYear.value, lastRecord.year())
      maxYear.value = Math.min(maxYear.value, firstRecord.year())
      formData.year = Math.max(maxYear.value)

      switch(type) {
        case 'i':
          fii.value = filterBySelectedDate(data.value);
          break;
        case 'r':
          fri.value = filterBySelectedDate(data.value);
          break;
      }
    }
  } else {
    console.error('Can not load issued invoice')
  }
}

function handleSubmit() {
  fii.value = filterBySelectedDate(issuedInvoice.value)
  fri.value = filterBySelectedDate(receivedInvoice.value)
}

function filterBySelectedDate(input: Array<any>): Array<any> {
  return input.filter(value => {
    const m = moment(value.issueDate)
    return m.month() == formData.month.index && m.year() == formData.year
  })
}

// Define enumerated
const computedYears = computed(() => {
  let years = []
  for (let a = minYear.value; a <= maxYear.value; a++) {
    years.push(a)
  }
  return years
})

const computedMonths = computed(() => {
  return months.map((_item, _index) => {
    return {
      index: _index,
      value: _item
    } as MonthType
  })
})


</script>

<style scoped>

</style>
