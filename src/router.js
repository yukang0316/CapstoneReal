import { createRouter, createWebHashHistory } from 'vue-router';
import ReportingPage from './components/ReportingPage.vue';
import ListPage from './components/ListPage.vue';

const routes = [
  {
    path: '/',
    redirect: '/reporting'
  },
  {
    path: '/reporting',
    component: ReportingPage
  },
  {
    path: '/list',
    component: ListPage
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
  base: '/'
});

export default router;