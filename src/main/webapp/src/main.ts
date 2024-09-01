import Vue, {createApp} from 'vue'

import App from './App.vue'
import {createPinia} from "pinia";
import router from './router'

import axios from "axios";
import moment from "moment";
import 'moment/locale/cs'
import {cs} from "@/cs";

import {createI18n} from "vue-i18n";
import messages from '@/i18n/messages'

import VueAxios from "vue-axios";

import PrimeVue from 'primevue/config'
import ConfirmationSerice from 'primevue/confirmationservice'
import 'primevue/resources/themes/bootstrap4-dark-blue/theme.css'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'
import 'primeflex/primeflex.min.css'

import {useUserStore} from "@/stores/UserStore";
import {useDataStore} from "@/stores/DataStore";

import {useUserInfo} from "@/stores/InfoStore";

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
const pinia = createPinia()
const l = navigator.language.split("-")[0] || 'cs'
const i18n = createI18n({
    legacy: false,
    locale: l,
    fallbackLocale: 'cs',
    messages
})

moment.locale('cs')
app.use(router)
    .use(VueAxios, axios)
    .use(pinia)
    .use(i18n)

app.provide('axios', app.config.globalProperties.axios)
const store = useUserStore()
const userInfo = useUserInfo()
const dataStore = useDataStore()
app.use(PrimeVue, {locale: cs}).use(ConfirmationSerice)
app.mount("#app")
