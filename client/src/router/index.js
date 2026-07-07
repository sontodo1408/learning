import { createRouter, createWebHistory } from "vue-router";
import { ROUTER_NAME } from "@/helpers/const";
import AdminLayout from "@/layouts/AdminLayout.vue";
import BlankLayout from "@/layouts/BlankLayout.vue";
import HomeView from "@/views/HomeView.vue";
import LoginPage from "@/views/LoginPage.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      component: AdminLayout,
      children: [
        {
          path: "",
          name: ROUTER_NAME.HOME,
          component: HomeView,
        },
      ],
    },
    {
      path: "/",
      component: BlankLayout,
      children: [
        {
          path: "login",
          name: ROUTER_NAME.LOGIN,
          component: LoginPage,
        },
      ],
    },
  ],
});

export default router;