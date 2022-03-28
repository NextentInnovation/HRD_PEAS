
    create table batchevent (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        batchEventType varchar(100) not null,
        done TIMESTAMP,
        error varchar(1000),
        parameterId bigint,
        status varchar(100) not null,
        company_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table company (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        active BIT(1) default 1 not null,
        email varchar(100),
        fullName varchar(100),
        name varchar(50) not null,
        primary key (id)
    ) engine=InnoDB;

    create table companyparameter (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        code varchar(100) not null,
        name varchar(255),
        value varchar(100),
        company_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table companyvirtue (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        active BIT(1) default 1 not null,
        editvalue varchar(255),
        value varchar(255),
        company_id bigint not null,
        primary key (id)
    ) comment='Vállalati értékek' engine=InnoDB;

    create table difficulty (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        active BIT(1) default 1 not null,
        automatic BIT(1) default 0 not null,
        description varchar(255),
        multiplier decimal(5,2) default 1 not null,
        name varchar(100) not null,
        company_id bigint not null,
        primary key (id)
    ) comment='Nehézség' engine=InnoDB;

    create table emailtemplate (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        code varchar(100) not null,
        content LONGTEXT not null,
        language varchar(100) not null,
        subject varchar(1000) not null,
        primary key (id)
    ) comment='Email minta' engine=InnoDB;

    create table evaluation (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        automaticStartDate datetime(6),
        deadline TIMESTAMP,
        evaluatedEndDate TIMESTAMP,
        evaluatedStartDate TIMESTAMP,
        note varchar(255),
        score decimal(8,5),
        status varchar(100) default 'BEFORE_EVALUATING' not null,
        company_id bigint not null,
        evaluator_id bigint,
        task_id bigint not null,
        primary key (id)
    ) comment='Értékelés' engine=InnoDB;

    create table evaluationselection (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        evaluation_id bigint not null,
        factoroption_id bigint not null,
        primary key (id)
    ) comment='Értékelt szempontok' engine=InnoDB;

    create table factor (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        active BIT(1) default 1 not null,
        automatic BIT(1) default 0 not null,
        name varchar(100) not null,
        company_id bigint not null,
        primary key (id)
    ) comment='Értékelési szempontok' engine=InnoDB;

    create table factoroption (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        active BIT(1) default 1 not null,
        best BIT(1) default 1,
        name varchar(100) not null,
        score decimal(4,2) not null,
        factor_id bigint not null,
        primary key (id)
    ) comment='Értékelési szempontokra adható válaszok' engine=InnoDB;

    create table label (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        code varchar(100) not null,
        label varchar(255),
        language varchar(100) not null,
        primary key (id)
    ) comment='Application Labels' engine=InnoDB;

    create table leadervirtue (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        active BIT(1) default 1 not null,
        value varchar(255),
        owner_id bigint not null,
        primary key (id)
    ) comment='Csoport értékek' engine=InnoDB;

    create table notification (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        body varchar(1000) not null,
        needGenerate BIT(1) default 0 not null,
        notifacededDay date,
        notificationType varchar(100) not null,
        readed TIMESTAMP,
        referenceType varchar(20) not null,
        sendedError varchar(1000),
        sendedStatus varchar(20) default 'NEW' not null,
        status varchar(100) not null,
        subject varchar(1000) not null,
        company_id bigint not null,
        evaluation_id bigint,
        leadervirtue_id bigint,
        period_id bigint,
        rating_id bigint,
        task_id bigint,
        to_user_id bigint not null,
        primary key (id)
    ) comment='Notifikációs megjegyzések' engine=InnoDB;

    create table notificationaction (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        createable BIT(1) default 1 not null,
        notificationType varchar(100) not null,
        sendable BIT(1) default 1 not null,
        showable BIT(1) default 1 not null,
        company_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table period (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        enddate TIMESTAMP not null,
        name varchar(100) not null,
        ratingEndDate TIMESTAMP not null,
        startdate TIMESTAMP not null,
        status varchar(100) not null,
        company_id bigint not null,
        primary key (id)
    ) comment='Periódus' engine=InnoDB;

    create table rating (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        automaticTaskScore decimal(8,5),
        cooperation BIT(1) default 1,
        deadline TIMESTAMP not null,
        gradeRecommendation varchar(1000),
        normalTaskScore decimal(8,5),
        periodScore decimal(8,5),
        ratingStartDate TIMESTAMP,
        status varchar(100) not null,
        textualEvaluation varchar(1000),
        company_id bigint not null,
        leader_id bigint,
        period_id bigint not null,
        user_id bigint not null,
        primary key (id)
    ) comment='Vezetõi minõsítés' engine=InnoDB;

    create table role (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        description varchar(255),
        external varchar(100),
        name varchar(100) default 'USER' not null,
        primary key (id)
    ) engine=InnoDB;

    create table roleitem (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        name varchar(100) not null,
        primary key (id)
    ) comment='Role Item' engine=InnoDB;

    create table rolexroleitem (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        role_id bigint not null,
        roleitem_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table systemparameter (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        code varchar(100) not null,
        name varchar(255),
        value varchar(100),
        primary key (id)
    ) comment='System parameters' engine=InnoDB;

    create table task (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        deadline TIMESTAMP,
        description varchar(255),
        endDate TIMESTAMP,
        evaluatedCount integer default 0 not null,
        evaluaterCount integer default 0 not null,
        evaluationPercentage decimal(8,5) default 0.0 not null,
        name varchar(100) not null,
        score decimal(8,5),
        startDate TIMESTAMP,
        status varchar(100) default 'PARAMETERIZATION' not null,
        taskType varchar(100) not null,
        company_id bigint not null,
        difficulty_id bigint not null,
        owner_id bigint not null,
        period_id bigint,
        primary key (id)
    ) comment='Feladatok' engine=InnoDB;

    create table taskxcompanyvirtue (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        companyvirtue_id bigint not null,
        task_id bigint not null,
        primary key (id)
    ) comment='Kapcsoló tábla a Task és CompanyVirtue között' engine=InnoDB;

    create table taskxfactor (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        required BIT(1) default 0 not null,
        factor_id bigint not null,
        task_id bigint not null,
        primary key (id)
    ) comment='Taskok és a hozzájuk rendelt értékelési szempontok' engine=InnoDB;

    create table taskxleadervirtue (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        leadervirtue_id bigint not null,
        task_id bigint not null,
        primary key (id)
    ) comment='Kapcsoló tábla a Task és LeaderVirtue között' engine=InnoDB;

    create table todo (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        deadline TIMESTAMP,
        done TIMESTAMP,
        messageCode varchar(100) not null,
        referenceType varchar(100) not null,
        status varchar(100) not null,
        toDoType varchar(100) not null,
        company_id bigint not null,
        evaluation_id bigint,
        leadervirtue_id bigint,
        period_id bigint,
        rating_id bigint,
        task_id bigint,
        to_user_id bigint not null,
        primary key (id)
    ) comment='Rendszer által generált feladatok' engine=InnoDB;

    create table tokencache (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        exp TIMESTAMP default CURRENT_TIMESTAMP not null,
        iat TIMESTAMP default CURRENT_TIMESTAMP,
        iss varchar(100) default 'Nextent' not null,
        nbf TIMESTAMP default CURRENT_TIMESTAMP,
        remoteAddress varchar(50),
        sub varchar(100) not null,
        user_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table user (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        active BIT(1) default 1 not null,
        email varchar(100),
        fullName varchar(100) not null,
        initial varchar(100),
        language varchar(10),
        organization varchar(100),
        organizationPath varchar(200),
        passwdHash varchar(100) not null,
        userName varchar(100) not null,
        company_id bigint not null,
        leader_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table userxrole (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        role_id bigint not null,
        user_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    create table virtualuser (
       id bigint not null auto_increment,
        version bigint default 0 not null,
        created_by varchar(255) default '<anonymus>' not null,
        created_at TIMESTAMP default CURRENT_TIMESTAMP not null,
        modified_by varchar(255),
        modified_at TIMESTAMP,
        active BIT(1) default 1 not null,
        componentRef varchar(100) not null,
        email varchar(100) not null,
        name varchar(100),
        validTo TIMESTAMP not null,
        company_id bigint not null,
        primary key (id)
    ) engine=InnoDB;

    alter table company 
       add constraint uk_company_name unique (name);

    alter table companyparameter 
       add constraint uk_companyparameter_name unique (company_id, code);

    alter table difficulty 
       add constraint uk_difficulty unique (company_id, name);

    alter table emailtemplate 
       add constraint uk_emailtemplate unique (language, code);

    alter table evaluation 
       add constraint uk_evaluation unique (evaluator_id, task_id, automaticStartDate);

    alter table evaluationselection 
       add constraint uk_evaluationselection unique (evaluation_id, factoroption_id);
create index idx_label_code on label (code);

    alter table label 
       add constraint uk_label_language_code unique (language, code);

    alter table notificationaction 
       add constraint uk_notificationaction unique (notificationType, company_id);

    alter table period 
       add constraint uk_period unique (company_id, enddate);

    alter table rating 
       add constraint uk_rating unique (period_id, user_id);

    alter table role 
       add constraint uk_role_name unique (name);

    alter table roleitem 
       add constraint fk_roleitem_name unique (name);

    alter table rolexroleitem 
       add constraint uk_rolexroleitem unique (role_id, roleitem_id);

    alter table systemparameter 
       add constraint uk_systemparameter_code unique (code);

    alter table taskxcompanyvirtue 
       add constraint uk_taskxcompanyvirtue unique (task_id, companyvirtue_id);

    alter table taskxleadervirtue 
       add constraint uk_taskxleadervirtue unique (task_id, leadervirtue_id);

    alter table user 
       add constraint uk_user unique (company_id, userName);

    alter table userxrole 
       add constraint uk_userxrole unique (user_id, role_id);

    alter table virtualuser 
       add constraint uk_virtualuser_email unique (company_id, email);

    alter table batchevent 
       add constraint fk_batchevent_company_id 
       foreign key (company_id) 
       references company (id);

    alter table companyparameter 
       add constraint fk_companyparameter_company_id 
       foreign key (company_id) 
       references company (id);

    alter table companyvirtue 
       add constraint fk_companyvirtue_company_id 
       foreign key (company_id) 
       references company (id);

    alter table difficulty 
       add constraint fk_difficulty_company_id 
       foreign key (company_id) 
       references company (id);

    alter table evaluation 
       add constraint fk_evaluation_company_id 
       foreign key (company_id) 
       references company (id);

    alter table evaluation 
       add constraint fk_evaluation_evaluator_id 
       foreign key (evaluator_id) 
       references user (id);

    alter table evaluation 
       add constraint fk_evaluation_task_id 
       foreign key (task_id) 
       references task (id);

    alter table evaluationselection 
       add constraint fk_evaluationselection_evaluation_id 
       foreign key (evaluation_id) 
       references evaluation (id);

    alter table evaluationselection 
       add constraint fk_evaluationselection_factoroption_id 
       foreign key (factoroption_id) 
       references factoroption (id);

    alter table factor 
       add constraint fk_factor_company_id 
       foreign key (company_id) 
       references company (id);

    alter table factoroption 
       add constraint fk_factoroption_factor_id 
       foreign key (factor_id) 
       references factor (id);

    alter table leadervirtue 
       add constraint fk_leadervirtue_user_id 
       foreign key (owner_id) 
       references user (id);

    alter table notification 
       add constraint fk_notification_company_id 
       foreign key (company_id) 
       references company (id);

    alter table notification 
       add constraint fk_notification_evaluation_id 
       foreign key (evaluation_id) 
       references evaluation (id);

    alter table notification 
       add constraint fk_notification_leadervirtue_id 
       foreign key (leadervirtue_id) 
       references leadervirtue (id);

    alter table notification 
       add constraint fk_notification_period_id 
       foreign key (period_id) 
       references period (id);

    alter table notification 
       add constraint fk_notification_rating_id 
       foreign key (rating_id) 
       references rating (id);

    alter table notification 
       add constraint fk_notification_task_id 
       foreign key (task_id) 
       references task (id);

    alter table notification 
       add constraint fk_notification_to_user_id 
       foreign key (to_user_id) 
       references user (id);

    alter table notificationaction 
       add constraint fk_notificationaction_company_id 
       foreign key (company_id) 
       references company (id);

    alter table period 
       add constraint fk_period_company_id 
       foreign key (company_id) 
       references company (id);

    alter table rating 
       add constraint fk_periodevaluation_company_id 
       foreign key (company_id) 
       references company (id);

    alter table rating 
       add constraint fk_rating_leader_id 
       foreign key (leader_id) 
       references user (id);

    alter table rating 
       add constraint fk_rating_period_id 
       foreign key (period_id) 
       references period (id);

    alter table rating 
       add constraint fk_rating_user_id 
       foreign key (user_id) 
       references user (id);

    alter table rolexroleitem 
       add constraint fk_rolexroleitem_role_id 
       foreign key (role_id) 
       references role (id);

    alter table rolexroleitem 
       add constraint fk_rolexroleitem_roleitem_id 
       foreign key (roleitem_id) 
       references roleitem (id);

    alter table task 
       add constraint fk_task_company_id 
       foreign key (company_id) 
       references company (id);

    alter table task 
       add constraint fk_task_difficulty_id 
       foreign key (difficulty_id) 
       references difficulty (id);

    alter table task 
       add constraint fk_task_owner_id 
       foreign key (owner_id) 
       references user (id);

    alter table task 
       add constraint fk_task_period_id 
       foreign key (period_id) 
       references period (id);

    alter table taskxcompanyvirtue 
       add constraint fk_taskxcompanyvirtue_companyvirtue_id 
       foreign key (companyvirtue_id) 
       references companyvirtue (id);

    alter table taskxcompanyvirtue 
       add constraint fk_taskxcompanyvirtue_task_id 
       foreign key (task_id) 
       references task (id);

    alter table taskxfactor 
       add constraint fk_taskxfactor_factor_id 
       foreign key (factor_id) 
       references factor (id);

    alter table taskxfactor 
       add constraint fk_taskxfactor_task_id 
       foreign key (task_id) 
       references task (id);

    alter table taskxleadervirtue 
       add constraint fk_taskxleadervirtue_leadervirtue_id 
       foreign key (leadervirtue_id) 
       references leadervirtue (id);

    alter table taskxleadervirtue 
       add constraint fk_taskxleadervirtue_task_id 
       foreign key (task_id) 
       references task (id);

    alter table todo 
       add constraint fk_todo_company_id 
       foreign key (company_id) 
       references company (id);

    alter table todo 
       add constraint fk_todo_evaluation_id 
       foreign key (evaluation_id) 
       references evaluation (id);

    alter table todo 
       add constraint fk_todo_leadervirtue_id 
       foreign key (leadervirtue_id) 
       references leadervirtue (id);

    alter table todo 
       add constraint fk_todo_period_id 
       foreign key (period_id) 
       references period (id);

    alter table todo 
       add constraint fk_todo_rating_id 
       foreign key (rating_id) 
       references rating (id);

    alter table todo 
       add constraint fk_todo_task_id 
       foreign key (task_id) 
       references task (id);

    alter table todo 
       add constraint fk_todo_to_user_id 
       foreign key (to_user_id) 
       references user (id);

    alter table tokencache 
       add constraint fk_tokencache_user_id 
       foreign key (user_id) 
       references user (id);

    alter table user 
       add constraint fk_user_company_id 
       foreign key (company_id) 
       references company (id);

    alter table user 
       add constraint fk_user_user_id 
       foreign key (leader_id) 
       references user (id);

    alter table userxrole 
       add constraint fk_userxrole_role_id 
       foreign key (role_id) 
       references role (id);

    alter table userxrole 
       add constraint fk_userxrole_user_id 
       foreign key (user_id) 
       references user (id);

    alter table virtualuser 
       add constraint fk_virtualuser_company_id 
       foreign key (company_id) 
       references company (id);
