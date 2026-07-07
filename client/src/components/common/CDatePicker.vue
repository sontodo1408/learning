<script setup>
import dayjs from '@/utilities/day';
import { ref, watch, watchEffect } from 'vue';

// 1) =============== INITIALIZATION   ===============
/** コンポーネントのプロパティ */
const props = defineProps({
  // セレクトボックスにバインディングされる値
  modelValue: {
    type: String,
    default: '',
  },
  // 最大日付しきい値
  maxDate: String,
  // 最小日付しきい値
  minDate: String,
});
/** プラグインを発行する */
const emits = defineEmits(['update:modelValue']);

// 2) =============== VARIABLE REF     ===============
/** プロキシを表示するかどうかを確認します */
const popupProxy = ref();
/** 日時値 */
const dateValue = ref();

// 3) =============== METHOD/FUNCTION  ===============
/**
 * プロキシチェックを非表示
 */
const hidePopupProxy = () => {
  popupProxy.value = false;
};

/**
 * フィルター日を選択可能
 */
const optionsPicker = (date) => {
  const { minDate, maxDate } = props;

  const isLimitMin = !!minDate; // minDateが存在するか確認する
  const isLimitMax = !!maxDate; // maxDateが存在するか確認する

  if (isLimitMin && isLimitMax) {
    return date >= minDate && date <= maxDate;
  }
  if (isLimitMin) {
    return date >= minDate;
  }
  if (isLimitMax) {
    return date <= maxDate;
  }

  // 条件は適用されません。すべての日付を返します
  return true;
};
// 4) =============== VUE JS LIFECYCLE ===============
watchEffect(() => {
  const value = dayjs(props.modelValue).format('YYYY/MM/DD');
  if (props.modelValue && value === props.modelValue) {
    dateValue.value = value;
  } else {
    dateValue.value = props.modelValue;
  }
});

watch(dateValue, (value) => {
  hidePopupProxy();
  emits('update:modelValue', value);
});
</script>
<template>
  <q-input dense outlined v-model="dateValue" mask="date">
    <template v-slot:append>
      <q-icon name="calendar_today" class="cursor-pointer">
        <q-popup-proxy v-model="popupProxy" cover transition-show="scale" transition-hide="scale" class="tw-flex">
          <q-date v-model="dateValue" mask="YYYY/MM/DD" color="secondary" :options="optionsPicker" />
        </q-popup-proxy>
      </q-icon>
    </template>
  </q-input>
</template>
