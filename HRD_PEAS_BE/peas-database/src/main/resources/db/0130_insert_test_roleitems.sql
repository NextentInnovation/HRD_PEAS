--liquibase formatted sql

INSERT INTO roleitem (name) VALUES ('admin_roleitem');
INSERT INTO roleitem (name) VALUES ('user_roleitem');
INSERT INTO roleitem (name) VALUES ('hr_roleitem');
INSERT INTO roleitem (name) VALUES ('leader_roleitem');
INSERT INTO roleitem (name) VALUES ('virtual_roleitem');
INSERT INTO roleitem (name) VALUES ('common_roleitem');

SET @admin_roleitem = (select id from roleitem where name = 'admin_roleitem');
SET @user_roleitem = (select id from roleitem where name = 'user_roleitem');
SET @hr_roleitem = (select id from roleitem where name = 'hr_roleitem');
SET @leader_roleitem = (select id from roleitem where name = 'leader_roleitem');
SET @virtual_roleitem = (select id from roleitem where name = 'virtual_roleitem');
SET @common_roleitem = (select id from roleitem where name = 'common_roleitem');

SET @role_id = (select id from role where name = 'ADMIN');
insert into rolexroleitem (role_id, roleitem_id) values (@role_id, @admin_roleitem);
insert into rolexroleitem (role_id, roleitem_id) values (@role_id, @common_roleitem);

SET @role_id = (select id from role where name = 'USER');
insert into rolexroleitem (role_id, roleitem_id) values (@role_id, @user_roleitem);
insert into rolexroleitem (role_id, roleitem_id) values (@role_id, @common_roleitem);

SET @role_id = (select id from role where name = 'HR');
insert into rolexroleitem (role_id, roleitem_id) values (@role_id, @hr_roleitem);
insert into rolexroleitem (role_id, roleitem_id) values (@role_id, @common_roleitem);

SET @role_id = (select id from role where name = 'LEADER');
insert into rolexroleitem (role_id, roleitem_id) values (@role_id, @leader_roleitem);
insert into rolexroleitem (role_id, roleitem_id) values (@role_id, @common_roleitem);
