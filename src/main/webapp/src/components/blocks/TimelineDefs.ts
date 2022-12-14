import {minValue, required} from "@vuelidate/validators";

type RemoteWriteTimestampType = {
    id: number,
    name: string,
    timestamp: Date,
    success: boolean
}

type TimelineType = {

    id: number,
    projectName: string,
    projectId: number,
    fromTime: Date,
    toTime: Date,
    pause: number,
    workTime: string,
    note: string,
    remoteWriteTimestamp: Array<RemoteWriteTimestampType>,
    writers: Array<string>

}

type FormDataType = {
    project: any | null,
    date: Date | null,
    fromTime: Date | null,
    toTime: Date | null,
    note: string | null,
    pause: number
}

const TimelineRules = {
    project: {required},
    note: {required},
    date: {required},
    fromTime: {required},
    toTime: {required},
    pause: {required, minValue: minValue(0)},
}

export {FormDataType, TimelineRules, TimelineType, RemoteWriteTimestampType}
