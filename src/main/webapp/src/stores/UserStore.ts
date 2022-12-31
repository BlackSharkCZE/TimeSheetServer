import {defineStore} from "pinia";

export type UserDetailType = {
    userDetail: UserDetail
}

export type UserDetail = {
    userName: string
    subject: string,
    internalId: number,
    company: any | null
}

export const useUserStore = defineStore('userStore', {
    state: () => ({
        userDetail: {}
    } as UserDetailType),
    getters: {
        hasPrimaryCompany: (state) => {
            return state.userDetail.company !== undefined && state.userDetail.company !== null
        }
    },
    actions: {
        storeUser(u: UserDetail) {
            this.userDetail = u
        },

        setCompany(u: any) {
            this.userDetail.company = u
        }

    }
})
