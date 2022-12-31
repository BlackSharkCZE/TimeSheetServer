<template>
  <div class="home">

    <gem-project-field v-model="gemProject" ></gem-project-field>

    {{gemProject}}


    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App"/>
    <div v-if="!$keycloak.authenticated">
      <button @click="$keycloak.login()">Login</button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {defineComponent, inject, ref} from 'vue';
import HelloWorld from '@/components/HelloWorld.vue';
import Keycloak from "keycloak-js";
import GemProjectField from "@/components/blocks/GemProjectField.vue"; // @ is an alias to /src

const gemProject = ref({rootProject: null, project: null})


</script>
