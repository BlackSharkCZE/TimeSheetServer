import {defineStore} from "pinia";

export type UserInfoType = {
    userInfo: UserInfo
}

export type EarningType = {
    workTime: number,
    earning: number,
    companyId: number,
    companyName: number,
}

export type UserInfo = {
    initialized: boolean
    fromTime: string,
    toTime: string,
    earnings: EarningType[]
}

export const useUserInfo = defineStore('userInfoStore', {
    state: () => ({
        userInfo: {
            initialized: false,
            fromTime: '',
            toTime: '',
            earnings: []
        }
    } as UserInfoType),
    getters: {
        allEarningSum(): number {
            return this.userInfo.earnings.reduce((acc,current) => acc + current.earning, 0.0)
        },
        allWorktimeSum(): number {
            return this.userInfo.earnings.reduce((acc,current) => acc + current.workTime, 0.0) * 60.0
        }
    },
    actions: {
        storeUserInfo(u: UserInfo) {
            this.userInfo = u
        }



    }
})
