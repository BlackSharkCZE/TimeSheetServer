import {createRouter, createWebHistory, RouteLocationRaw, RouteRecordRaw} from 'vue-router'
import PrivateHome from '@/views/private/PrivateHome.vue'
import DashboardView from '@/views/private/dashboard/DashboardView.vue'
import CompanyView from '@/views/private/company/CompanyView.vue'
import RequisitionView from '@/views/private/requisition/RequisitionView.vue'
import ProjectView from '@/views/private/project/ProjectView.vue'
import RateView from '@/views/private/rate/RateView.vue'
import RemoteWritersView from '@/views/private/writers/RemoteWritersView.vue'
import InvoiceView from '@/views/private/invoices/InvoiceView.vue'
import InvoiceDetail from '@/views/private/invoices/InvoiceDetail.vue'
import BillingView from '@/views/private/billing/BillingView.vue'
import AddInvoiceView from '@/views/private/invoices/AddInvoiceView.vue'
import DphView from '@/views/private/dph/DphView.vue'
import StatisticView from "@/views/private/statistic/StatisticView.vue";
import PaymentsView from "@/views/private/payments/PaymentsView.vue";
import TaxView from "@/views/private/tax/TaxView.vue";
import LoginView from "@/views/LoginView.vue";

import {UserDetail, useUserStore} from "@/stores/UserStore";
import {Exception} from "sass";
import NotFoundView from "@/views/NotFoundView.vue";

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        component: DashboardView,
        meta: {
            authRequired: true
        }
    },
    {
        path: '/login',
        component: LoginView
    },
    {
        path: '/about',
        name: 'About',
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
                path: 'tax',
                component: TaxView
            },
            {
                path: 'dph',
                component: DphView
            },
            {
                path: 'home',
                component: PrivateHome,
            },
            {
                path: 'invoices',
                component: InvoiceView,
            },
            {
                path: "invoices/:number",
                name: "invoiceDetail",
                component: InvoiceDetail
            },
            {
                path: "invoices-add",
                component: AddInvoiceView
            },
            {
                path: 'dashboard',
                name: 'dashboard',
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
                component: RequisitionView
            },
            {
                path: 'project',
                component: ProjectView
            },
            {
                path: 'writers',
                component: RemoteWritersView
            },
            {
                path: 'billing',
                component: BillingView
            },
            {
                path: 'money-flow',
                component: StatisticView
            },
            {
                path: 'payments',
                component: PaymentsView
            },
        ]
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        redirect: '/private/dashboard',
        // component: NotFoundView
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

async function isAuthenticated() {
    const userStore = useUserStore()
    return userStore.userDetail.userName !== undefined && userStore.userDetail.userName !== 'anonymous'
}

async function loadUserData() {
    const res = await fetch("/user/current")
    const json = await res.json()
    if (json.success === false) throw new Error("Loading user data failed. Success false!")
    const userStore = useUserStore()
    userStore.storeUser({
        userName: json.login,
        roles: json.roles,
        company: json.companyId
    } as UserDetail)
}

router.beforeEach(async (to, from, next) => {
    if (to.meta.authRequired == true) {
        console.log("AuthRequired for this endpoint!")
        if (await isAuthenticated()) {
            next()
        } else {
            try {
                await loadUserData()
                const userStore = useUserStore()
                if (userStore.userDetail.userName !== undefined && userStore.userDetail.userName !== 'anonymous') {
                    next()
                } else {
                    next({
                        path: '/login'
                    })
                }
            } catch (e) {
                next({
                   path: '/login'
                } as RouteLocationRaw)
            }

        }
    } else {

        next()
    }
})


export default router
