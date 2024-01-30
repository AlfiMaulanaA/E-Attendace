import { createRouter, createWebHistory } from "vue-router";

import User from "../views/User.vue";
import Matkul from "../views/MataKuliah.vue";
import Home from "../views/Home.vue";
import Absen from "../views/Absen.vue";
import Test from "../views/Test.vue";

const routes = [
  {
    path: "/",
    component: User,
  },
  {
    path: "/matkul",
    component: Matkul,
  },
  {
    path: "/home",
    component: Home,
  },
  {
    path: "/absen",
    component: Absen,
  },
  {
    path: "/test",
    component: Test,
  },
];

// Router Creation
const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
