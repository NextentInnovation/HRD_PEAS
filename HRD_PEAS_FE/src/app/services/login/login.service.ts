import { BaseService } from '@src/app/core/services/base.service';
import { ApiURLS } from '@src/app/core/constants/urls/api-url.constant';
import { HttpClient } from '@angular/common/http';
import { AuthenticationRequest } from '@src/app/models/authentication/authentication.model';

export class LoginService extends BaseService {

    constructor( private http: HttpClient ) {
        super();
    }

    login( request: AuthenticationRequest ) {
        // @ts-ignore
        return this.http.post<any>( this.createURI( ApiURLS.LOGIN ), request, { responseType: 'text' } );
    }

    logout() {
        return this.http.delete<any>( ApiURLS.LOGOUT );
    }
}
