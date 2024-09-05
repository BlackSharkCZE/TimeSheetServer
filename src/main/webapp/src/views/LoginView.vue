<script lang="ts" setup>
import Button from "primevue/button";
import Dropdown from "primevue/dropdown";
import InputText from "primevue/inputtext";
import {useI18n} from "vue-i18n";
import {inject, ref} from "vue";
import type {AxiosStatic} from "axios";
import {useRoute} from "vue-router";
import Message from "primevue/message";

const axios = inject<AxiosStatic>('axios')
const htmlForm = ref()

interface FormDataType {
  user_name: string,
  user_password: string,
}

interface LoginDataType {
  j_username: string,
  j_password: string,
}

const t = useI18n()
const formData = ref<FormDataType>({
  user_name: '',
  user_password: '',
})

const loginData = ref<LoginDataType>({
  j_username: '',
  j_password: ''
})

const route = useRoute()
console.log('Route params', route.query)


function handleFormSubmit() {
  loginData.value.j_username = formData.value.user_name
  loginData.value.j_password = formData.value.user_password
  setTimeout(()=>{
    htmlForm.value.submit()
  },1)
}
</script>

<template>

  <form ref="htmlForm" action="/j_security_check" method="post" style="visibility: hidden">
    <input type="text" name="j_username" v-model="loginData.j_username"/>
    <input type="text" name="j_password" v-model="loginData.j_password"/>
  </form>

  <div>
    <div class="middle text-2xl">
      <div class="flex flex-column">

        <div class="flex align-items-center justify-content-center p-2">

          <Message v-if="route.query.error!==undefined" severity="error" >Neplatné přihlášení</Message>
        </div>

        <div class="flex align-items-center justify-content-center p-2">
          <InputText v-model="formData.user_name" :placeholder="t.t('your_id')" autofocus @keyup.enter="handleFormSubmit"/>
        </div>
        <div class="flex align-items-center justify-content-center p-2">
          <InputText v-model="formData.user_password" type="password"
                     @keyup.enter="handleFormSubmit"
                     :placeholder="t.t('your_password')"/>
        </div>

        <div class="flex align-items-center justify-content-center p-2">
          <Button type="button" :label="t.t('login')" @click="handleFormSubmit" @keyup.enter="handleFormSubmit"/>
        </div>
      </div>
    </div>
  </div>


</template>

<style scoped>

</style>
