import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import router from "./router";
import i18n from "@/i18n";

import "@/assets/css/tailwind.css";

import { Quasar } from "quasar";
import "quasar/src/css/index.sass";
import "@quasar/extras/material-icons/material-icons.css";

import "@/assets/css/main.scss";

import CBtn from "@/components/common/CBtn.vue";
import CDatePicker from "@/components/common/CDatePicker.vue";
import CSelect from "@/components/common/CSelect.vue";

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(i18n);
app.use(Quasar, {
  plugins: {},
});

app.component("CBtn", CBtn);
app.component("CDatePicker", CDatePicker);
app.component("CSelect", CSelect);

app.mount("#app");
