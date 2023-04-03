<template>
    <Panel header="Balance Overview" class="mt-2" v-if="props.invoices.length>0">

        <DataTable
                headerStyle="color:red !important"
                ref="dt"
                dataKey="id"
                :value="props.invoices"
                :paginator="false"
                :rowHover="true"
                :totalRecords="props.invoices.length">

            <Column field="invoiceNumber" header="Číslo faktury"/>

            <Column header="Vydal">
                <template #body="{data}">{{ data.issuer.companyName }}</template>
            </Column>

            <Column header="Příjemce">
                <template #body="{data}">{{ data.recipient.companyName }}</template>
            </Column>

            <Column header="Částka">
                <template #body="{data}">{{ formatPrice(data.paymentSumWithoutVat) }}</template>
            </Column>

            <Column header="Částka vč. DPH">
                <template #body="{data}">{{ formatPrice(data.paymentSum) }}</template>
            </Column>
        </DataTable>


        <div class="grid">
            <div class="col-10">
                <div>
                    <span class="text-600">Celkem včetně DPH: </span>
                    <span class="text-900 font-medium text-xl mb-2">{{totalWithVat}}</span>
                </div>
                <div>
                    <span class="text-600">Celkem bez DPH: </span>
                    <span class="text-900 font-medium text-xl mb-2">{{totalWithoutVat}}</span>
                </div>
            </div>
            <div class="col-2 text-right">
                <Button @click="generateBulk" icon="pi pi-download" label="Generovat zip"></Button>
            </div>
        </div>
    </Panel>

</template>

<script lang="ts" setup>
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import {computed, defineProps, defineEmits} from "vue";
import {InvoicePreview} from "@/components/blocks/Types";
import {formatPrice} from "@/services/FormatService";
import Panel from "primevue/panel";
import Button from "primevue/button";

// Define types
interface Properties {
    invoices: InvoicePreview[],
}

// Define props
const props = defineProps<Properties>()
const emits = defineEmits(['GenerateBulkRequest']);

const totalWithoutVat = computed(() => {
    if (props.invoices.length > 0) {
        return formatPrice(props.invoices.reduce((curr, acc) => acc["paymentSumWithoutVat"] + curr, 0))
    } else {
        return 0
    }
})

const totalWithVat = computed(() => {
    if (props.invoices.length > 0) {
        return formatPrice(props.invoices.reduce((curr, acc) => acc["paymentSum"] + curr, 0))
    } else {
        return 0
    }
})


function generateBulk() {
    emits('GenerateBulkRequest');
}

</script>

<style scoped>

</style>
