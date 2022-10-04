<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App"/>
    <div v-if="!$keycloak.authenticated">
      <button @click="$keycloak.login()">Login</button>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent, inject} from 'vue';
import HelloWorld from '@/components/HelloWorld.vue';
import Keycloak from "keycloak-js"; // @ is an alias to /src

export default defineComponent({
  name: 'Home',
  components: {
    HelloWorld,
  },
  setup() {
    const keycloak = inject('keycloak') as Keycloak
    return {
      keycloak,
    }
  },
  methods: {
    refresh() {
      console.log(this.keycloak)
    }
  }
});
</script>
