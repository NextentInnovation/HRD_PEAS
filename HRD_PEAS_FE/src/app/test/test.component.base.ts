import { OnInit } from '@angular/core';
import { TestService } from '@src/app/test/test.service';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { PageSizerConfig } from '@src/app/core/table-control/configs/page-sizer.config';
import { Router } from '@angular/router';
import { SortDirection } from '@src/app/core/table-control/constants/sort-direction.contant';
import { TestModels } from '@src/app/test/test.models';
import { MultiSelectModel } from '@src/app/models/multi-select.model';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';

export class TestComponentBase extends BaseComponent implements OnInit {
    title = 'PEAS Frontend 0.7 Table';

    public search: any = {};
    // tslint:disable-next-line:max-line-length
    public tableOptions: TableOptions = new TableOptions( 'users', this.router, PageSizerConfig.DEFAULT_PAGE_SIZE, this.search, false, false );
    public list: Array<TestModels> = new Array<TestModels>();
    public multiSelectList: Array<MultiSelectModel<TestModels>> = new Array<MultiSelectModel<TestModels>>();

    public selectedListElement: TestModels;
    public selectedList: Array<TestModels> = new Array<TestModels>();
    public selectedListIDs = new Array<string>();
    public testData: string;

    public selectButtonDatas = [
        { id: 1, name: 'Rutin', code: '0' },
        { id: 2, name: 'Tapasztalatszerzés', code: '1' },
        { id: 3, name: 'Kihívás', code: '2' },
    ];
    public selectButtonList: MultiSelectModel<any>[] = [];
    public selectButton: any;

    constructor( protected testService: TestService,
                 protected router: Router,
                 protected notification: NotificationBaseService ) {
        super();
    }

    ngOnInit() {
        this.getList();
        this.selectButtonList = MultiSelectModel.listConverter( this.selectButtonDatas );
    }

    getList() {
        this.testService.getTestTableData( this.tableOptions ).subscribe(
            response => {
                this.list = response;
                this.multiSelectList = MultiSelectModel.listConverter<TestModels>( response );
                // this.tableOptions.update( this.list.length, this.list );
                this.tableOptions.setSourceList( this.list );
                this.tableOptions.sortDirection = SortDirection.ASC;
                this.tableOptions.sortedColumn = 'name';
                this.refreshList();
                console.log( this.multiSelectList );
            }
        );
    }

    refreshList() {
        this.list = this.tableOptions.update( this.list.length, this.list );
    }

    deleteItemFromList( item ) {
        const index = this.selectedList.indexOf( item );
        if ( index >= 0 ) {
            this.selectedList.splice( index, 1 );
        }
    }

    show( elenemt: TestModels ) {
        console.log( elenemt );
        return elenemt;
    }

    tooltTipSuccess( message: string ) {
        this.notification.success( message );
    }

    tooltTipError( message: string ) {
        this.notification.error( message );
    }

    tooltTipInfo( message: string ) {
        this.notification.info( message );
    }
}
