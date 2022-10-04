<template>

  <div>

    <bs-data-table
        label="TimeSheet"
        @item-selected="itemSelectedHandler"
        sort="id"
        resource-url="http://localhost:8882/datatable/timeline"
        :page-size="25"
        :definition="definition"
        :initial-filter="filter">
      <template #remoteWriters="{row}">
        {{ getWriters(row) }}
      </template>

      <template #NotExistingKey="{row}">

        <button @click="processRowClick(row)">Select</button>

      </template>

    </bs-data-table>
  </div>

</template>

<script lang="ts" setup>
import BsDataTable from '@/components/blackshark/BsDataTable.vue'
import TimelineTableFields from "./def/TimelineTableFields.js"

const filter = {
  column: "ID"
}

const definition = TimelineTableFields

function processRowClick(row: any) {
  console.log('Click to row: ', row)
}

function getWriters(row: any) {
  return row.writers.join(" | ")

}

function itemSelectedHandler() {
  alert('Item selected handler invoked!')
}

</script>

<style scoped lang="scss">
.wrote {
  color: #42b983;
}

.waiting {
  color: darkred;
}
</style>
