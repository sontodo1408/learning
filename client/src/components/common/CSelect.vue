<script setup>
import { isEqual } from '@/utilities/common';
import { ref, computed, watch, watchEffect, onMounted } from 'vue';

// 1) =============== INITIALIZATION   ===============
const props = defineProps({
  modelValue: {
    type: [String, Number, Object],
    default: null,
  },
  options: {
    type: Array,
    default: () => [],
  },
  outlined: {
    type: Boolean,
    default: true,
  },
  dense: {
    type: Boolean,
    default: true,
  },
  error: {
    type: Boolean,
    default: undefined,
  },
  optionLabel: Array,
  optionValue: String,
  displayValue: String,
  blankFirst: Boolean,
  filter: Boolean,
  multiple: Boolean,
  selectClass: String,
  disable: Boolean,
  optionDisable: String,
  errorMessage: String,
  label: String,
});
const emits = defineEmits(['update:modelValue', 'change']);

// 2) =============== VARIABLE REF     ===============
// 参照コンポーネント
const selectRef = ref(null);
// 選択されている選択肢
const selectedValue = ref(null);
const textSearch = ref('');
// フィルタリング後のオプション
const optionFiltered = ref([]);
const optionFilteredEmpty = ref(false);

// 選択された値に従って表示テキストを計算します //Calculate the displayed text according to the selected value
const displayText = computed(() => {
  if (!selectedValue.value) { return ''; }
  let result = props.displayValue ? selectedValue.value[props.displayValue] : selectedValue.value;
  // 複数選択可の場合、値は","区切りで表示する
  if (props.multiple) {
    result = selectedValue.value.map((x) => (props.displayValue ? x[props.displayValue] : x)).join(', ');
  }
  return result || '';
});
// props変換後の選択肢
const options = computed(() => {
  if (!props.options) { return []; }
  if (props.blankFirst) {
    const firstElement = '';
    return [firstElement, ...props.options];
  }
  return props.options;
});

// 3) =============== METHOD/FUNCTION  ===============
// コンポーネントの選択をクリックしたときのイベント // Event when clicking on select component
const selectClick = () => {
  if (props.filter) {
    textSearch.value = '';
    optionFiltered.value = options.value;
    optionFilteredEmpty.value = false;
  }
};

// 行の値を選択するイベント
const selectRowEvent = (value) => {
  let returnVal;
  // 複数選択可の場合
  if (props.multiple) {
    // 複数選択可の場合、返却値は","で区切る
    returnVal = props.optionValue ? value.map((x) => x[props.optionValue || '']) : value;
  } else {
    returnVal = props.optionValue ? value[props.optionValue] : value;
  }

  emits('update:modelValue', returnVal);
  emits('change', returnVal);
};

// 4) =============== VUE JS LIFECYCLE ===============
// フィルターされたオプションを初期化する / Init option filtered
watch(() => options.value, (value) => {
  optionFiltered.value = value;
});

// セレクトボックスに値をバインディングする処理
watchEffect(
  () => {
    // 複数選択可の場合
    if (props.multiple && props.modelValue?.length > 0) {
      // ","区切られている文字列から選択値一覧を取得する
      selectedValue.value = [];
      // 選択肢一覧内の項目を検索し、配列を順番にプッシュする
      props.modelValue.forEach((element) => {
        const option = options.value.find((x) => isEqual((props.optionValue ? x[props.optionValue] : x), element));
        if (option) { selectedValue.value.push(option); }
      });
    } else if (props.multiple) {
      selectedValue.value = [];
    } else if (props.optionValue) {
      // 単一選択可の場合
      selectedValue.value = options.value.find((x) => isEqual((props.optionValue ? x[props.optionValue] : x), props.modelValue));
    } else { selectedValue.value = props.modelValue; }
  },
);

