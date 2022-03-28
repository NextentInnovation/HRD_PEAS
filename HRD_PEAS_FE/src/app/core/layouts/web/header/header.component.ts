import { Component, EventEmitter, HostListener, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { StorageControl } from '@src/app/core/storage/storage.control';
import { UserDataModel } from '@src/app/models/user/user.data.model';
import { LoginService } from '@src/app/services/login/login.service';
import { CommonUtils } from '@src/app/core/utils/common.util';
import { Router } from '@angular/router';
import { SiteURLS } from '@src/app/core/constants/urls/site-url.constant';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { NotificationIFService } from '@src/app/services/notification/notification.service';
import { NotificationModel, NotificationSearchModel } from '@src/app/models/notification/notification.model';
import { TableOptions } from '@src/app/core/table-control/models/table-options.model';
import { HttpErrorResponse } from '@angular/common/http';
import { ReferenceType, NotificationStatus, ToDoStatus } from '@src/app/core/constants/constans';
import { NotificationService } from '@src/app/core/notification/notification.service';
import { TranslateService } from '@ngx-translate/core';
import { ToDoModel, ToDoSearchModel } from '@src/app/models/to-do/to-do.model';
import { ToDoService } from '@src/app/services/to-do/to-do.service';
import { SortDirection } from '@src/app/core/table-control/constants/sort-direction.contant';
import { DataTransferService } from '@src/app/core/services/data-transfer.service';

@Component( {
    selector: 'peas-header',
    templateUrl: './header.component.html',
    styleUrls: [ './header.component.scss' ]
} )
export class HeaderComponent extends BaseComponent implements OnInit, OnDestroy {
    private static notificationRefreshTime = 30;
    @Output() sidebarToggleEvent = new EventEmitter<any>();
    NotificationStatus = NotificationStatus;
    userData: UserDataModel;

    public notificationSearch: NotificationSearchModel = new NotificationSearchModel();
    public notificationTableOptions: TableOptions =
        new TableOptions( 'newNotifications', this.router, 30, this.notificationSearch, true, true );
    public notificationList: Array<NotificationModel> = new Array<NotificationModel>();

    public toDoSearch: ToDoSearchModel = new ToDoSearchModel();
    public toDoTableOptions: TableOptions =
        new TableOptions( 'headerTodo', this.router, 999999, this.toDoSearch, false, false );
    public toDoList: Array<ToDoModel> = new Array<ToDoModel>();

    public newNotificiation = false;
    private loadNotificationInterval;

    @ViewChild('dropDownBox', {static: false}) dropDownBoxElement;
    @ViewChild('dropDown', {static: false}) dropDownElement;
    @ViewChild('dropDownMenu', {static: false}) dropDownMenuElement;

    constructor( private loginService: LoginService,
                 private router: Router,
                 private notification: NotificationService,
                 private notificationIFService: NotificationIFService,
                 private translate: TranslateService,
                 private toDoService: ToDoService,
                 private dataTransferService: DataTransferService) {
        super();
        this.toDoTableOptions.sortedColumn = 'deadline';
        this.toDoTableOptions.sortDirection = SortDirection.ASC;
    }

    ngOnInit() {
        this.userData = StorageControl.getUserData();
        this.loadNotifications();
        this.notificationIntervalLoader();
        this.getOpenToDoList();
        this.dataTransferService.getToDoCompletedID$.subscribe(value => {
            if (this.toDoList) {
                console.log('Todo kivétele: ', value);
                this.toDoList = this.toDoList.filter(value1 => value1.reference !== Number(value));
            }
        });
    }

    @HostListener('document:click', ['$event.target'])
    public onClick(targetElement) {
        const clickedInside = this.dropDownBoxElement.nativeElement.contains(targetElement);
        if (!clickedInside) {
            this.closeNotificationDropDown();
        }
    }

    menuToggle() {
        this.sidebarToggleEvent.emit();
    }

    getMonogram( fullName: string ): String {
        let it = 'A';		// Create and set the intials variable to nothing
        if ( CommonUtils.isNotEmpty( fullName ) ) {
            it = '';
            const n = fullName.split( ' ' );	// Split the full name into an array
            let i = 0;			// Set i to 0 for the while loop
            while ( i < n.length && i < 3 ) {
                it += n[ i ][ 0 ];	// Get the first letter from the name and add it to the it(initial) variable
                i++;			// Increment i by 1
            }
        }
        return (it.toUpperCase());	// Convert the initials to uppercase and then return it
    }

    notificationIntervalLoader() {
        this.loadNotificationInterval = setInterval(() => { this.loadNotifications(); }, HeaderComponent.notificationRefreshTime * 1000);
    }

    clearNotificationInterval() {
        clearInterval( this.loadNotificationInterval );
    }

    loadNotifications(tryNumber = 0) {
        if (!this.dropDownElement || !this.dropDownElement.nativeElement.classList.contains('show')) {
            this.notificationIFService.getList( this.notificationSearch, this.notificationTableOptions ).subscribe(
                response => {
                    this.notificationList = response ? response.content : new Array();
                    this.newNotification();
                },
                ( error: HttpErrorResponse ) => {
                    if ( error.status !== 401 && tryNumber < 3 ) {
                        tryNumber++;
                        setTimeout( () => {
                            this.loadNotifications( tryNumber );
                        }, 3000 );
                    }
                }
            );
        }
    }

    loadNotificationsAndSetRead() {
        this.clearNotificationInterval();
        const notificationSearchAndSetRead: NotificationSearchModel = new NotificationSearchModel();
        notificationSearchAndSetRead.markReaded = true;
        this.notificationIFService.getList( notificationSearchAndSetRead, this.notificationTableOptions ).subscribe(
            response => {
                this.notificationList = new Array();
                this.notificationList = response ? response.content : new Array();
                this.newNotificiation = false;
                this.notificationIntervalLoader();
            }
        );
    }

    getOpenToDoList() {
        this.toDoSearch.status = new Array<string>();
        this.toDoSearch.status.push(ToDoStatus.OPEN);
        this.toDoService.getList( this.toDoSearch, this.toDoTableOptions).subscribe(
            response => {
                this.toDoList = response ? response.content : new Array();
                this.toDoTableOptions.update( response ? response.totalElements : 0, this.toDoList );
            }
        );
    }

    newNotification() {
        this.newNotificiation = false;
        this.notificationList.forEach(value => {
            if ( !value.readed ) {
                this.newNotificiation = true;
            }
        });
        if (this.newNotificiation) {
            this.getOpenToDoList();
        }
    }

    getUnreadNotificationNumber(): number {
        let ret = 0;
        if (this.notificationList) {
            this.notificationList.forEach(value => {
               if (!value.readed) {
                   ret++;
               }
            });
        }
        return ret;
    }

    goToReferenceAction( item: NotificationModel) {
        console.log(item);
        if (NotificationStatus.OPEN === item.status) {
            switch ( item.referenceType ) {
                case ReferenceType.EVALUATION: {
                    this.router.navigate( [ this.SiteURLS.RATING.BASE + '/' + this.SiteURLS.RATING.EVALUATION, item.reference ] );
                    this.closeNotificationDropDown();
                    break;
                }
                case ReferenceType.RATING: {
                    this.router.navigate( [ this.SiteURLS.RATING.BASE + '/' + this.SiteURLS.RATING.RATING, item.reference ] );
                    this.closeNotificationDropDown();
                    break;
                }
                default: {
                    this.notification.info( this.translate.instant('notification.click_event.unmanaged_type') );
                    break;
                }
            }
        } else if (NotificationStatus.INFORMATION === item.status) {
            this.notification.info( this.translate.instant('notification.click_event.information_type') );
        } else {
            this.notification.info( this.translate.instant('notification.click_event.not_open_status') );
        }
    }

    openNextToDo() {
        if (this.toDoList && this.toDoList.length > 0) {
            switch ( this.toDoList[0].referenceType ) {
                case ReferenceType.EVALUATION: {
                    this.router.navigate( [ this.SiteURLS.RATING.BASE + '/' + this.SiteURLS.RATING.EVALUATION + '/'
                    + this.toDoList[0].reference, {back: 'main'} ] );
                    break;
                }
                case ReferenceType.RATING: {
                    this.router.navigate( [ this.SiteURLS.RATING.BASE + '/' + this.SiteURLS.RATING.RATING + '/'
                    + this.toDoList[0].reference, {back: 'main'} ] );
                    break;
                }
                default: {
                    this.notification.info( 'Az adott feladat nem elvégezhető művelet!' );
                    break;
                }
            }
        }
    }

    closeNotificationDropDown() {
        this.dropDownElement.nativeElement.classList.remove('show');
        this.dropDownMenuElement.nativeElement.classList.remove('show');
    }

    logout() {
        this.loginService.logout().subscribe(
            () => {
                console.log( 'LOGOUT!' );
            },
            error1 => {
                StorageControl.clearLoggedInUserSession();
                console.log( 'LOGOUT!' );
            }
        );
        StorageControl.clearLoggedInUserSession();
        this.router.navigate( [ SiteURLS.LOGIN.BASE ] );
    }

    ngOnDestroy(): void {
        console.log('destroy');
        this.clearNotificationInterval();
    }
}
