import Vue, {createApp} from 'vue'

import App from './App.vue'
import router from './router'

import keycloakPlugin from "@/plugins/KeycloakPlugin";

import axios from "axios";
import moment from "moment";

import VueAxios from "vue-axios";
import Keycloak from "keycloak-js";

const app: Vue.App = createApp(App)
    .use(keycloakPlugin, {
        onReady: (_keycloak: Keycloak) => {
            moment.locale('cs')
            app.use(router)
                .use(VueAxios, axios)
            app.provide('axios', app.config.globalProperties.axios)
            app.mount("#app")
        }
    })
