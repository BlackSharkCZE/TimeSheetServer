export type GemProject = {
    key: number,
    text: string,
    parentId: number | null,
    projects: Array<GemProject> | null,
    tags: Array<GemProject> | null,
}

export type Company = {
    bankAccountNumber: string
    castObce: string,
    cisloDomu: string,
    companyName: string,
    dic: string,
    email: string,
    ic: string,
    id: number,
    obec: string,
    okres: string,
    phoneNumber: string,
    platceDph: boolean,
    primaryAccount: boolean,
    ps: string,
    ulice: string
}

export type Requisition = {
    company: Company,
    customerNumber: string,
    endDate: string,
    id: number,
    note: string,
    origFileName: string,
    path: string,
    startDate: string
}

export type InvoiceItem = {
    id: number,
    price: number,
    totalPrice: number,
    vat: number,
    requisition: Requisition,
    note: string
}

export type Billing = {
    companyName: string,
    companyId: number,
    workTime: number,
    earn: number,
    note: string
}

export type RemoteWriterTimestamp = {
    id: number,
    name: string,
    success: boolean,
    timestamp: Date
}

export type MonthType = {
    index: number,
    value: string
}

export type SelectedMonthType = {
    month: MonthType,
    year: number
}

export type ErrorType = {
    show: boolean,
    message: string | null
}

export const months = ['Leden', 'Únor', 'Březen', 'Duben', 'Květen', 'Červen', 'Červenec', 'Srpen', 'Září', 'Říjen', 'Listopad', 'Prosinec']
