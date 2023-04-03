<template>
    <Panel header="Podklady daňového přiznání" class="mt-2 mb-2">
        <form @submit.prevent="handleSubmit(!v$.$invalid)" class="p-fluid">

            <div class="card">
                <div class="formgrid grid">

                    <company-field id="company"
                                   class="col-2"
                                   v-model="v$.company.$model"
                                   :vualidate="v$.company"
                                   :submitted="submitted"
                                   label="Společnost"></company-field>

                    <input-field id="year"
                                 class="field col-2"
                                 label="Rok"
                                 v-model="v$.year.$model"
                                 :submitted="submitted"
                                 :vualidate="v$.year"/>

                    <div class="field col-1">
                        <label>&nbsp;</label>
                        <Button type="submit" label="Generovat"/>
                    </div>
                </div>
            </div>
        </form>
    </Panel>

    <InvoiceListPreview :invoices="invoices" @GenerateBulkRequest="generateBulkForCurrent"></InvoiceListPreview>

</template>

<script lang="ts" setup>

import Button from "primevue/button";
import CompanyField from "@/components/blocks/CompanyField.vue";
import InputField from "@/components/blocks/InputField.vue";
import Panel from "primevue/panel";
import {inject, reactive, ref} from "vue";
import {AxiosStatic} from "axios";
import {Company, InvoicePreview} from "@/components/blocks/Types";
import useVuelidate from "@vuelidate/core";
import {required} from "@vuelidate/validators";
import InvoiceListPreview from "@/views/private/tax/InvoiceListPreview.vue";

// Define types
type FormData = {
    year: number | null
    company: Company | null
}

const rules = {
    company: {required},
    year: {required},
}

// Define model
const model = reactive<FormData>({
    year: 2022,
    company: null
});

const invoices = ref<InvoicePreview[]>([])
const submitted = ref<boolean>(false)

// Inject dependencies
const axios = inject<AxiosStatic>('axios')

//Define used properties
const v$ = useVuelidate(rules, model)


// Define functions
function handleSubmit(valid: boolean) {
    submitted.value = true
    if (valid) {
        const url = `/dph/tax-report/${model.year}/${model.company.id}`
        axios?.get(url).then(response => {
            if (response.status >= 200 && response.status <= 299) {
                invoices.value = response.data.invoices
            } else {
                console.error('Tax Report error', response)
            }
        })
    }
}

function generateBulkForCurrent() {
    console.log('Generate bulk for ', model)
    axios?.get(`/dph/download/tax-report/${model.year}/${model.company.id}`, {
        responseType: 'blob'
    }).then(response => {

        const objectURL = URL.createObjectURL(response.data)
        const link = document.createElement('a')
        link.href = objectURL
        link.setAttribute('download', `${model.company.ic}-tax-${model.year}` + '.zip')
        document.body.appendChild(link)
        link.click()
        link.remove()
        window.URL.revokeObjectURL(link.href)
    })
}

</script>

<style scoped>

</style>
