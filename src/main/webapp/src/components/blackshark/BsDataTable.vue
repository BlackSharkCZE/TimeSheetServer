<template>

  <div v-if="loading">
    <bs-loading/>
  </div>

  <div v-else>

    <div class="bs-table">
      <div class="bs-table-label">{{props.label || props.resourceUrl}}</div>
      <table class="bs-table">
        <thead>
        <tr>
          <th v-for="th in props.definition" v-bind:key="th.key">
            {{ th.label }}
          </th>
        </tr>

        <tr>
          <th v-for="th in props.definition" v-bind:key="th.key">
            <div v-if="th.filter !== undefined">
              <input type="text" />
            </div>
          </th>
        </tr>
        </thead>

        <tbody>
        <tr v-for="(row, index) in rows" v-bind:key="row.id" :class="{'even': index%2===0, 'odd': index%2>0}">
          <td v-for="th in props.definition" v-bind:key="th.key">
            <slot :name="th.key" :value="row[th.key]" :row="row">
              {{ processCell(row, th) }}
            </slot>
          </td>
        </tr>
        </tbody>

        <tfoot v-if="paginator.pageCount>0">
        <tr>
          <td :colspan="props.definition.length">

            <div class="bs-table-navigation">
              <div class="item left">
                left info
              </div>
              <div class="item middle">
                <ul class="navigation">
                  <li v-for="page in pageList"
                      @click="changePage(page)"
                      :class="{current: page==currentPage}"
                      v-bind:key="page"> {{ page+1 }}
                  </li>
                </ul>
              </div>
              <div class="item right">
                right info
              </div>
            </div>
          </td>
        </tr>
        </tfoot>
      </table>
    </div>
    <button @click="emitCustomSignal()">Emit signal</button>

  </div>

</template>

<script lang="ts" setup>

// Define types for BsDataTable
import {computed, defineEmits, defineProps, inject, onMounted, ref, unref, withDefaults} from "vue";
import {AxiosStatic} from "axios";
import BsLoading from "@/components/blackshark/BsLoading.vue";
import moment from "moment";

type BsDataTableProps = {
  initialFilter: any
  pageSize?: number,
  resourceUrl: string,
  sort: string,
  autofocus?: boolean,
  definition: any,
  label?: string
}

// Define available signal
const emits = defineEmits(['itemSelected', 'displayDetail'])

// Define properties of the component
const props = withDefaults(defineProps<BsDataTableProps>(), {
  pageSize: 25,
  autofocus: false,
  initialFilter: {} as any
})

// Inject required services
const axios = inject<AxiosStatic>('axios')

// define private data
const rows = ref<string[]>([])
const paginator = ref<any>({})
const loading = ref<boolean>(true)
const currentPage = ref<number>(0)

// Define computed values
const pageList = computed(() => buildNavigationIndex())

// Define lifecycle hooks
onMounted(() => {
  loadData()
})

// Define functions

function changePage(page: number) {
  if (page < paginator.value.pageCount && page >= 0) {
    currentPage.value = page
    loadData()
  }
}

function emitCustomSignal() {
  emits('itemSelected')
}

function processCell(row: any, definition: any): string {
  if (row[definition.key] === undefined) {
    return "KEY DOES NOT EXISTS"
  } else {
    if (definition.format) {
      return processCellFormat(row, definition)
    } else {
      return row[definition.key]
    }
  }
}

function processCellFormat(row: any, definition: any): string {
  switch (definition.format.type) {
    case 'date':
      return processCellFormatDate(row, definition)
    default:
      return row[definition.key]
  }
}

function processCellFormatDate(row: any, definition: any): string {
  return moment(row[definition.key]).format(definition.format.pattern)
}

function buildNavigationIndex(): number[] {
  const pg = unref(paginator)
  const res: number[] = []
  if (pg.pageCount > 10) {
    for (let i = 0; i < pg.pageCount; i++) {
      if (i > pg.currentPage - 7 && i < pg.currentPage + 7) {
        res.push(i)
      }
    }
  } else {
    for (let i = 0; i < pg.pageCount; i++) {
      res.push(i)
    }
  }
  return res
}


function loadData() {
  loading.value = true
  const params = `?pageSize=${props.pageSize}&sort=${props.sort}&page=${currentPage.value}`
  axios?.post(props.resourceUrl + params, {}, {})
      .then(response => {
        if (response.status == 200) {
          paginator.value = response.data.paginator
          rows.value = response.data.rows
        } else {
          console.error('Can not read data from address', response.statusText, response)
        }
        loading.value = false
      })
}

</script>

<style scoped lang="scss">
@import "./styles/components";
</style>
