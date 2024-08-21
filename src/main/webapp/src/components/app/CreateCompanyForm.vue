<template>
    <Panel header="Create company" :toggleable="true" :collapsed="collapsed"  class="mt-2">
        <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

            <div class="card">
                <div class="formgrid grid">

                    <div class="field col-1">
                        <label for="ic">IČ</label>
                        <div class="p-inputgroup" id="ic">
                            <InputText
                                    :class="{'p-invalid':v$.ic.$invalid}"
                                    v-model="v$.ic.$model"/>
                            <Button icon="pi pi-search" class="p-button-warning" @click="loadDataFromAres"/>
                        </div>
                        <small v-if="v$.ic.$invalid && submitted"
                               class="p-error">{{ v$.ic.required.$message.replace('Value', 'IC') }}</small>
                    </div>

                    <input-field id="companyName"
                                 class="col-2"
                                 label="Company Name 2"
                                 :vualidate="v$.companyName"
                                 :submitted="submitted"
                                 v-model="formData.companyName">
                    </input-field>

                  <div :class="props.class">
                    <label for="dic">DIC</label>
                    <InputText
                        id="dic"
                        class="p-inputtext"
                        v-model="formData.dic"/>
                  </div>

                    <div class="field col-1">
                        <label>&nbsp;</label>
                        <div class="mt-3">
                            Platce DPH: &nbsp;<InputSwitch v-model="formData.platceDph"/>
                        </div>
                    </div>
                </div>

                <div class="formgrid grid">
                    <input-field id="okres"
                                 class="col-2"
                                 label="Okres"
                                 :vualidate="v$.okres"
                                 :submitted="submitted"
                                 v-model="formData.okres"/>

                    <input-field id="obec"
                                 class="col-2"
                                 label="Obec"
                                 :vualidate="v$.obec"
                                 :submitted="submitted"
                                 v-model="formData.obec"/>

                    <input-field id="castObce"
                                 class="col-2"
                                 label="Cast obce"
                                 :vualidate="v$.castObce"
                                 :submitted="submitted"
                                 v-model="formData.castObce"/>
                    <input-field id="ulice"
                                 class="col-2"
                                 label="Ulice"
                                 :vualidate="v$.ulice"
                                 :submitted="submitted"
                                 v-model="formData.ulice"/>

                    <input-field id="cisloDomu"
                                 class="col-1"
                                 label="Cislo Domu"
                                 :vualidate="v$.cisloDomu"
                                 :submitted="submitted"
                                 v-model="formData.cisloDomu"/>
                  <input-field id="PSC"
                               class="col-1"
                               label="PSČ"
                               :vualidate="v$.psc"
                               :submitted="submitted"
                               v-model="formData.psc"/>

                </div>

                <div class="formgrid grid">
                    <input-field id="email"
                                 class="col-2"
                                 label="E-mail"
                                 :vualidate="v$.email"
                                 :submitted="submitted"
                                 v-model="formData.email"/>

                    <input-field id="phone"
                                 class="col-2"
                                 label="Phone"
                                 :vualidate="v$.phone"
                                 :submitted="submitted"
                                 v-model="formData.phone"/>

                    <input-field id="bankAccountNumber"
                                 class="col-2"
                                 label="Cislo uctu"
                                 :vualidate="v$.bankAccountNumber"
                                 :submitted="submitted"
                                 v-model="formData.bankAccountNumber"/>

                    <div class="field col-1">
                        <label>&nbsp;</label>
                        <Button type="submit" label="Create" class="p-button-lg"/>
                    </div>
                </div>
            </div>
        </form>

    </Panel>

</template>

<script lang="ts" setup>

import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import Panel from 'primevue/panel'
import InputSwitch from 'primevue/inputswitch'

import {AxiosStatic} from "axios";
import {inject, reactive, ref, withDefaults, defineEmits, defineProps} from "vue";
import useVuelidate from "@vuelidate/core";
import {email, required, requiredIf} from "@vuelidate/validators";
import InputField from "@/components/blocks/InputField.vue";
import {useUserStore} from "@/stores/UserStore";

