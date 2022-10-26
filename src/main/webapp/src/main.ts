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
import 'primevue/resources/themes/mdc-dark-indigo/theme.css'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'
import 'primeflex/primeflex.min.css'

import { useUserStore } from "@/stores/UserStore";

const app: Vue.App = createApp(App)
    .use(keycloakPlugin, {
        onReady: (_keycloak: Keycloak) => {
            console.log('Keycloak authenticated: ', _keycloak.authenticated)
            if (!_keycloak.authenticated) {
                _keycloak.login()
            }
            moment.locale('cs')
            const pinia = createPinia()
            app.use(router)
                .use(VueAxios, axios)
                .use(pinia)

            const store = useUserStore()
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
                app.use(PrimeVue)

                app.mount("#app")
            })
        }
    })
