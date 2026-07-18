import { createRouter, createWebHistory } from "vue-router";
import { ROLE, ROUTER_NAME } from "@/helpers/const";
import { useAuthStore } from "@/stores/auth-store";
import authService from "@/services/auth-service";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/admin",
      component: () => import("@/layouts/AdminLayout.vue"),
      // Only ROLE_ADMIN users may enter any /admin screen; enforced in the beforeEach guard below
      meta: { requiresAdmin: true },
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
      component: () => import("@/layouts/UserLayout.vue"),
      children: [
        {
          path: "",
          component: () => import("@/views/study/S0008_Study.vue"),
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
      ],
    },
    {
      path: "/",
      component: () => import("@/layouts/UserLayout.vue"),
      children: [
        {
          path: "",
          component: () => import("@/views/user/S0007_UserHome.vue"),
          children: [
            {
              path: "",
              name: ROUTER_NAME.USER_HOME,
              component: () => import("@/views/user/S0009_Dashboard.vue"),
            },
            {
              path: "search",
              name: ROUTER_NAME.USER_SEARCH,
              component: () => import("@/views/user/S0010_Search.vue"),
            },
            {
              path: "my-study-sets",
              name: ROUTER_NAME.USER_MY_STUDY_SETS,
              component: () => import("@/views/user/S0011_MyStudySets.vue"),
              // Only a signed-in user has study sets of their own; enforced in the beforeEach guard below
              meta: { requiresAuth: true },
            },
          ],
        },
      ],
    },
    {
      path: "/login",
      component: () => import("@/layouts/BlankLayout.vue"),
      children: [
        {
          path: "",
          name: ROUTER_NAME.LOGIN,
          component: () => import("@/views/S0003_Login.vue"),
        },
      ],
    },
    {
      path: "/:pathMatch(.*)*",
      component: () => import("@/layouts/BlankLayout.vue"),
      children: [
        {
          path: "",
          name: ROUTER_NAME.NOT_FOUND,
          component: () => import("@/views/S0012_NotFound.vue"),
        },
      ],
    },
  ],
});

// After a page refresh, the JWT survives (localStorage) but the Pinia user profile does
// not: re-fetch the profile once before the first route renders so the user stays signed in.
router.beforeEach(async (to) => {
  const authStore = useAuthStore();

  if (authStore.token && !authStore.user) {
    try {
      await authService.checkLogin();
    } catch {
      // rest-client already signs the user out on a 401; nothing else to do here
    }
  }

  // Screens marked requiresAuth need a signed-in user, regardless of role
  if (to.matched.some((record) => record.meta.requiresAuth) && !authStore.isLoggedIn) {
    // Carry the originally requested URL along so the login screen can return here afterwards
    return { name: ROUTER_NAME.LOGIN, query: { redirect: to.fullPath } };
  }

  // /admin screens require a signed-in ROLE_ADMIN user; every other screen stays public
  if (to.matched.some((record) => record.meta.requiresAdmin)) {
    if (!authStore.isLoggedIn) {
      // Carry the originally requested URL along so the login screen can return here afterwards
      return { name: ROUTER_NAME.LOGIN, query: { redirect: to.fullPath } };
    }
    if (authStore.user?.role !== ROLE.ADMIN) {
      return { name: ROUTER_NAME.USER_HOME };
    }
  }
});

export default router;