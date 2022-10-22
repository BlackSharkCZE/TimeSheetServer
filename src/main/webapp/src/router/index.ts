import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from '../views/Home.vue'
import {KeycloakInstance} from '@/plugins/KeycloakPlugin'
import PrivateHome from '@/views/private/PrivateHome.vue'
import ListCompanyView from '@/views/private/company/ListCompaniesView.vue'
import CreateCompanyView from '@/views/private/company/CreateCompanyView.vue'
import CreateRateView from '@/views/private/rate/CreateRateView.vue'
import ListRateView from '@/views/private/rate/ListRateView.vue'
import CreateRequisitionView from '@/views/private/requisition/CreateRequisitionView.vue'
import ListRequisitionView from '@/views/private/requisition/ListRequisitionView.vue'
import CreateProjectView from '@/views/private/project/CreateProjectView.vue'


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
                path: 'rate',
                children: [
                    {
                        path: 'create',
                        component: CreateRateView
                    },
                    {
                        path: 'list',
                        component: ListRateView
                    },

                ]
            },
            {
                path: 'companies',
                children: [
                    {
                        path: 'create',
                        component: CreateCompanyView
                    },
                    {
                        path: 'list',
                        component: ListCompanyView
                    }
                ]
            },
            {
                path: 'requisition',
                children: [
                    {
                        path: 'create',
                        component: CreateRequisitionView
                    },
                    {
                        path: 'list',
                        component: ListRequisitionView
                    },

                ]
            },
            {
                path: 'project',
                children: [
                    {
                        path: 'create',
                        component: CreateProjectView
                    }
                ]
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