<template>

  <div class="card">
    <div class="flex gap-2 flex-row flex-wrap card-container blue-container">
      <div
          class="align-items-center justify-content-center">
        <Dropdown
            style="width:16rem"
            id="rootProject"
            v-model="selectedRootProject"
            :options="gmRootProject"
            :filter="true"
            :filterFields="['text']"
            :autoFilterFocus="true"
            :autoOptionFocus="false"
            optionLabel="text"
            placeholder="Select root project"
            @change="handleRootProjectChange"
            :showClear="true">
        </Dropdown>
      </div>
      <div
          class="align-items-center justify-content-center">
        <Dropdown
            style="width:16rem"
            id="rootProject"
            v-model="selectedProject"
            :options="gmProject"
            :filter="true"
            :filterFields="['text']"
            :autoFilterFocus="true"
            :autoOptionFocus="false"
            optionLabel="text"
            placeholder="Select project"
            @change="handleProjectChange"
            :showClear="true">
        </Dropdown>
      </div>

    </div>
  </div>

</template>

<script lang="ts" setup>
import Dropdown, {DropdownChangeEvent} from "primevue/dropdown";
import {AxiosStatic} from "axios";
import {defineEmits, defineProps, inject, onMounted, ref, withDefaults} from "vue";

// Define types
type Props = {
  modelValue: any | null
}

type GemProject = {
  key: number,
  text: string,
  parentId: number | null,
  projects: Array<GemProject> | null,
  tags: Array<GemProject> | null,
}

//  Define emits
const emits = defineEmits(['update:modelValue'])

// Define input props
const props = withDefaults(defineProps<Props>(), {
  modelValue: null
})

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define props
const gmRootProject = ref<GemProject[]>([])
const gmProject = ref<GemProject[]>([])

const selectedRootProject = ref<GemProject | null>(null)
const selectedProject = ref<GemProject | null>(null)

// Define lifecycle hooks
onMounted(() => {
  axios?.get("/gem-timesheet-resource/list").then(response => {
    if (response.status >= 200 && response.status <= 299) {
      gmRootProject.value = response.data
    } else {
      console.error('Can not load GEM Timesheet project settings', response)
    }
  })
})

// Define methods
function handleRootProjectChange(event: DropdownChangeEvent) {
  if (event.value && event.value.projects) {
    gmProject.value = event.value.projects
    emits('update:modelValue', {
      rootProject: {
        text: selectedRootProject.value.text,
        id: selectedRootProject.value.key
      }, project: null
    })
  } else {
    gmProject.value = []
    emits('update:modelValue', {rootProject: null, project: null})
  }
}

function handleProjectChange(event: DropdownChangeEvent) {
  if (event.value) {
    emits('update:modelValue', {
      rootProject: {
        rootProject: {
          text: selectedRootProject.value.text,
          id: selectedRootProject.value.key
        }
      }, project: {
        text: selectedProject.value.text,
        id: selectedProject.value.key,
        parentId: selectedProject.value.parentId
      }
    })

  } else {
    emits('update:modelValue', {
      rootProject: {
        rootProject: {
          text: selectedRootProject.value.text,
          id: selectedRootProject.value.key
        }
      }, project: null
    })
  }
}

</script>

<style scoped>

</style>
