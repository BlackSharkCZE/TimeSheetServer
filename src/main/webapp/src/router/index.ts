import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from '../views/Home.vue'
import {KeycloakInstance} from '@/plugins/KeycloakPlugin'
import PrivateHome from '@/views/private/PrivateHome.vue'
import DashboardView from '@/views/private/dashboard/DashboardView.vue'
import CompanyView from '@/views/private/company/CompanyView.vue'
import RequistionView from '@/views/private/requisition/RequistionView.vue'
import ProjectView from '@/views/private/project/ProjectView.vue'
import RateView from '@/views/private/rate/RateView.vue'


const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        component: Home
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
        meta: {
            authRequired: true
        },
        children: [
            {
                path: 'home',
                component: PrivateHome,
            },
            {
                path: 'dashboard',
                component: DashboardView
            },
            {
                path: 'rate',
                component: RateView
            },
            {
                path: 'companies',
                component: CompanyView
            },
            {
                path: 'requisition',
                component: RequistionView
            },
            {
                path: 'project',
                component: ProjectView
            }
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

router.beforeEach(async (to, from, next) => {
    if (to.meta.authRequired == true) {
        if (await isAuthenticated()) {
            next()
        } else {
            const path = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + to.fullPath
            KeycloakInstance.login({
                redirectUri: path
            })
            next()
        }
    } else {
        next()
    }
})


export default router
