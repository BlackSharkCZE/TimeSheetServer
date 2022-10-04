<template>
  <div class="justify-center flex bg-yellow-300 items-center h-screen">
    <div class="text-4xl">This is an about page</div>
    <h3>{{message}}</h3>
    <button @click="increment()">Click: {{counter}}</button>
  </div>
</template>

<script lang="ts" setup>

import {ref} from "vue";

const message = "Hello World from Vue"
let counter = ref<number>(1)

const increment = () => {
  counter.value = counter.value.valueOf() + 1
}


</script>

