import Vue, {createApp} from 'vue'

import App from './App.vue'
import router from './router'

import keycloakPlugin from "@/plugins/KeycloakPlugin";

import axios from "axios";
import moment from "moment";

import VueAxios from "vue-axios";

const app: Vue.App = createApp(App)
    .use(keycloakPlugin, {
        onReady: () => {

            moment.locale('cs')

            app.use(router)
                .use(VueAxios, axios)
            app.provide('axios', app.config.globalProperties.axios)

            app.config.globalProperties.moment = moment
            app.provide('moment', app.config.globalProperties.moment)

            app.mount("#app")
        }
    })