// Define types
type ComponentProps = {
    collapsed?: boolean
}

type FormData = {
    ic: string | null
    companyName: string | null,
    dic: string | null,
    platceDph: boolean,
    okres: string | null,
    obec: string | null,
    castObce: string | null,
    ulice: string | null,
    cisloDomu: string | null,
    psc: string | null,
    email: string | null,
    phone: string | null,
    bankAccountNumber: string | null
}

// Define component properties
const props = withDefaults(defineProps<ComponentProps>(), {
    collapsed: true
})


// Define component data
const formData = reactive<FormData>({
    ic: '71857001',
    companyName: null,
    dic: null,
    platceDph: false,
    okres: null,
    obec: null,
    castObce: null,
    ulice: null,
    cisloDomu: null,
    psc: null,
    email: null,
    phone: null,
    bankAccountNumber: null
})

const aresLoading = ref<boolean>(false)
const submitted = ref<boolean>(false)

const rules = {
    // ic: {required: helpers.withMessage('IC je povinny parametr.', required), minLength: minLength(2)}
    ic: {required},
    companyName: {required, $autoDirty: true},
    okres: {required},
    obec: {required},
    castObce: {required},
    ulice: {required},
    cisloDomu: {required},
    psc: {required},
    email: {required, email},
    phone: {required},
    bankAccountNumber: {required}
}

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

// Define used properties
const v$ = useVuelidate(rules, formData)
const userStore = useUserStore()

// Define emits
const emits = defineEmits(['companyCreated'])

// Define functions
function handleSubmit(isFormValid: boolean) {
    submitted.value = true
    if (isFormValid) {
        saveCompany()
    }else {
      console.error('Form is not valid!', v$)
    }
}

function saveCompany() {
    const primary = !userStore.hasPrimaryCompany
    let path = "/company"
    if (primary) {
        path = "/company/primary"
    }
    axios?.post(path, buildData()).then((response) => {
        if (response.status >= 200 && response.status <= 299) {
            if (primary)
                userStore.setCompany(response.data)
            emits('companyCreated', response.data)
            resetForm()
        } else {
            // message.error('Ulozeni spolecnosti se nezdarilo!')
            console.error(response)
        }
    })
}

function resetForm() {
    formData.ic = null
    formData.companyName = null
    formData.dic = null
    formData.okres = null
    formData.obec = null
    formData.castObce = null
    formData.ulice = null
    formData.cisloDomu = null
    formData.psc = null
    formData.email = null
    formData.phone = null
    formData.bankAccountNumber = null
    submitted.value = false
}

function buildData(): any {

    const primary = !userStore.hasPrimaryCompany

    return {
        ic: formData.ic,
        companyName: formData.companyName,
        dic: formData.dic,
        okres: formData.okres,
        obec: formData.obec,
        castObce: formData.castObce,
        ulice: formData.ulice,
        cisloDomu: formData.cisloDomu,
        ps: formData.psc,
        email: formData.email,
        phoneNumber: formData.phone,
        bankAccountNumber: formData.bankAccountNumber,
        platceDph: formData.platceDph,
        primaryAccount: primary
    }
}

function loadDataFromAres() {
    aresLoading.value = true
    axios?.get(`/ares/` + formData.ic).then(response => {
        aresLoading.value = false
        if (response.status === 200) {
            formData.ic = response.data.ic
            formData.companyName = response.data.companyName
            formData.dic = response.data.dic
            formData.platceDph = response.data.platceDph
            formData.okres = response.data.okres
            formData.obec = response.data.obec
            formData.castObce = response.data.castObce
            formData.ulice = response.data.ulice
            formData.cisloDomu = response.data.cisloDomu
            formData.psc = response.data.psc
        } else {
            console.error('Can not load ARES info for the IC', response)
        }
    })
}
</script>

<style scoped>

</style>
