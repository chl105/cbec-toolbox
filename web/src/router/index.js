import Vue from "vue";
import Router from "vue-router";
import Login from "@/page/Login";
import Admin from "@/page/Admin";
import Trade from "@/page/Trade";
import store from "@/store/index";

Vue.use(Router);

const routes = [{
    path: "/login",
    component: Login,
  },
  {
    path: "/",
    component: Trade,
  },
  {
    path: "/trade",
    component: Trade,
    children: [{
        path: "",
        redirects: "cost-calculator",
      },
      {
        path: "cost-calculator",
        meta: {
          key: "cost-calculator",
        },
        component: () => import("@/page/CostCalculator"),
      },
      {
        path: "logistics",
        meta: {
          key: "logistics",
        },
        component: () => import("@/page/Logistics"),
      },
      {
        path: "auto-select-product",
        meta: {
          key: "auto-select-product"
        },
        component: () => import("@/page/AutoSelectProduct")
      },
    ],
  },
  {
    path: "/admin",
    component: Admin,
    children: [{
        path: "/",
        redirects: "user-manage",
      },
      {
        path: "user-manage",
        meta: {
          key: "user-manage",
        },
        component: () => import("@/page/UserManage"),
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