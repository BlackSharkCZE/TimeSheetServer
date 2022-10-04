import {App, Plugin} from "vue";
import Keycloak, {KeycloakInitOptions} from "keycloak-js";


const keycloakOptions: KeycloakInitOptions = {
    onLoad: 'check-sso',
    redirectUri: window.location.href,
    silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html'
}
const location = document.location.protocol + '//' + document.location.host + '/data/keycloak.json'
const keycloak = new Keycloak(location)

const keycloakPlugin: Plugin = {
    install(app: App, options: any): any {
        keycloak.init(keycloakOptions).then(function (authenticated) {
            setTimeout(updateTokenProcess, 1000)
            options.onReady(keycloak)
        }).catch(function (e) {
            options.onReady()
        })
        app.config.globalProperties.$keycloak = keycloak
        app.provide('keycloak', keycloak)
    }
}

function updateTokenProcess() {
    keycloak.updateToken(10).then((res) => {
        setTimeout(updateTokenProcess, 10000)
    })
}

export default keycloakPlugin
export {keycloak as KeycloakInstance}
