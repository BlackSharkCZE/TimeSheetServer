import {FormDataType} from "@/components/blocks/TimelineDefs";
import moment from "moment/moment";
import {Axios, AxiosRequestConfig, AxiosResponse, AxiosStatic} from "axios";

type AxiosOperation = <T = any, R = AxiosResponse<T>, D = any>(url: string, data?: D, config?: AxiosRequestConfig<D>)=> Promise<R>

function buildTimelineDataFromForm(formData: FormDataType): any {

    const input = formData.date?.getFullYear() + "-" + (1 + (formData.date?.getMonth() || 0)).toString(10).padStart(2, '0') + "-" +
        formData.date?.getDate().toString(10).padStart(2, '0')

    const fromTimeM = moment(input, 'YYYY-MM-DD')

    fromTimeM.set('hour', (formData.fromTime as Date).getHours())
    fromTimeM.set('minute', (formData.fromTime as Date).getMinutes())
    const fromTruncated = fromTimeM.startOf('minute').toISOString(true)

    const toTimeM = moment(input, 'YYYY-MM-DD')
    toTimeM.set('hour', (formData.toTime as Date).getHours())
    toTimeM.set('minute', (formData.toTime as Date).getMinutes())
    const toTimeTruncated = toTimeM.startOf('minute').toISOString(true)

    const x = {
        id: formData.id,
        projectId: formData.project.id,
        fromTime: fromTruncated,
        toTime: toTimeTruncated,
        pause: formData.pause,
        workTime: 0,
        note: formData.note
    }
    return x

}

function saveTimeline(axios: AxiosStatic | undefined, formData: FormDataType, successHandler: (data: any) => void, errorHandler: (data: AxiosResponse<any, any>) => void) {
    if (axios) {
        const action:AxiosOperation = axios.post
        saveUpdate(action, formData, successHandler, errorHandler)
    }
}

function updateTimeline(axios: AxiosStatic | undefined, formData: FormDataType, successHandler: (data: any) => void, errorHandler: (data: AxiosResponse<any, any>) => void) {
    if (axios) {
        const action:AxiosOperation = axios.put
        saveUpdate(action, formData, successHandler, errorHandler)
    }
}

function saveUpdate(operation: AxiosOperation, formData: FormDataType, successHandler: (data: any) => void, errorHandler: (data: AxiosResponse<any, any>) => void) {
    const path = "/timeline"
    operation(path, buildTimelineDataFromForm(formData)).then((response) => {
        if (response.status >= 200 && response.status <= 299) {
            if (response.data.itemID > 0) {
                successHandler(response.data)
            } else {
                errorHandler(response)
            }
        } else {
            errorHandler(response)
        }
    })
}


export {buildTimelineDataFromForm, saveTimeline, updateTimeline}
