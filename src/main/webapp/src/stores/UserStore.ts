import {defineStore} from "pinia";

export type UserDetailType = {
    userDetail: UserDetail
}

export type UserDetail = {
    userName: string
    subject: string,
    internalId: number,
    company: any | null
    roles: string[]
}

export const useUserStore = defineStore('userStore', {
    state: () => ({
        userDetail: {}
    } as UserDetailType),
    getters: {
        hasPrimaryCompany: (state) => {
            return state.userDetail.company !== undefined && state.userDetail.company !== null
        },
    },
    actions: {
        hasRole(role: string)  {
            console.log('Checking if user hase role ', role, ' in roles ', this.userDetail.roles)
            if (this.userDetail.roles) {
                return this.userDetail.roles.filter( item => {
                    return item === role
                }).length > 0
            } else {
                return false
            }

        },
        storeUser(u: UserDetail) {
            this.userDetail = u
        },

        setCompany(u: any) {
            this.userDetail.company = u
        }

    }
})
