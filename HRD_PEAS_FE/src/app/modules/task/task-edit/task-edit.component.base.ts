import { EvaluatorModel, TaskModel } from '@src/app/models/task/task.model';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationBaseService } from '@src/app/core/notification/notification.base.service';
import { TaskService } from '@src/app/services/task/task.service';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from '@src/app/shared/components/base/base-component';
import { OnInit } from '@angular/core';
import { CommonUtils } from '@src/app/core/utils/common.util';
import { AutoCompletType, TaskStatuses, TaskTypes, ViewMode } from '@src/app/core/constants/constans';
import { ResourceService } from '@src/app/services/resource/resource.service';
import { BaseDatasModel } from '@src/app/models/base/base-datas.model';
import { UserDataModel } from '@src/app/models/user/user.data.model';
import { StorageControl } from '@src/app/core/storage/storage.control';

export class TaskEditComponentBase extends BaseComponent implements OnInit {
    taskId: string;
    viewMode: ViewMode = ViewMode.VIEW;
    TaskStatuses = TaskStatuses;
    data: TaskModel = new TaskModel();

    public baseData: BaseDatasModel;
    public users: EvaluatorModel[] = new Array<EvaluatorModel>();
    public meUser: UserDataModel;
    private backType = 'basic';

    constructor( protected router: Router,
                 protected notification: NotificationBaseService,
                 protected taskService: TaskService,
                 protected translate: TranslateService,
                 protected route: ActivatedRoute,
                 protected resourceService: ResourceService ) {
        super();
        this.meUser = StorageControl.getUserData();
    }

    ngOnInit() {
        this.resourceService.getBaseDatas().subscribe(
            response => {
                this.baseData = response;
                this.baseData.factors.forEach(value => {
                   value.required = false;
                });
                if ( this.viewMode === ViewMode.CREATE ) {
                    this.data.owner = this.baseData.currentUser;
                    this.data.evaluators.push(new EvaluatorModel( this.meUser.id, this.meUser.fullName, this.meUser.userName, this.meUser.email ));
                }
                if ( response && this.data ) {
                    this.setFactorRequired();
                }
            }
        );

        this.route.params.subscribe( params => {
            if ( CommonUtils.isNotEmpty( params.back ) ) {
                this.backType = params.back;
            }
            if ( CommonUtils.isNotEmpty( params.id ) ) {
                this.taskId = params.id;
                this.taskService.get( this.taskId ).subscribe(
                    response => {
                        this.data = response;
                        if ( this.data.status === TaskStatuses.PARAMETERIZATION ) {
                            this.viewMode = ViewMode.EDIT;
                            this.getUsers();
                        } else {
                            this.viewMode = ViewMode.VIEW;
                        }
                        if ( this.baseData && response.taskfactors ) {
                            this.setFactorRequired();
                        }
                    }
                );
            } else {
                this.viewMode = ViewMode.CREATE;
                this.getUsers();
                this.data.owner = this.baseData ? this.baseData.currentUser : null;
                this.data.evaluators.push(new EvaluatorModel( this.meUser.id, this.meUser.fullName, this.meUser.userName, this.meUser.email ));
                this.data.taskType = TaskTypes.NORMAL;
            }
        } );
    }

    setFactorRequired() {
        this.baseData.factors.forEach( value => {
            this.data.taskfactors.forEach( value1 => {
                if ( value.id === value1.id ) {
                    value.required = value1.required;
                }
            } );
        } );
    }

    getUsers( filter = '' ) {
        this.resourceService.autoComplete( AutoCompletType.USER, filter ).subscribe(
            response => {
                this.users = new Array<EvaluatorModel>();
                if ( response.content ) {
                    response.content.forEach( value => {
                        if (value.id !== this.meUser.id) {
                            this.users.push( new EvaluatorModel( value.id, value.description, value.name, value.email ) );
                        }
                    } );
                }
            }
        );
    }

    save() {
        if (!this.data.evaluators.find(value => value.evaluator.id === this.meUser.id)) {
            this.data.evaluators.push(new EvaluatorModel( this.meUser.id, this.meUser.fullName, this.meUser.userName, this.meUser.email ));
            this.data.evaluators = JSON.parse(JSON.stringify(this.data.evaluators));
        }
        this.DateUtils.setEndOfDay(this.data.deadline);
        this.taskService.set( this.taskId, this.data ).subscribe(
            response => {
                this.notification.success( this.translate.instant( 'notification.general.success_save' ) );
            }
        );
    }

    create() {
        if (!this.data.evaluators.find(value => value.evaluator.id === this.meUser.id)) {
            this.data.evaluators.push(new EvaluatorModel( this.meUser.id, this.meUser.fullName, this.meUser.userName, this.meUser.email ));
        }
        this.DateUtils.setEndOfDay(this.data.deadline);
        this.taskService.create( this.data ).subscribe(
            response => {
                this.notification.success( this.translate.instant( 'notification.general.success_create' ) );
                this.back();
            }
        );
    }

    copy() {
        this.taskService.set( this.taskId, this.data ).subscribe(
            response => {
                this.taskService.copy( this.taskId ).subscribe(
                    response2 => {
                        this.notification.success( this.translate.instant( 'notification.general.success_copy' ) );
                        this.router.navigate( [ this.SiteURLS.TASK.BASE + '/' + this.SiteURLS.TASK.EDIT, response2.id ] );
                    }
                );
            }
        );
    }

    startEvaluation() {
        this.taskService.set( this.taskId, this.data ).subscribe(
            response => {
                this.taskService.startEvaluation( this.taskId ).subscribe(
                    () => {
                        this.notification.success( this.translate.instant( 'task_edit.form.notification.start_evaluation.success' ) );
                        this.back();
                    }
                );
            }
        );
    }

    clearSelectedEvaluators() {
        this.data.evaluators.push(new EvaluatorModel( this.meUser.id, this.meUser.fullName, this.meUser.userName, this.meUser.email ));
        this.data.evaluators = JSON.parse(JSON.stringify(this.data.evaluators));
    }

    removeEvaluators( event ) {
        if (event.value.evaluator.id === this.meUser.id) {
            this.data.evaluators.push(new EvaluatorModel( this.meUser.id, this.meUser.fullName, this.meUser.userName, this.meUser.email ));
            this.data.evaluators = JSON.parse(JSON.stringify(this.data.evaluators));
        }
    }

    removeFactor( event ) {
        event.value.required = false;
    }

    clearSelectedFactor() {
        this.baseData.factors.forEach(value => {
            value.required = false;
        });
    }

    changeFactorRequired(event: any, id: any ) {
        try {
            this.data.taskfactors.find( value => value.id === id ).required = event.checked;
        } finally { }
    }

    back() {
        if (this.backType === 'main') {
            this.router.navigate([ '/' ]);
        } else {
            super.back();
        }
    }

}
