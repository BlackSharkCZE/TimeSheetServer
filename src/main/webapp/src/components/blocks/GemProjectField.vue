<template>

  <div class="card">
    <div class="flex gap-2 flex-row flex-wrap card-container blue-container">
      <div class="align-items-center justify-content-center">

        <label for="GemProjectField:rootProject">Root project<span
            v-if="vualidate.rootProject.required">*</span></label>
        <Dropdown
            :class="{'p-invalid':vualidate.rootProject.$invalid && submitted}"
            style="width:16rem"
            id="GemProjectField:rootProject"
            v-model="selectedRootProject"
            :options="gmRootProject"
            :filter="true" e
            :filterFields="['text']"
            :autoFilterFocus="true"
            :autoOptionFocus="false"
            optionLabel="text"
            placeholder="Select root project"
            @change="handleRootProjectChange"
            :showClear="true">
        </Dropdown>
        <small v-if="vualidate.rootProject.$invalid && submitted"
               class="p-error">{{ vualidate.rootProject.required.$message.replace('Value', 'Root project') }}</small>
      </div>
      <div class="align-items-center justify-content-center">
        <label for="GemProjectField:project">Project<span
            v-if="vualidate.project.required">*</span></label>
        <Dropdown
            style="width:16rem"
            :class="{'p-invalid':vualidate.project.$invalid && submitted}"
            id="GemProjectField:project"
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
        <small v-if="vualidate.project.$invalid && submitted"
               class="p-error">{{ vualidate.project.required.$message.replace('Value', 'Project') }}</small>
      </div>

      <div
          class="align-items-center justify-content-center">
        <label for="GemProjectField:tags">Tag</label>
        <Dropdown
            style="width:16rem"
            id="GemProjectField:tags"
            v-model="selectedTag"
            :options="gmTags"
            :filter="true"
            :filterFields="['text']"
            :autoFilterFocus="true"
            :autoOptionFocus="false"
            optionLabel="text"
            placeholder="Select tag"
            @change="handleTagChange"
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
import {GemProject} from "@/components/blocks/Types";

// Define types
type Props = {
  rootProject: any | null,
  project: any | null,
  tag: any | null,
  vualidate?: any | null,
  submitted: boolean

}

//  Define emits
const emits = defineEmits(['update:rootProject', 'update:project', 'update:tag'])

// Define input props
const props = withDefaults(defineProps<Props>(), {
  modelValue: null,
  rootProject: null,
  project: null,
  tag: null,
  submitted: false
})

// Define injects
const axios = inject<AxiosStatic>('axios')

// Define props
const gmRootProject = ref<GemProject[]>([])
const gmProject = ref<GemProject[]>([])
const gmTags = ref<GemProject[]>([])

const selectedRootProject = ref<GemProject | null>(null)
const selectedProject = ref<GemProject | null>(null)
const selectedTag = ref<GemProject | null>(null)

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
  gmProject.value = []
  gmTags.value = []

  selectedProject.value = null
  selectedTag.value = null

  if (event.value && event.value.projects) {
    gmProject.value = event.value.projects

    const _rootProject = {
      text: selectedRootProject.value?.text,
      id: selectedRootProject.value?.key
    }
    const eventData = {
      rootProject: _rootProject,
      project: null,
      tag: null
    }

    emits('update:rootProject', eventData.rootProject)
    emits('update:project', null)
    emits('update:tag', null)
  } else {

    emits('update:rootProject', null)
    emits('update:project', null)
    emits('update:tag', null)
  }
}

function handleProjectChange(event: DropdownChangeEvent) {
  selectedTag.value = null
  gmTags.value = event.value?.tags
  const _rootProject = {
    text: selectedRootProject.value?.text,
    id: selectedRootProject.value?.key
  }

  if (event.value) {
    const _project = {
      text: selectedProject.value?.text,
      id: selectedProject.value?.key,
      parentId: selectedProject.value?.parentId
    }
    const eventData = {
      rootProject: _rootProject,
      project: _project,
      tag: null
    }

    emits('update:project', eventData.project)
    emits('update:tag', null)

  } else {
    const eventData = {
      rootProject: _rootProject,
      project: null,
      tag: null
    }
    gmTags.value = []
    emits('update:project', eventData.project)
    emits('update:tag', null)
  }
}

function handleTagChange(event: DropdownChangeEvent) {
  if (event.value) {

    const _rootProject = {
      text: selectedRootProject.value?.text,
      id: selectedRootProject.value?.key
    }
    const _project = {
      text: selectedProject.value?.text,
      id: selectedProject.value?.key,
      parentId: selectedProject.value?.parentId
    }

    const _tag = {
      text: selectedTag.value?.text,
      id: selectedTag.value?.key,
      parentId: selectedTag.value?.parentId,

    }

    const eventData = {
      rootProject: _rootProject,
      project: _project,
      tag: _tag
    }
    emits('update:tag', eventData.tag)

  } else {

    emits('update:tag', null)

  }
}


</script>

<style scoped>

</style>
