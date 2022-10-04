import {createRouter, createWebHistory, NavigationGuardWithThis, RouteRecordRaw} from 'vue-router'
import Home from '../views/Home.vue'
import PrivateLayout from '../views/private/PrivateLayout.vue'
import PrivateHome from '@/views/private/PrivateHome.vue'
import TodoList from '@/views/private/TodoList.vue'
import DataTableView from '@/views/DataTableView.vue'
import Vue, {App} from "vue";
import { KeycloakInstance } from '@/plugins/KeycloakPlugin'


const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },
    {
      path: '/dev',
      name: 'DevPage',
      component: DataTableView
    },
    {
        path: '/about',
        name: 'About',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
    },
    {
        path: '/private',
        name: 'privateLayout',
        component: PrivateLayout,
        meta: {
            authRequired: true
        },
        children: [
            {
                path: 'home',
                component: PrivateHome,
            },
            {
                path: 'todo-list',
                component: TodoList
            },
        ]
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

async function isAuthenticated() {
    return KeycloakInstance.authenticated
}

router.beforeEach(async( to, from, next) => {
    if (to.meta.authRequired == true) {
        if (await isAuthenticated()) {
            next()
        } else {
            KeycloakInstance.login()
            next()
        }
    } else {
        next()
    }
})


export default router
