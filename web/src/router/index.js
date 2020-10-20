import Vue from "vue";
import Router from "vue-router";
import store from "@/store/index";

Vue.use(Router);

const routes = [
  {
    path: "/",
    redirect: "/cbec",
  },
  {
    path: "/login",
    component: () => import("@/page/login/Index"),
  },
  {
    path: "/cbec",
    component: () => import("@/page/cbec/Index"),
    children: [
      {
        path: "cost-calculator",
        meta: {
          key: "cost-calculator",
        },
        component: () => import("@/page/cbec/CostCalculator"),
      },
      {
        path: "logistics",
        meta: {
          key: "logistics",
        },
        component: () => import("@/page/cbec/Logistics"),
      },
      {
        path: "select-goods",
        meta: {
          key: "select-goods",
        },
        component: () => import("@/page/cbec/SelectGoods"),
      },
    ],
  },
  {
    path: "/user",
    component: () => import("@/page/user/Index"),
    children: [
      {
        path: "/",
        redirect: "user-manage",
      },
      {
        path: "user-manage",
        meta: {
          key: "user-manage",
        },
        component: () => import("@/page/user/UserManage"),
      },
      {
        path: "platform-account-manage",
        meta: {
          key: "platform-account-manage",
        },
        component: () => import("@/page/user/PlatformAccountManage"),
      },
    ],
  },
];

const router = new Router({
  routes,
});

router.beforeEach((to, from, next) => {
  if (store.state.token) {
    next();
  } else {
    if (to.path == "/login") {
      next();
    } else {
      next("/login");
    }
  }
});

export default router;