// テキスト検索を入力するときにフィルターされたオプションを再構築する // Rebuild option filtered when entering text searches
watch(() => textSearch.value, (value) => {
  // 検索テキストが空の場合は何もしません // Do nothing
  if (!value) { optionFiltered.value = options.value; }
  // テキスト検索の小文字を計算する // Calculate lower case of text search
  const valLowerCase = value?.toLowerCase();
  // 空のフラグを再初期化する // Reinitialize the empty flag
  optionFilteredEmpty.value = false;

  if (props.optionLabel?.length) {
    // オプションラベルがある場合 // If there is optionLabel
    optionFiltered.value = options.value.filter((x) => {
      // optionLabel のすべての要素でフィルタリングする // Filter by all elements in optionLabel
      for (let index = 0; index < props.optionLabel?.length; index++) {
        const propValLowerCase = x[props.optionLabel[index] || '']?.toString()?.toLowerCase();
        if (propValLowerCase?.includes(valLowerCase)) { return true; }
      }
      return false;
    });
  } else {
    // オプションラベルがない場合 // If there is no optionLabel
    optionFiltered.value = options.value.filter((x) => {
      // 初期オプションでフィルタリングする// Filter by initial options
      const propValLowerCase = x?.toString()?.toLowerCase();
      return propValLowerCase?.includes(valLowerCase);
    });
  }

  // フィルタリングされたオプションに値がない場合 // In case the filtered option has no value
  if (!optionFiltered.value?.length) {
    // 空のフラグ値を true に設定します // Set the empty flag value to true
    optionFilteredEmpty.value = true;
    // 検索入力を表示するデフォルト値を追加 // Add default value to show search input
    optionFiltered.value = [{}];
  }
});

onMounted(() => {
  // Initialize the filtered option value in case the watch event is not catch
  optionFiltered.value = options.value;
});

</script>
<template>
  <div ref="selectRef" @click="selectClick()">
    <q-select :outlined="outlined" no-error-icon v-model="selectedValue" :options="optionFiltered"
      :display-value="displayText" :dense="dense" :multiple="multiple" :class="['select-component', selectClass]"
      @update:model-value="selectRowEvent" :disable="disable" :option-disable="optionDisable" :error="error"
      :error-message="errorMessage" :label="label" behavior="menu">
      <template v-slot:prepend>
        <slot name="prepend"></slot>
      </template>
      <template v-slot:option="scope">
        <q-item v-if="filter && scope.index === 0">
          <q-item-section>
            <q-input outlined v-model="textSearch" dense autofocus>
              <template v-slot:prepend>
                <q-icon name="search" />
                <div class="tw:mr-1"></div>
              </template>
            </q-input>
          </q-item-section>
        </q-item>
        <div v-if="optionFilteredEmpty" class="full-width row flex-center text-default q-gutter-sm tw:m-4">
          <span>Dữ liệu trống.</span>
        </div>
        <q-item v-else v-bind="scope.itemProps" :disable="!!optionDisable && scope.opt[optionDisable]">
          <q-item-section side v-if="multiple">
            <q-checkbox dense :model-value="selectedValue?.length > 0 && selectedValue.includes(scope.opt)"
              @update:model-value="scope.toggleOption(scope.opt)"></q-checkbox>
          </q-item-section>
          <q-item-section>
            <div v-if="optionLabel" class="flex items-center tw:space-x-5 ">
              <div v-for="label in optionLabel" :key="label">
                {{ scope.opt[label] }}
              </div>
            </div>
            <div v-else>{{ scope.opt }}</div>
          </q-item-section>
        </q-item>
      </template>
    </q-select>
  </div>
</template>
<style scoped lang="scss">
.select-component {
  :deep(.q-field__native span) {
    text-overflow: ellipsis;
    width: 100%;
    overflow: hidden;
    white-space: nowrap;
    line-height: 22px;
  }


  :deep(.q-field--dense .q-field__before),
  :deep(.q-field--dense .q-field__prepend) {
    padding-right: 0;
  }
}
</style>
