import {defineStore} from "pinia";

export type UserDetailType = {
    userDetail: UserDetail
}

export type UserDetail = {
    userName: string
    subject: string,
    company: any
}

export const useUserStore = defineStore('userStore', {
    state: () => ({
        userDetail: {}
    } as UserDetailType),
    getters: {},
    actions: {
        storeUser(u: UserDetail) {
            this.userDetail = u
        }
    }
})
