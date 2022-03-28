import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable( {
    providedIn: 'root'
} )
export class DataTransferService {

    public constructor() { }
    public data: any;
    private dataForService = new Subject<string>();
    private toDoComplateService = new Subject<string>();

    getData$ = this.dataForService.asObservable();

    getToDoCompletedID$ = this.toDoComplateService.asObservable();

    sendData(data: any) {
        this.dataForService.next(data);
    }

    sendToDoComplete(toDoID: any) {
        this.toDoComplateService.next(toDoID);
    }

}
