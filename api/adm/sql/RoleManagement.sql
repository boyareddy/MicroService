update role set description = 'Lab Operator', name = 'Operator' where id=2;
update role set description = 'Lab Manager', name = 'Manager' where id=3;
update role set description = 'Device', name = 'Device' where id=4;

INSERT INTO permission VALUES (88,'Create_Device');
INSERT INTO permission VALUES (89,'Update_Device');
INSERT INTO permission VALUES (90,'Delete_Device');
INSERT INTO permission VALUES (91,'Create_Order');
INSERT INTO permission VALUES (92,'Update_Order');
INSERT INTO permission VALUES (93,'Update_Order_Status');
INSERT INTO permission VALUES (94,'Validate_Container_Samples');
INSERT INTO permission VALUES (95,'Create_Container_Samples');
INSERT INTO permission VALUES (96,'Update_Container_Samples');
INSERT INTO permission VALUES (97,'Delete_ContainerSamples');
INSERT INTO permission VALUES (98,'Update_Container_samples_Flag');
INSERT INTO permission VALUES (99,'Validate_Bulk_Orders');
INSERT INTO permission VALUES (100,'Create_Bulk_Orders');
INSERT INTO permission VALUES (101,'Get_Bulk_Order_Template');
INSERT INTO permission VALUES (102,'Get_Audit_Details');

INSERT INTO role_permission VALUES (1,88);
INSERT INTO role_permission VALUES (1,89);
INSERT INTO role_permission VALUES (1,90);
INSERT INTO role_permission VALUES (1,91);
INSERT INTO role_permission VALUES (2,91);
INSERT INTO role_permission VALUES (1,92);
INSERT INTO role_permission VALUES (1,93);
INSERT INTO role_permission VALUES (1,94);
INSERT INTO role_permission VALUES (1,95);
INSERT INTO role_permission VALUES (1,96);
INSERT INTO role_permission VALUES (1,97);
INSERT INTO role_permission VALUES (1,98);
INSERT INTO role_permission VALUES (1,99);
INSERT INTO role_permission VALUES (1,100);
INSERT INTO role_permission VALUES (1,101);
INSERT INTO role_permission VALUES (1,102);

--New Permissions added for Application overview Screen
insert into permission values(103,'view_order_on_dashboard');
insert into permission values(104,'view_workflow_on_dashboard');
insert into permission values(105,'view_connections_on_dashboard');
insert into permission values(106,'view_settings_on_dashboard');

--insert into role_permission table for the above 4 new permissions
insert into role_permission values(1,103);
insert into role_permission values(1,104);
insert into role_permission values(1,105);
insert into role_permission values(1,106);

insert into role_permission values(2,103);
insert into role_permission values(2,104);
insert into role_permission values(2,105);
insert into role_permission values(2,106);

insert into role_permission values(3,103);
insert into role_permission values(3,104);
insert into role_permission values(3,105);
insert into role_permission values(3,106);

--permission added to the individual role as per role matrix
INSERT INTO role_permission VALUES (3,91);
INSERT INTO role_permission VALUES (3,92);
INSERT INTO role_permission VALUES (3,94);
INSERT INTO role_permission VALUES (3,95);
INSERT INTO role_permission VALUES (3,96);
INSERT INTO role_permission VALUES (3,97);
INSERT INTO role_permission VALUES (3,99);
INSERT INTO role_permission VALUES (3,100);
INSERT INTO role_permission VALUES (3,101);
--Add device permission added for Lab Manager role
INSERT INTO role_permission VALUES (3,88);
INSERT INTO role_permission VALUES (3,89);
INSERT INTO role_permission VALUES (3,90);

--new permission added for lab setting
INSERT INTO PERMISSION VALUES(108,'view_lab_settings');
INSERT INTO role_permission VALUES (1,108);
INSERT INTO role_permission VALUES (3,108);

--insert new permission for administration tabs
INSERT INTO PERMISSION VALUES(109,'view_user_management_tab');
INSERT INTO PERMISSION VALUES(110,'view_audit_log_tab');
INSERT INTO PERMISSION VALUES(111,'view_about_tab');
INSERT INTO PERMISSION VALUES(112,'view_backup_tab');

INSERT INTO ROLE_PERMISSION VALUES(1,109);
INSERT INTO ROLE_PERMISSION VALUES(1,110);
INSERT INTO ROLE_PERMISSION VALUES(1,111);
INSERT INTO ROLE_PERMISSION VALUES(1,112);

INSERT INTO ROLE_PERMISSION VALUES(3,109);
INSERT INTO ROLE_PERMISSION VALUES(3,110);
INSERT INTO ROLE_PERMISSION VALUES(3,111);
INSERT INTO ROLE_PERMISSION VALUES(3,112);


--Permissions added for Problem Report 
insert into permission values(107,'Get_Problem_Reports');
insert into role_permission values(2,107)
insert into role_permission values(3,107)
