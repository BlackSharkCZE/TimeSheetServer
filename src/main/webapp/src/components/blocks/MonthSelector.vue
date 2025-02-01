<template>

  <form @submit.prevent="handleSubmit()" class="p-fluid">

    <div class="card">
      <div class="formgrid grid">

        <div class="field col-6 sm:col-4 md:col-3 lg:col-2 xl:col-1">
          <Dropdown v-model="formData.year" :options="years"/>
        </div>

        <div class="field col-6 sm:col-4 md:col-3 lg:col-2 xl:col-1">
          <Dropdown v-model="formData.month" optionLabel="value" :options="computedMonths"/>
        </div>

        <div class="field col-6 sm:col-2 md:col-3 lg:col-2 xl:col-1">
          <Button type="submit" label="Select"/>
        </div>

      </div>
    </div>
  </form>

</template>

<script lang="ts" setup>
import {computed, defineEmits, defineProps, onMounted, ref, withDefaults} from 'vue'
import {months, MonthType, SelectedMonthType} from "@/components/blocks/Types";
import Dropdown from "primevue/dropdown";
import Button from "primevue/button";

type Properties = {
  years?: number[],
  modelValue: SelectedMonthType
}

// Define emits
const emits = defineEmits(['update:modelValue', 'change'])

// Define component property
const props = withDefaults(defineProps<Properties>(), {
  years: () => {
    const start = 2020;
    const endDate = (new Date()).getFullYear() + 1;
    return Array.from({length: endDate - start + 1}, (_, i) => start + i);
  },
  // years: () => [2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030]
})

// Define lifecycle hooks
onMounted(() => {
  formData.value = props.modelValue
})

// Define private property
const formData = ref<SelectedMonthType>({} as SelectedMonthType)

// Define computed values
const computedMonths = computed(() => {
  return months.map((_item, _index) => {
    return {
      index: _index,
      value: _item
    } as MonthType
  })
})


// Define functions
function handleSubmit() {
  emits('change', formData.value)
}

</script>

<style scoped>

</style>
