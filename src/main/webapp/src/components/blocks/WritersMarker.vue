<template>
  <div v-if="writers != null && writers.length>0" class="card-container flex align-items-left justify-content-start">
    <div
        :class="{'text-red-500': !w.success, 'text-green-500': w.success}"
        v-for="(w, index) in writerDef"
        v-bind:key="index">{{ w.name }}
      <span class="mr-1" v-if="index<writerDef.length-1"> |</span>
    </div>

    <div v-if="!allSuccess">
      <span class="m-1"> |</span>
      <ClickIcon icon="pi pi-upload" @click="remoteWriteRequest()"></ClickIcon>
    </div>

  </div>


</template>

<script lang="ts" setup>

import {RemoteWriterTimestamp} from "@/components/blocks/Types";
import {defineEmits, defineProps, onMounted, ref, watch} from "vue";
import ClickIcon from "@/components/blocks/ClickIcon.vue";
import {useConfirm} from "primevue/useconfirm";

interface PropsInterface {
  writers: string[] | null,
  settings: RemoteWriterTimestamp[] | null
}

type WriterDef = {
  name: string,
  success: boolean,
  last: boolean
}

// Define props
const props = defineProps<PropsInterface>()

// Define refs
const writerDef = ref<WriterDef[]>([])
const allSuccess = ref<boolean>(false)

// Define use
const confirm = useConfirm()


// Define emits
const emits = defineEmits(['click'])

// Define lifecycle hooks
onMounted(() => {
  initialize()
})

// Define watch
watch(() => props.settings, (current, prev) => {
  initialize()
})

// Define function
function initialize() {
  if (props.writers != null && props.writers.length > 0) {
    const result = [] as WriterDef[]
    for (let i = 0; i < props.writers.length; i++) {
      const writer = props.writers[i]
      result.push({
        name: props.writers[i],
        last: i == props.writers.length - 1,
        success: props.settings?.filter(item => item.name == writer)[0]?.success || false
      } as WriterDef)
    }

    allSuccess.value = !(result.some(it => it.success === false))
    writerDef.value = result
  }
}

function remoteWriteRequest() {

  confirm.require({
    message: 'Do you want write timeline to remote system?',
    header: 'Remote write request',
    icon: 'pi pi-question-circle',
    accept: () => {
      emits('click')
    }
  })


}

</script>

<style scoped>

</style>
