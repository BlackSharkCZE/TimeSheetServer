import {AxiosResponse, AxiosStatic} from "axios";

export class CompanyService {

    private _axios: AxiosStatic

    public constructor(axios: AxiosStatic) {
        this._axios = axios
    }

    public loadCompanies(): Promise<AxiosResponse<any, any>> {
        return this._axios.get("/company/all?primary=true")
    }

}
