import { Injectable } from '@angular/core';
import { BaseService } from '@src/app/core/services/base.service';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { HttpClient } from '@angular/common/http';
import { finalize } from 'rxjs/operators';

@Injectable( {
    providedIn: 'root'
} )
export class TestService extends BaseService {

    constructor( private http: HttpClient ) {
        super();
    }

    getTestTableData( tableOptions: TableOptions ) {
        this.addLoadingIndicator( tableOptions.name );
        return this.http.get<any>( '/mock/test-table.json' )
                   .pipe( finalize( () => this.removeLoadingIndicator( tableOptions.name ) ) );
    }
}
