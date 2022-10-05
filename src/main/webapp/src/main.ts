import Vue, {createApp} from 'vue'

import App from './App.vue'
import {createPinia} from "pinia";
import router from './router'

import keycloakPlugin from "@/plugins/KeycloakPlugin";

import axios from "axios";
import moment from "moment";

import VueAxios from "vue-axios";
import Keycloak from "keycloak-js";

import { useUserStore } from "@/stores/UserStore";

const app: Vue.App = createApp(App)
    .use(keycloakPlugin, {
        onReady: (_keycloak: Keycloak) => {
            moment.locale('cs')
            const pinia = createPinia()
            app.use(router)
                .use(VueAxios, axios)
                .use(pinia)

            const store = useUserStore()

            app.provide('axios', app.config.globalProperties.axios)
            axios.get("/subject", {
                headers: {
                    'Authorization': 'Bearer ' + _keycloak.token
                }
            }).then(response => {
                store.storeUser(response.data)
                app.mount("#app")
            })
        }
    })
