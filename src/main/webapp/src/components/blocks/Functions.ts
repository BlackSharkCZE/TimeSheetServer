import {SelectedMonthType, MonthType, months} from "@/components/blocks/Types";
import moment from "moment/moment";

const currentMonth = function() {
    const now = moment()
    return {
        month: {
            index: now.month(),
            value: months[now.month()]
        } as MonthType,
        year: now.year()
    } as SelectedMonthType
}

const previousMonth = function() {
    const now = moment().add(-1, 'month')
    return {
        month: {
            index: now.month(),
            value: months[now.month()]
        } as MonthType,
        year: now.year()
    } as SelectedMonthType
}

const buildMoment= function(input: SelectedMonthType): moment.Moment {
    const str = input.year+'-'+  (input.month.index+1).toString(10).padStart(2,'0') +'-01'
    return moment(str, 'YYYY-MM-DD')
}

export {currentMonth, previousMonth, buildMoment}
