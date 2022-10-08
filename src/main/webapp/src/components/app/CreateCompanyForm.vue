<template>
  <div class="form">
    <div class="row">
      <div class="item">
        <bs-input ref="icInput" v-model="company.ic" :required="true" label="IČ" :button-callback="loadDataFromAres">
          <font-awesome-icon :icon="searchAresIcon" :spin="searchAresIcon==='spinner'"></font-awesome-icon>
        </bs-input>
      </div>
      <div class="item">
        <bs-input v-model="company.dic" label="DIČ"/>
      </div>
      <div class="item">
        <bs-input ref="nameInput" v-model="company.companyName" :required="true" label="Název společnosti"/>
      </div>
    </div>

    <div class="row">
      <div class="item">
        <bs-input ref="nazevObce" :required="true" v-model="company.nazevObce" label="Obec"/>
      </div>
      <div class="item">
        <bs-input ref="nazevCastiObce" :required="true" v-model="company.nazevCastiObce" label="Část obce"/>
      </div>
      <div class="item">
        <bs-input ref="nazevUlice" :required="true" v-model="company.nazevUlice" label="Ulice"/>
      </div>
      <div class="item">
        <bs-input ref="cisloDomovni" :required="true" v-model="company.cisloDomovni" label="Číslo domu"/>
      </div>
      <div class="item">
        <bs-input v-model="company.nazevOkresu" label="Název okresu"/>
      </div>
    </div>

    <div class="row">
      <div class="item">
        <bs-input ref="email" :required="true" v-model="company.email" label="e-mail"/>
      </div>
      <div class="item">
        <bs-input ref="phoneNumber" :required="true" v-model="company.phoneNumber" label="Telefonní číslo"/>
      </div>
      <div class="item">
        <bs-input ref="bankAccountNumber" :required="true" v-model="company.bankAccountNumber" label="Číslo účtu"/>
      </div>
    </div>
    <div class="row">
      <div class="item">
        <bs-checkbox v-model="company.vatPayer" label="Plátce DPH"/>
      </div>
      <div class="item">
        <bs-checkbox v-model="company.primary" label="Primární účet"/>
      </div>
    </div>
  </div>


</template>

<script lang="ts" setup>

</script>

<style scoped>

</style>
