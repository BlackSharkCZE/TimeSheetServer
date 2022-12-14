<template>

  <Dialog header="Header" v-model:visible="visible" :modal="true" position="top" :breakpoints="{'960px': '75vw', '640px': '90vw'}"
          :style="{width: '50vw'}">
    <p>
      {{row}}
    </p>
    <template #footer>
      <Button label="Cancel" icon="pi pi-times" class="p-button-text" @click="hideDialog()"/>
      <Button label="Save" icon="pi pi-check" autofocus/>
    </template>
  </Dialog>

</template>

<script lang="ts" setup>

import Dialog from 'primevue/dialog'
import Button from 'primevue/button'
import {defineProps, defineEmits, onMounted, ref, watch, reactive, inject} from 'vue'

import {FormDataType, TimelineRules, TimelineType} from "@/components/blocks/TimelineDefs";
import {AxiosStatic} from "axios";
import useVuelidate from "@vuelidate/core";


// Define types
type PropsType = {
  display: boolean,
  row: TimelineType | null
}

const formData = reactive<FormDataType>({
  project: null,
  date: new Date(),
  fromTime: null,
  toTime: null,
  note: null,
  pause: 30
})

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define props
const props = defineProps<PropsType>()

// Define emits
const emits = defineEmits(['update:display', 'update:row', 'updated'])

// Define lifecycle
onMounted(() => { visible.value = props.display})

// Define refs
const visible = ref<boolean>(false)
const v$ = useVuelidate(TimelineRules, formData)
const submitted = ref<boolean>(false)
const errorMessage = ref<string | null>(null)

// Define watch
watch(() => props.display, (cur, prev) => visible.value = cur)
watch(() => props.row, (cur, prev) => {
  if (cur != null) {
    formData.project = cur.project // TODO musi se najit projekt z nejake kolekce. Budeme udrzovat projekty v store? Nebo je zde budeme nacitat?
  }
})

// Define functions
function hideDialog() {
  emits('update:row', null)
  emits('update:display', false)
}

function updateRow() {
  emits('updated', )
}

</script>

<style scoped>

</style>
