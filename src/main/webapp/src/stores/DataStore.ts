import {defineStore} from "pinia";
import { TimesheetProject, Company} from "@/components/blocks/Types";
import axios from "axios";

export type DataStoreStateType = {
    projects: Array<TimesheetProject>
}

export const useDataStore = defineStore('dataStore', {
    state: () => ({
        projects: []
    } as DataStoreStateType),
    getters: {
        getProjects(): Array<TimesheetProject> {
            return this.projects
        }
    },
    actions: {
        async load() {
            axios.get("/project/all").then((response) => {
                if (response.status >= 200 && response.status <= 299) {
                    this.projects = response.data
                } else {
                    console.error('Error loading projects into store', response)
                    this.projects = []
                }
            })
        },

    }
})
