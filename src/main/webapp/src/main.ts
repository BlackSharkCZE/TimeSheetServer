import Vue, {createApp} from 'vue'

import App from './App.vue'
import {createPinia} from "pinia";
import router from './router'

import keycloakPlugin from "@/plugins/KeycloakPlugin";

import axios, {AxiosRequestConfig} from "axios";
import moment from "moment";

import VueAxios from "vue-axios";
import Keycloak from "keycloak-js";

import PrimeVue from 'primevue/config'
import ConfirmationSerice from 'primevue/confirmationservice'
import 'primevue/resources/themes/bootstrap4-dark-blue/theme.css'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'
import 'primeflex/primeflex.min.css'

import {useUserStore} from "@/stores/UserStore";
import {UserInfo, useUserInfo} from "@/stores/InfoStore";

const app: Vue.App = createApp(App)
    .use(keycloakPlugin, {
        onReady: (_keycloak: Keycloak) => {
            if (!_keycloak.authenticated) {
                _keycloak.login()
            }
            moment.locale('cs')
            const pinia = createPinia()
            app.use(router)
                .use(VueAxios, axios)
                .use(pinia)
            const store = useUserStore()
            const userInfo = useUserInfo()
            app.config.globalProperties.axios.interceptors.request.use((config: AxiosRequestConfig) => {
                config.headers = {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${_keycloak.token}`
                }
                return config
            })
            app.provide('axios', app.config.globalProperties.axios)
            axios.get("/subject", {
                headers: {
                    'Authorization': 'Bearer ' + _keycloak.token
                }
            }).then(response => {
                store.storeUser(response.data)
            })

            const from = moment().add(-1, 'month').startOf('month').format('YYYY-MM-DD')
            const to = moment().add(-1, 'month').format('YYYY-MM-DD')

            axios.get(`/timeline/earning?from=${from}&to=${to}`, {
                headers: {
                    'Authorization': 'Bearer ' + _keycloak.token
                }
            }).then(response => {
                if (response.status == 200) {
                    const dataToStore = {
                        initialized: true,
                        fromTime: from,
                        toTime: to,
                        earnings: response.data
                    } as UserInfo
                    userInfo.storeUserInfo(dataToStore)
                }
                app.use(PrimeVue).use(ConfirmationSerice)
                app.mount("#app")
            })


        }
    })
