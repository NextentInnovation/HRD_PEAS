import { Component, OnInit } from '@angular/core';
import { TestComponentBase } from '@src/app/test/test.component.base';
import { BarcodeScanner } from 'nativescript-barcodescanner';
import { TestService } from '@src/app/test/test.service';
import { Router } from '@angular/router';
import { NotificationService } from '@src/app/core/notification/notification.service';

@Component({
    selector: 'peas-test',
    templateUrl: './test.component.html',
    styleUrls: ['./test.component.scss'],
    moduleId: module.id
})
export class TestComponent extends TestComponentBase implements OnInit {
    constructor(private barcodeScanner: BarcodeScanner,
                protected testService: TestService,
                protected router: Router,
                protected notification: NotificationService) {
        super(testService, router, notification);
    }

    ngOnInit() {
        super.ngOnInit();
        this.title += ' Mobile';
    }

    tapScen() {
        this.barcodeScanner.scan( {
            formats: 'QR_CODE, EAN_13',
            cancelLabel: 'EXIT. Also, try the volume buttons!', // iOS only, default 'Close'
            cancelLabelBackgroundColor: '#333333', // iOS only, default '#000000' (black)
            message: 'Use the volume buttons for extra light', // Android only, default is 'Place a barcode inside the viewfinder rectangle to scan it.'
            showFlipCameraButton: true,   // default false
            preferFrontCamera: false,     // default false
            showTorchButton: true,        // default false
            beepOnScan: true,             // Play or Suppress beep on scan (default true)
            torchOn: false,               // launch with the flashlight on (default false)
            closeCallback: () => {
                console.log( 'Scanner closed' );
            }, // invoked when the scanner was closed (success or abort)
            resultDisplayDuration: 500,   // Android only, default 1500 (ms), set to 0 to disable echoing the scanned text
            orientation: undefined,     // Android only, default undefined (sensor-driven orientation), other options: portrait|landscape
            openSettingsIfPermissionWasPreviouslyDenied: true // On iOS you can send the user to the settings app if access was previously denied
        } ).then( ( result ) => {
                // Note that this Promise is never invoked when a 'continuousScanCallback' function is provided
                console.log( {
                    title: 'Scan result',
                    message: 'Format: ' + result.format + ',\nValue: ' + result.text,
                    okButtonText: 'OK'
                } );
            }, ( errorMessage ) => {
                console.log( 'No scan. ' + errorMessage );
            }
        );
    }
}
