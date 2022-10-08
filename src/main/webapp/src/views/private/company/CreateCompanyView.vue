<template>
  <h1>Create company page</h1>

  <div class="panel-layout">

    <div class="panel" v-if="userStore.userDetail.company===undefined || userStore.userDetail.company===null">
      Primary company is not set. Pleas set company first.
      <button>Create my company</button>
    </div>

    <div style="padding:3rem;">

        <BsInput id="input123"
                 ref="refInstance"
                 label="Input1"
                 :button-callback="makeSomething"
                 v-model:value="formData.name">OK</BsInput>

        <BsInput id="papud"
                 ref="refInstance2"
                 label="Input2" v-model:value="formData.label"></BsInput>

    </div>

    <div>
      <div>FormData.name = {{formData.name}}</div>
      <div>FormData.label = {{formData.label}}</div>
    </div>


  </div>

</template>

<script lang="ts" setup>

import {useUserStore} from "@/stores/UserStore";
import BsInput from "@/components/blackshark/BsInput.vue";
import {onMounted, reactive, ref} from "vue";


const userStore = useUserStore()
const errors = ref<string[]>([])
const checked = ref<boolean>(true)
const inputValue = ref<string | null>('Hello World')

// const refInstance = ref(null)


type FormData = {
  name: string | null
  label: string | null
}
const formData = reactive<FormData>({
  name: null,
  label: null
})

onMounted(()=>{
  const a = 1
})


function submitCallback() {
  const a=1
}

function makeSomething(value: any) {
  const a=1
}

</script>


<style scoped lang="scss">


</style>
