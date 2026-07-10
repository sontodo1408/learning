import { createRouter, createWebHistory } from "vue-router";
import { ROUTER_NAME } from "@/helpers/const";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/admin",
      component: () => import("@/layouts/AdminLayout.vue"),
      children: [
        {
          path: "",
          name: ROUTER_NAME.HOME,
          component: () => import("@/views/admin/S0001_Home.vue"),
        },
        {
          path: "video-vocab",
          name: ROUTER_NAME.VIDEO_VOCAB,
          component: () => import("@/views/admin/S0002_VideoVocab.vue"),
        },
      ],
    },
    {
      path: "/study/:setId",
      component: () => import("@/layouts/StudyLayout.vue"),
      children: [
        {
          path: "flashcards",
          name: ROUTER_NAME.FLASHCARD,
          component: () => import("@/views/study/S0004_Flashcard.vue"),
        },
        {
          path: "learn",
          name: ROUTER_NAME.LEARN,
          component: () => import("@/views/study/S0005_Learn.vue"),
        },
        {
          path: "test",
          name: ROUTER_NAME.TEST,
          component: () => import("@/views/study/S0006_Test.vue"),
        },
      ],
    },
    {
      path: "/",
      component: () => import("@/layouts/BlankLayout.vue"),
      children: [
        {
          path: "login",
          name: ROUTER_NAME.LOGIN,
          component: () => import("@/views/S0003_Login.vue"),
        },
      ],
    },
  ],
});

export default router;