import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, ViewChild } from '@angular/core';
import { NavigationCancel, NavigationEnd, NavigationStart, Router } from '@angular/router';
import { fadeAnimation, slideInAnimation } from '@src/app/core/utils/fadeIn.animation';
import { MatSidenav } from '@angular/material';
import { MediaMatcher } from '@angular/cdk/layout';

@Component( {
    selector: 'peas-main-wrapper',
    templateUrl: './main-wrapper.component.html',
    styleUrls: [ './main-wrapper.component.scss' ],
    animations: [
        fadeAnimation,
        slideInAnimation
    ]
} )
export class MainWrapperComponent implements AfterViewInit, OnDestroy {
    public loading: boolean;
    private menuAlwaysOpen = false;
    @ViewChild( 'sidebar', {static: false} ) sidebar: MatSidenav;

    mobileQuery: MediaQueryList;
    private _mobileQueryListener: () => void;

    constructor(private router: Router,
                private changeDetectorRef: ChangeDetectorRef,
                private media: MediaMatcher) {
        this.mobileQuery = media.matchMedia('(max-width: 1200px)');
        this._mobileQueryListener = () => changeDetectorRef.detectChanges();
        this.mobileQuery.addListener(this._mobileQueryListener);
    }

    ngOnDestroy(): void {
        this.mobileQuery.removeListener(this._mobileQueryListener);
    }

    ngAfterViewInit() {
        this.router.events
            .subscribe( ( event ) => {
                if ( event instanceof NavigationStart ) {
                    this.loading = true;
                } else if (
                    event instanceof NavigationEnd ||
                    event instanceof NavigationCancel
                ) {
                    this.loading = false;
                }
            } );
    }

    onSidebarOpen( event: any ) {
        this.sidebar.open();
    }

    onSidebarClose( event: any ) {
        this.sidebar.close();
    }

    onSidebarClick( event: any ) {
        if (this.mobileQuery.matches) {
            this.onSidebarClose(event);
        }
    }

    onSideBarToogle( event: any ) {
        if (this.sidebar.opened) {
            this.sidebar.close();
        } else {
            this.sidebar.open();
        }
    }

}
