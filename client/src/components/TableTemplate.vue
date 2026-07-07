<script setup>
import { ref, computed, watch } from 'vue';

// 1) =============== INITIALIZATION   ===============
const props = defineProps({
  /**
   * Describe columns attribute: columns template
   * name: attribute name
   * field: support for sort function
   * label: label show on header of table
   * align: 'left' | 'center' | 'right' is align for table header
   * isHtml: using html mode of body columns (value required is html string)
   * isIcon: using link mode of body columns (value required is binding data)
   * isChip: using chip mode of body columns (value required is {label: chip's label; chipBind: binding data })
   * cellBind: binding columns's attribute
   */
  columns: {
    type: Array,
    required: true,
  },
  rows: {
    type: Array,
    required: true,
  },
  checkField: {
    type: String,
    default: null,
  },
  checkSelected: {
    type: Array,
    default: () => [],
  },
  radioField: {
    type: String,
    default: null,
  },
  radioSelected: {
    type: Number,
    default: null,
  },
  separator: {
    type: String,
    default: 'cell',
  },
  maxHeight: {
    type: String,
    default: null,
  },
  rowClick: {
    type: Function,
    default: null,
  },
});

const emits = defineEmits([
  'update:checkSelected',
  'update:radioSelected'
]);

// 2) =============== VARIABLE REF     ===============
const columns = computed(() => {
  if (props.checkField) {
    return [
      { name: 'select', label: '', field: 'select', align: 'center', headerStyle: 'width: 60px' },
      ...props.columns,
    ];
  }

  if (props.radioField) {
    return [
      { name: 'radio', label: '', field: 'radio', align: 'center', headerStyle: 'width: 60px' },
      ...props.columns,
    ];
  }

  return props.columns;
});

const checkSelectedModel = computed({
  get: () => props.checkSelected,
  set: (value) => emits('update:checkSelected', value),
});

const radioSelectedModel = computed({
  get: () => props.radioSelected,
  set: (value) => emits('update:radioSelected', value),
});

const selectAll = ref(false);

// 3) =============== METHOD/FUNCTION  ===============
const selectAllClick = () => {
  checkSelectedModel.value = selectAll.value ? [] : props.rows.map((x) => x[props.checkField || '']);
};

const rowClick = (row) => {
  if (props.rowClick) { props.rowClick(row); }
};

// 4) =============== VUE JS LIFECYCLE ===============
watch(checkSelectedModel, (v) => {
  if (v?.length === props.rows.length && v?.length > 0) { selectAll.value = true; }
  else if (v?.length === 0) { selectAll.value = false; }
  else { selectAll.value = null; }
}, { deep: true });
</script>

<template>
  <div class="custom-table">
    <q-table hide-bottom square bordered :columns="columns" :rows="rows" :separator="separator"
      :rows-per-page-options="[0]" class="thead-sticky" :style="{ maxHeight }">
      <template v-slot:header-cell="props">
        <q-th :props="props" class="tw:cursor-pointer">
          <span v-if="props.col.field === 'select' && checkField">
            <q-checkbox dense :model-value="selectAll" @click="selectAllClick" @keyup.enter="selectAllClick" />
          </span>
          <span v-else-if="props.col.field === 'radio' && radioField">
          </span>
          <span>{{ props.col.label }}</span>
        </q-th>
      </template>
      <template v-slot:body="props">
        <q-tr :props="props" @click="rowClick(props.row)">
          <q-td v-for="col in props.cols" :key="col.name">
            <div v-if="col.name === 'select'" class="tw:text-center">
              <q-checkbox dense color="lime-1" v-model="checkSelectedModel" :val="props.row[checkField || '']" />
            </div>
            <div v-else-if="col.name === 'radio'" class="tw:text-center">
              <q-radio dense color="lime-1" v-model="radioSelectedModel" :val="props.row[radioField || '']" />
            </div>
            <div v-else-if="col.isHtml" v-html="props.row[col.name]" v-bind="col.cellBind || {}"></div>
            <div v-else-if="col.isIcon" v-bind="col.cellBind">
              <q-icon v-bind="props.row[col.name]" />
            </div>
            <div v-else-if="col.isChip" v-bind="col.cellBind">
              <q-chip v-bind="props.row[col.name]?.chipBind">{{ props.row[col.name]?.label }}</q-chip>
            </div>
            <div v-else v-bind="col.cellBind || {}">{{ props.row[col.name] }}</div>
          </q-td>
        </q-tr>
      </template>
    </q-table>
  </div>
</template>

<style scoped lang="scss">
.custom-table {
  .thead-sticky {
    thead tr th {
      position: sticky;
      z-index: 1;
    }

    thead tr:first-child th {
      top: 0;
    }
  }

  :deep(.q-table thead tr) {
    height: 40px;
  }

  :deep(.q-table tbody td) {
    height: unset;
    padding: 5px 16px;
  }

  :deep(.q-table thead th) {
    background-color: $lime-1;
    border-color: $lime-6;
    border-bottom-color: $lime-1;
    color: white;
    font-size: 14px;
    font-weight: 600;
  }

  :deep(.q-table thead .q-checkbox__inner) {
    color: rgba(0, 0, 0, 0.54) !important; // show focus color

    .q-checkbox__bg {
      background-color: white;
      border-color: white;

      .q-checkbox__svg {
        color: $lime-1;
      }
    }
  }
}
</style>