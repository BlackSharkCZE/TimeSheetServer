import Vue, {createApp} from 'vue'

import App from './App.vue'
import {createPinia} from "pinia";
import router from './router'

import keycloakPlugin from "@/plugins/KeycloakPlugin";

import axios, {AxiosRequestConfig} from "axios";
import moment from "moment";
import 'moment/locale/cs'
import {cs} from "@/cs";

import {createI18n} from "vue-i18n";
import messages from '@/i18n/messages'

import VueAxios from "vue-axios";
import Keycloak from "keycloak-js";

import PrimeVue from 'primevue/config'
import ConfirmationSerice from 'primevue/confirmationservice'
import 'primevue/resources/themes/bootstrap4-dark-blue/theme.css'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'
import 'primeflex/primeflex.min.css'

import {useUserStore} from "@/stores/UserStore";
import {useDataStore} from "@/stores/DataStore";

import {UserInfo, useUserInfo} from "@/stores/InfoStore";
import {CompanyService} from "@/services/CompanyService";

const primeVueConfig = {
    locale: {
        dayNames: ["Neděle", "Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek", "Sobota"],
        dayNamesShort: ["Ned", "Pon", "Úte", "Stř", "Čtv", "Pát", "Sob"],
        dayNamesMin: ["Ne", "Po", "Út", "St", "Čt", "Pá", "So"],
        monthNames: ["Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"],
        monthNamesShort: ["Led", "Úno", "Bře", "Dub", "Kvě", "Čer", "Črv", "Srp", "Zář", "Říj", "Lis", "Pro"],
        firstDayOfWeek: 1,
        aria: {
            navigation: "Navigace"
        }
    }
}

const app: Vue.App = createApp(App)
    .use(keycloakPlugin, {
        onReady: (_keycloak: Keycloak) => {
            if (!_keycloak.authenticated) {
                _keycloak.login()
            }

            const l = navigator.language.split("-")[0] || 'cs'

            const i18n = createI18n({
                legacy: false,
                locale: l,
                fallbackLocale: 'cs',
                messages
            })

            moment.locale('cs')
            const pinia = createPinia()
            app.use(router)
                .use(VueAxios, axios)
                .use(pinia)
                .use(i18n)
            const store = useUserStore()
            const userInfo = useUserInfo()
            const dataStore = useDataStore()
            app.config.globalProperties.axios.interceptors.request.use((config: AxiosRequestConfig) => {
                config.headers = {
                    'Content-type': 'application/json',
                    'Authorization': `Bearer ${_keycloak.token}`
                }
                return config
            })

            app.provide('axios', app.config.globalProperties.axios)
            app.provide('CompanyService', new CompanyService(axios))
            axios.get("/subject", {
                headers: {
                    'Authorization': 'Bearer ' + _keycloak.token
                }
            }).then(response => {
                store.storeUser(response.data)
            })

            dataStore.load()

            const from = moment().add(0, 'month').startOf('month').format('YYYY-MM-DD')
            const to = moment().add(0, 'month').endOf('month').format('YYYY-MM-DD')

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
                app.use(PrimeVue, {locale: cs}).use(ConfirmationSerice)
                app.mount("#app")
            })
        }
    })
