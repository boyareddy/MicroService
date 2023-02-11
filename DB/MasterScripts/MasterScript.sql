-- Device Management
\connect "pas-device"

INSERT INTO public.device_type(device_type_id, active, attributes, createddate, description, editedby, is_retired, modifieddate, name, parent_device_type_id, createdby) VALUES ('fa99cb08-76bf-40c2-8dc7-9c44116dee78', true, '{"supportedProtocols": ["HL7"],"supportedProtocolVersion":["2.5.1"],"supportedAssayTypes": ["NIPT-HTP"]}', '2018-12-03 15:47:52.108', 'MagNaPure24', 'admin#@#hcl.com', 'N', '2018-12-03 15:47:52.108', 'MagNaPure24', null,'admin#@#hcl.com');

INSERT INTO public.device_type(device_type_id, active, attributes, createddate, description, editedby, is_retired, modifieddate, name, parent_device_type_id, createdby) VALUES ('fddf0abb-ba4f-463f-b100-accbc2ee6e62', true, '{"supportedProtocols": ["HL7"],"supportedProtocolVersion":["2.4"],"supportedAssayTypes": ["NIPT-dPCR"]}', '2018-12-03 15:47:52.108', 'MagNA Pure 96', 'admin#@#hcl.com', 'N', '2018-12-03 15:47:52.108', 'MagNA Pure 96', null,'admin#@#hcl.com');

INSERT INTO public.device_type(device_type_id, active, attributes, createddate, description, editedby, is_retired, modifieddate, name, parent_device_type_id, createdby) VALUES ('39746f3e-2646-4fc2-ac1e-e8a13bb1b4be', true, '{"supportedProtocols": ["REST"],"supportedAssayTypes": ["NIPT-HTP"]}', '2018-12-03 15:47:52.108', 'High Throughput sequencing', 'admin#@#hcl.com', 'N', '2018-12-03 15:47:52.108', 'High Throughput sequencing', null,'admin#@#hcl.com');

INSERT INTO public.device_type(device_type_id, active, attributes, createddate, description, editedby, is_retired, modifieddate, name, parent_device_type_id, createdby) VALUES ('7cd184f7-8989-4322-9ee4-33e68472aaf7', true, '{"supportedProtocols": ["HL7"],"supportedProtocolVersion":["2.5.1"],"supportedAssayTypes": ["NIPT-HTP","NIPT-dPCR"]}', '2018-12-03 15:47:52.108', 'LP24', 'admin#@#hcl.com', 'N', '2018-12-03 15:47:52.108', 'LP24', null,'admin#@#hcl.com');

INSERT INTO public.device_type(device_type_id, active, attributes, createddate, description, editedby, is_retired, modifieddate, name, parent_device_type_id, createdby) VALUES ('1fb02202-db3a-4fa3-bbc7-8392f966d924', true, '{"supportedProtocols": ["HL7"],"supportedProtocolVersion":["2.5","2.6","2.7","2.8","2.9","3.0"],"supportedAssayTypes": ["NIPT-HTP","NIPT-dPCR"]}', '2018-12-03 15:47:52.108', 'cobas dPCR', 'admin#@#hcl.com', 'N', '2018-12-03 15:47:52.108', 'cobas dPCR', null,'admin#@#hcl.com');

INSERT INTO public.device_type(device_type_id, active, attributes, createddate, description, editedby, is_retired, modifieddate, name, parent_device_type_id, createdby) VALUES ('fb956b2e-bce4-41cc-83ef-ea51d29e16d7', true, '{"":""}', '2018-12-03 15:47:52.108', 'Illumina Sequencer', 'admin#@#hcl.com', 'N', '2018-12-03 15:47:52.108', 'Illumina Sequencer', null,'admin#@#hcl.com');

INSERT INTO public.device_type(device_type_id, active, attributes, createddate, description, editedby, is_retired, modifieddate, name, parent_device_type_id, createdby) VALUES ('4956e7a2-e52b-4809-8606-309b6e0d5ac2', true, '{"":""}', '2019-05-27 15:47:52.108', 'Analysis SW TTv2', 'admin#@#hcl.com', 'N', '2019-05-27 15:47:52.108', 'Analysis SW TTv2', null,'admin#@#hcl.com'); 

-- Message Template Management

\connect "pas-security"

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
INSERT INTO ROLE_PERMISSION VALUES(3,111);
INSERT INTO ROLE_PERMISSION VALUES(3,112);


--Permissions added for Problem Report 
insert into permission values(107,'Get_Problem_Reports');
insert into role_permission values(1,107);
insert into role_permission values(2,107);
insert into role_permission values(3,107);


-- Message_Template

\connect "connect-adm"

INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (1, 'Y', 'UI', NULL, NULL, 'The MP24 device {0} is not registered and is sending messages to Connect. Please register the device to proceed further.', 'en_US', 'UNREGISTERED_DEVICE_MP24', 'Connections', NULL, 'Warning', 'Unregistered MP24 device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (2, 'Y', 'UI', NULL, NULL, 'The LP24 device {0} is not registered and is sending messages to Connect. Please register the device to proceed further.', 'en_US', 'UNREGISTERED_DEVICE_LP24', 'Connections', NULL, 'Warning', 'Unregistered LP24 device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (3, 'Y', 'UI', NULL, NULL, 'The MP96 device {0} is not registered and is sending messages to Connect. Please register the device to proceed further.', 'en_US', 'UNREGISTERED_DEVICE_MP96', 'Connections', NULL, 'Warning', 'Unregistered MP96 device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (4, 'Y', 'UI', NULL, NULL, 'The dPCR device {0} is not registered and is sending messages to Connect. Please register the device to proceed further.', 'en_US', 'UNREGISTERED_DEVICE_dPCR', 'Connections', NULL, 'Warning', 'Unregistered dPCR device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (5, 'Y', 'UI', NULL, NULL, 'The HL7 version used by the MP96 device {0} is invalid. Please rectify to proceed further.', 'en_US', 'INVALID_HL7_VER_MP96', 'Connections', NULL, 'Error', 'Invalid HL7 version in MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (7, 'Y', 'UI', NULL, NULL, 'The HL7 version used by the MP24 device {0} is invalid. Please rectify to proceed further.', 'en_US', 'INVALID_HL7_VER_MP24', 'Connections', NULL, 'Error', 'Invalid HL7 version in MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (8, 'Y', 'UI', NULL, NULL, 'The HL7 version used by the dPCR device {0} is invalid. Please rectify to proceed further.', 'en_US', 'INVALID_HL7_VER_dPCR', 'Connections', NULL, 'Error', 'Invalid HL7 version in dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (6, 'Y', 'UI', NULL, NULL, 'The HL7 version used by the LP24 device {0} is invalid. Please rectify to proceed further.', 'en_US', 'INVALID_HL7_VER_LP24', 'Connections', NULL, 'Error', 'Invalid HL7 version in LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (9, 'Y', 'UI', NULL, NULL, 'The device model is invalid for MP96 {0}. Please rectify to proceed further.', 'en_US', 'INVALID_DEVICE_MODEL_MP96', 'Connections', NULL, 'Error', 'Invalid MP96 device model', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (10, 'Y', 'UI', NULL, NULL, 'The device model is invalid for LP24 {0}. Please rectify to proceed further.', 'en_US', 'INVALID_DEVICE_MODEL_LP24', 'Connections', NULL, 'Error', 'Invalid LP24 device model', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (11, 'Y', 'UI', NULL, NULL, 'The device model is invalid for MP24 {0}. Please rectify to proceed further.', 'en_US', 'INVALID_DEVICE_MODEL_MP24', 'Connections', NULL, 'Error', 'Invalid MP24 device model', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (12, 'Y', 'UI', NULL, NULL, 'The device model is invalid for dPCR {0}. Please rectify to proceed further.', 'en_US', 'INVALID_DEVICE_MODEL_dPCR', 'Connections', NULL, 'Error', 'Invalid dPCR device model', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (13, 'Y', 'UI', NULL, NULL, 'The MP96 device {0} is offline. Please establish connectivity to receive messages.', 'en_US', 'DEVICE_OFFLINE_MP96', 'Connections', NULL, 'Error', 'MP96 device offline', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (14, 'Y', 'UI', NULL, NULL, 'The LP24 device {0} is offline. Please establish connectivity to receive messages.', 'en_US', 'DEVICE_OFFLINE_LP24', 'Connections', NULL, 'Error', 'LP24 device offline', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (15, 'Y', 'UI', NULL, NULL, 'The MP24 device {0} is offline. Please establish connectivity to receive messages.', 'en_US', 'DEVICE_OFFLINE_MP24', 'Connections', NULL, 'Error', 'MP24 device offline', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (16, 'Y', 'UI', NULL, NULL, 'The dPCR device {0} is offline. Please establish connectivity to receive messages.', 'en_US', 'DEVICE_OFFLINE_dPCR', 'Connections', NULL, 'Error', 'dPCR device offline', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (17, 'Y', 'UI', NULL, NULL, 'Invalid Plate ID {0} is received from dPCR device {1}. Please rectify to proceed further.', 'en_US', 'INVALID_PLATE_ID_DPCR', 'Workflow', NULL, 'Error', 'Invalid Plate ID received from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (18, 'Y', 'UI', NULL, NULL, 'Connect has multiple sample mappings for the plate {0} from the dPCR device {1}. Please remove the duplicate values for plate ID to proceed further.', 'en_US', 'DUPLICATE_PLATE_ID_DPCR', 'Workflow', NULL, 'Error', 'Duplicate Plate ID received from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (19, 'Y', 'UI', NULL, NULL, 'The plate information for the plate ID {0} received from dPCR device {1} is incomplete. Please rectify to proceed further.', 'en_US', 'INCOMPLETE_PLATE_DPCR', 'Workflow', NULL, 'Error', 'Incomplete Plate ID information from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (20, 'Y', 'UI', NULL, NULL, 'No mapping exists for multiple samples in the run {0}. Please map the Accessioning IDs to the 96 well plate and location to proceed further.', 'en_US', 'ORDER_MAPPING_MISSING_MP96', 'Orders', NULL, 'Error', 'Order from MP96 is not mapped in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (21, 'Y', 'UI', NULL, NULL, 'No order exists for the {0} from the LP24 device {1}. Please enter a valid order to proceed further.', 'en_US', 'NO_ORDER_FOUND_LP24', 'Orders', NULL, 'Error', 'Order from LP24 not found in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (22, 'Y', 'UI', NULL, NULL, 'No order exists for the {0} from the MP24 device {1}. Please enter a valid order to proceed further.', 'en_US', 'NO_ORDER_FOUND_MP24', 'Orders', NULL, 'Error', 'Order from MP24 not found in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (23, 'Y', 'UI', NULL, NULL, 'No order exists for the {0} from the dPCR device {1}. Please enter a valid order to proceed further.', 'en_US', 'NO_ORDER_FOUND_dPCR', 'Orders', NULL, 'Error', 'Order from dPCR not found in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (24, 'Y', 'UI', NULL, NULL, 'Equipment status of the dPCR device {0} message has missing fields. Please rectify to proceed further.', 'en_US', 'MISSING_INFO_DPCR', 'Orders', NULL, 'Error', 'Missing mandatory details from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (25, 'Y', 'UI', NULL, NULL, 'The query message received from dPCR is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_QUERY_MSG_DPCR', 'Workflow', NULL, 'Warning', 'Unsupported query message from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (26, 'Y', 'UI', NULL, NULL, 'The status message received from dPCR is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_STATUS_MSG_DPCR', 'Workflow', NULL, 'Warning', 'Unsupported status message from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (27, 'Y', 'UI', NULL, NULL, 'The order {0} has some mandatory fields missing from MP24 device {1}. Please rectify to proceed further.', 'en_US', 'MISSING_INFO_MP24', 'Orders', NULL, 'Error', 'Missing mandatory details from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (28, 'Y', 'UI', NULL, NULL, 'The order {0} has some mandatory fields missing from LP24 device {1}. Please rectify to proceed further.', 'en_US', 'MISSING_INFO_LP24', 'Orders', NULL, 'Error', 'Missing mandatory details from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (29, 'Y', 'UI', NULL, NULL, 'The query message received from MP24 is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_QUERY_MSG_MP24', 'Workflow', NULL, 'Warning', 'Unsupported query message from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (30, 'Y', 'UI', NULL, NULL, 'The status message received from MP24 is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_STATUS_MSG_MP24', 'Workflow', NULL, 'Warning', 'Unsupported status message from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (31, 'Y', 'UI', NULL, NULL, 'The query message received from LP24 is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_QUERY_MSG_LP24', 'Workflow', NULL, 'Warning', 'Unsupported query message from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (32, 'Y', 'UI', NULL, NULL, 'The status message received from LP24 is unsupported. Please check the values to proceed further', 'en_US', 'UNSUPPORTED_STATUS_MSG_LP24', 'Workflow', NULL, 'Warning', 'Unsupported status message from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (33, 'Y', 'UI', NULL, NULL, 'Equipment status of the dPCR device {0}  message has missing fields. Please rectify to proceed further.', 'en_US', 'MISSING_INFO_DEVICE_DPCR', 'Connections', NULL, 'Error', 'Missing equipment status details', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (34, 'Y', 'UI', NULL, NULL, 'The order response has been rejected by the dPCR device {0} for the order ID {1}. Please rectify to proceed further.', 'en_US', 'ORDER_RESPONSE_REJ_dPCR', 'Workflow', NULL, 'Error', 'Order response rejected by dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (35, 'Y', 'UI', NULL, NULL, 'The sample {0} loaded on MP96 device {1} is invalid. Please rectify to proceed further.', 'en_US', 'INVALID_SAMPLE_MP96', 'Orders', NULL, 'Error', 'Invalid sample loaded on MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (37, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1},

The HL7 version used by the MP24 device {0} is invalid. 
Please rectify to proceed further. 

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_HL7_VER_MP24', 'Connections', 1, 'Error', 'Invalid HL7 version - MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (38, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1},
                 
The HL7 version used by the LP24 device {0} is invalid. 
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_HL7_VER_LP24', 'Connections', 1, 'Error', 'Invalid HL7 version - LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (39, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1},

The HL7 version used by the MP96 device {0} is invalid. 
Please rectify to proceed further. 

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_HL7_VER_MP96', 'Connections', 1, 'Error', 'Invalid HL7 version - MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (40, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1},
                 
The HL7 version used by the dPCR device {0} is invalid. 
Please rectify to proceed further. 

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_HL7_VER_dPCR', 'Connections', 1, 'Error', 'Invalid HL7 version - dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (41, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The device model is invalid for the MP96 device {0}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_DEVICE_MODEL_MP96', 'Connections', 1, 'Error', 'Invalid device model - MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (42, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The device model is invalid for the LP24 device {0}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_DEVICE_MODEL_LP24', 'Connections', 1, 'Error', 'Invalid device model - LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (43, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The device model is invalid for the MP24 device {0}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_DEVICE_MODEL_MP24', 'Connections', 1, 'Error', 'Invalid device model - MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (44, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
            
The device model is invalid for the dPCR device {0}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_DEVICE_MODEL_dPCR', 'Connections', 1, 'Error', 'Invalid device model - dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (45, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                  
The MP96 device {0} is offline. 
Please establish connectivity to receive messages.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'DEVICE_OFFLINE_MP96', 'Connections', 1, 'Error', 'Device offline - MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (46, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                  
The MP24 device {0} is offline. 
Please establish connectivity to receive messages.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'DEVICE_OFFLINE_MP24', 'Connections', 1, 'Error', 'Device offline - MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (47, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                  
The LP24 device {0} is offline. 
Please establish connectivity to receive messages.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'DEVICE_OFFLINE_LP24', 'Connections', 1, 'Error', 'Device offline - LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (48, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1},

The dPCR device {0} is offline. 
Please establish connectivity to receive messages.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'DEVICE_OFFLINE_dPCR', 'Connections', 1, 'Error', 'Device offline - dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (49, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {4}, 
             
No mapping exists for the Accessioning ID {2} in the 96 well plate location {3}.
Please map the Accessioning ID accordingly to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'ORDER_MAPPING_MISSING_MP96', 'Orders', 1, NULL, 'Order mapping not found - {1} in MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (50, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2}, 
                   
No order exists for the Order ID {0} from the MP24 device {1}. 
Please enter a valid order to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'NO_ORDER_FOUND_MP24', 'Orders', 1, 'Error', 'Order from MP24 is not found in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (51, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2}, 

No order exists for the Order ID {0} from LP24 device {1}.
Please enter a valid order to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'NO_ORDER_FOUND_LP24', 'Orders', 1, 'Error', 'Order from LP24 is not found in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (52, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2}, 

The Plate ID {0} received from dPCR device {1} is invalid.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_PLATE_ID_DPCR', 'Workflow', 1, 'Error', 'Invalid Plate ID received from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (53, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 

The Plate ID {0} received from dPCR is incomplete.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INCOMPLETE_PLATE_DPCR', 'Workflow', 1, 'Error', 'Incomplete Plate information from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (54, 'N', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 

Connect has multiple sample mappings for the plate ID {0}.
Please remove the duplicate values to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'DUPLICATE_PLATE_ID_DPCR', 'Workflow', 1, 'Error', 'Duplicate Plate ID received from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (55, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {0}, 

The query message received from dPCR is unsupported.
Please check the values to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'UNSUPPORTED_QUERY_MSG_DPCR', 'Workflow', 1, NULL, 'Unsupported query message from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (56, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {0},

The status message received from dPCR is unsupported.
Please check the values to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'UNSUPPORTED_STATUS_MSG_DPCR', 'Workflow', 1, NULL, 'Unsupported status message from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (57, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 

The HL7 message received from dPCR device {0} is unsupported.
Please check the values to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'UNSUPPORTED_HL7_MSG_DPCR', 'Workflow', 1, 'Warning', 'Unsupported HL7 message from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (58, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The HL7 message received from MP24 device {0} is unsupported.
Please check the values to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'UNSUPPORTED_HL7_MSG_MP24', 'Workflow', 1, 'Warning', 'Unsupported HL7 message from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (59, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The HL7 message received from LP24 device {0} is unsupported. 
Please check the values to proceed further.

Regards
Connect Admin Team

</pre></body></html>', 'en_US', 'UNSUPPORTED_HL7_MSG_LP24', 'Workflow', 1, 'Warning', 'Unsupported HL7 message from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (88, 'Y', 'UI', NULL, NULL, 'The result for order {0} has been rejected by the LP24 device {1}. Please rectify to proceed further.', 'en_US', 'RESULT_REJECTED_LP24', 'Workflow', NULL, 'Error', 'Result rejected by LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (60, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The HL7 message received from MP96 device {0} is unsupported.
Please check the values to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'UNSUPPORTED_HL7_MSG_MP96', 'Workflow', 1, 'Warning', 'Unsupported HL7 message from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (61, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1},

The sample {0} has some mandatory fields missing. 
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'MISSING_INFO_MP96', 'Orders', 1, 'Error', 'Missing mandatory details from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (62, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The Run ID {0} received from MP96 is invalid.
Please rectify to proceed further.

Regards
Connect Admin Team

</pre></body></html>', 'en_US', 'INVALID_RUN_ID_MP96', 'Orders', 1, NULL, 'Invalid Run ID received from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (63, 'Y', 'UI', NULL, NULL, 'The HL7 message received from dPCR device {0} is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_HL7_MSG_DPCR', 'Workflow', NULL, 'Warning', 'Unsupported HL7 message from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (64, 'Y', 'UI', NULL, NULL, 'The HL7 message received from MP24 device {0} is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_HL7_MSG_MP24', 'Workflow', NULL, 'Warning', 'Unsupported HL7 message from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (65, 'Y', 'UI', NULL, NULL, 'The HL7 message received from LP24 device {0} is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_HL7_MSG_LP24', 'Workflow', NULL, 'Warning', 'Unsupported HL7 message from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (66, 'Y', 'UI', NULL, NULL, 'The HL7 message received from MP96 device {0} is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_HL7_MSG_MP96', 'Workflow', NULL, 'Warning', 'Unsupported HL7 message from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (67, 'Y', 'UI', NULL, NULL, 'The order {0} has some mandatory fields missing from MP96 device {1}. Please rectify to proceed further.', 'en_US', 'MISSING_INFO_MP96', 'Orders', NULL, 'Error', 'Missing mandatory details from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (68, 'Y', 'UI', NULL, NULL, 'The Run ID {0} received from MP96 device {1} is invalid. Please rectify to proceed further.', 'en_US', 'INVALID_RUN_ID_MP96', 'Workflow', NULL, 'Error', 'Invalid Run ID received from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (69, 'Y', 'UI', NULL, NULL, 'The run details received for the run {0} from HTP device {1}  is incorrect / missing. Please rectify to proceed further.', 'en_US', 'INCORRECT_RUN_DETAILS_HTP', 'Workflow', NULL, 'Error', 'Incorrect/missing run details from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (70, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
The run details received for the run {0} from HTP device {1}  is incorrect / missing.
Please rectify to proceed further. 

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INCORRECT_RUN_DETAILS_HTP', 'Workflow', 1, 'Error', 'Incorrect/missing run details from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (71, 'Y', 'UI', NULL, NULL, 'The run status {0} received from HTP device {1} is invalid. Please rectify to proceed further.', 'en_US', 'INVALID_RUN_STATUS_RECEIVED_FROM_HTP', 'Workflow', NULL, 'Error', 'Invalid run status received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (72, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
The run status {0} received from HTP device {1} is invalid. 
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_RUN_STATUS_RECEIVED_FROM_HTP', 'Workflow', 1, NULL, 'Invalid run status received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (73, 'Y', 'UI', NULL, NULL, 'The Complex ID {0} received from HTP device {1} is invalid. Please rectify to proceed further.', 'en_US', 'INVALID_COMPLX_ID_HTP', 'Orders', NULL, 'Error', 'Invalid Complex ID received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (74, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
The Complex ID {0} received from HTP device {1} is invalid. 
Please rectify to proceed further.
                     
Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_COMPLX_ID_HTP', 'Orders', 1, 'Error', 'Invalid Complex ID received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (75, 'Y', 'UI', NULL, NULL, 'The run ID {1} received from HTP device {0} is incorrect. Please rectify to proceed further.', 'en_US', 'INCORRECT_RUN_ID_HTP', 'Orders', NULL, 'Error', 'Incorrect Run ID received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (76, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},

The run ID {0} received from HTP device {1} is incorrect.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INCORRECT_RUN_ID_HTP', 'Orders', 1, 'Error', 'Incorrect Run ID received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (77, 'Y', 'UI', NULL, NULL, 'The run ID {1} has received invalid data transfer information from HTP device {0}. Please rectify to proceed further.', 'en_US', 'INVALID_DATA_TRANSFER_INFO_HTP', 'Workflow', NULL, 'Error', 'Invalid data transfer information from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (78, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},

The run ID {0} has received invalid data transfer information from HTP device {1}. 
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_DATA_TRANSFER_INFO_HTP', 'Workflow', 1, 'Error', 'Invalid data transfer information from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (79, 'Y', 'UI', NULL, NULL, 'Cycle number is missing for the run ID {1} received from the HTP device {0}. Please rectify to proceed further.', 'en_US', 'MISSING_CYCLE_NUMBER_HTP', 'Workflow', NULL, 'Error', 'Missing cycle number from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (80, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},

Cycle number is missing for the run ID {0} received from the HTP device {1}. 
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'MISSING_CYCLE_NUMBER_HTP', 'Workflow', 1, NULL, 'Missing cycle number from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (81, 'Y', 'UI', NULL, NULL, 'The file received exceeds the allowed size limit for run {1} in HTP device {0}. Please rectify to proceed further.', 'en_US', 'FILE_SIZE_EXCEEDS_LIMIT_HTP', 'Workflow', NULL, 'Warning', 'File exceeds allowed size limit in HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (82, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},

The file received exceeds the allowed size limit for run {0} in HTP device {1}. 
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'FILE_SIZE_EXCEEDS_LIMIT_HTP', 'Workflow', 1, 'Warning', 'File exceeds allowed size limit in HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (83, 'Y', 'UI', NULL, NULL, 'The HTP device {0} is not registered and is sending messages to Connect. Please register the device to proceed further.', 'en_US', 'UNREGISTERED_DEVICE_HTP', 'Connections', NULL, 'Warning', 'Unregistered HTP device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (84, 'Y', 'UI', NULL, NULL, 'Multiple status messages of different values have been received from the LP24 device {0} for the order {1} . Please check the values to proceed further.', 'en_US', 'MUL_MSG_DIFF_STATUS_LP24', 'Workflow', NULL, 'Warning', 'Multiple status messages received from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (85, 'Y', 'UI', NULL, NULL, 'Multiple status messages of different values have been received from the MP96 device {0} for multiple orders of the plate {1}. Please check the values to proceed further.', 'en_US', 'MUL_MSG_DIFF_STATUS_MP96', 'Workflow', NULL, 'Warning', 'Multiple status messages received from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (86, 'Y', 'UI', NULL, NULL, 'Multiple status messages of different values have been received from the dPCR device {0} of the run {1}. Please check the values to proceed further.', 'en_US', 'MUL_MSG_DIFF_STATUS_dPCR', 'Workflow', NULL, 'Warning', 'Multiple status messages received from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (87, 'Y', 'UI', NULL, NULL, 'The equipment status received from the LP24 device {0} is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_EQUIP_STATUS_LP24', 'Connections', NULL, 'Error', 'Unsupported equipment status from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (89, 'Y', 'UI', NULL, NULL, 'The result for order {0} has been rejected by the MP96 device {1}. Please rectify to proceed further.', 'en_US', 'RESULT_REJECTED_MP96', 'Workflow', NULL, 'Error', 'Result rejected by MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (90, 'Y', 'UI', NULL, NULL, 'The result for order {0} has been rejected by the dPCR device {1}. Please rectify to proceed further.', 'en_US', 'RESULT_REJECTED_dPCR', 'Workflow', NULL, 'Error', 'Result rejected by dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (91, 'Y', 'UI', NULL, NULL, 'For the results received from MP96 device {0} for Plate {1}, no mapping exists in Connect. Please rectify to proceed further.', 'en_US', 'PLATE_RESULTS_MAPPING_MISSING_MP96', 'Orders', NULL, 'Error', 'No mapping exists for plate in MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (92, 'Y', 'UI', NULL, NULL, 'The equipment status received from the MP24 device {0} is unsupported. Please check the values to proceed further.', 'en_US', 'UNSUPPORTED_EQUIP_STATUS_MP24', 'Connections', NULL, 'Error', 'Unsupported equipment status from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (93, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1},

The equipment status received from the MP24 device {0} is unsupported.  
Please check the values to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'UNSUPPORTED_EQUIP_STATUS_MP24', 'Connections', 1, 'Error', 'Unsupported equipment status from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (94, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1},
                 
The equipment status received from the LP24 device {0} is unsupported.
Please check the values to proceed further. 

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'UNSUPPORTED_EQUIP_STATUS_LP24', 'Connections', 1, 'Error', 'Unsupported equipment status from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (95, 'Y', 'UI', NULL, NULL, 'Multiple status messages of different values have been received from the MP24 device {0} for the order {1} . Please check the values to proceed further.', 'en_US', 'MUL_MSG_DIFF_STATUS_MP24', 'Workflow', NULL, 'Warning', 'Multiple status messages received from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (96, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
Multiple status messages of different values have been received from the MP24 device {0} for the order {1}.
Please check the values to proceed further.                     

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'MUL_MSG_DIFF_STATUS_MP24', 'Workflow', 1, 'Warning', 'Multiple status messages received from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (97, 'Y', 'UI', NULL, NULL, 'Multiple status messages of different values have been received from the LP24 device {0} for the order {1} . Please check the values to proceed further.', 'en_US', 'MUL_MSG_DIFF_STATUS_LP24', 'Workflow', NULL, 'Warning', 'Multiple status messages received from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (98, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
Multiple status messages of different values have been received from the LP24 device {0} for the order {1}.
Please check the values to proceed further.                     

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'MUL_MSG_DIFF_STATUS_LP24', 'Workflow', 1, 'Warning', 'Multiple status messages received from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (99, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2}, 

The order {0} has some mandatory fields missing from MP24 device {1}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'MISSING_INFO_MP24', 'Orders', 1, 'Error', 'Missing mandatory details from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (100, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2}, 

The order {0} has some mandatory fields missing from LP24 device {1}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'MISSING_INFO_LP24', 'Orders', 1, 'Error', 'Missing mandatory details from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (101, 'Y', 'UI', NULL, NULL, 'The device ID is invalid for the HTP device {0}. Please rectify to proceed further.', 'en_US', 'INVALID_DEVICE_ID_HTP', 'Connections', NULL, 'Error', 'Invalid HTP device ID', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (102, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 

The device ID is invalid for the HTP device {0}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_DEVICE_ID_HTP', 'Connections', 1, 'Error', 'Invalid HTP device ID', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (103, 'Y', 'UI', NULL, NULL, 'The HTP device {0} is offline. Please establish connectivity to receive messages.', 'en_US', 'DEVICE_OFFLINE_HTP', 'Connections', NULL, 'Error', 'HTP device offline', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (104, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 

The HTP device {0} is offline.
Please establish connectivity to receive messages.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'DEVICE_OFFLINE_HTP', 'Connections', 1, 'Error', 'HTP device offline', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (105, 'Y', 'UI', NULL, NULL, 'Cycle number received from the HTP device {0} is out of sequence for the run ID {1} . The cycle number(s) {2} are missing. Please rectify to proceed further.', 'en_US', 'OUT_OF_SEQ_CYCLE_NUMBER_HTP', 'Workflow', NULL, 'Error', 'Out of sequence cycle number from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (106, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {3}, 

Cycle number received from the HTP device {0} is out of sequence for the run ID {1} . The cycle number(s) {2} are missing. Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'OUT_OF_SEQ_CYCLE_NUMBER_HTP', 'Workflow', 1, 'Error', 'Out of sequence cycle number from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (107, 'Y', 'UI', NULL, NULL, 'HTP device {0} has sent a message that is out of sequence for the run ID {1}. Please rectify to proceed further.', 'en_US', 'OUT_OF_SEQ_MSG_HTP', 'Workflow', NULL, 'Error', 'Out of sequence message from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (108, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2}, 

HTP device {0} has sent a message that is out of sequence for the run ID {1}. Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'OUT_OF_SEQ_MSG_HTP', 'Workflow', 1, 'Error', 'Out of sequence message from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (109, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The MP24 device {0} is not registered and is sending messages to Connect.
Please register the device to proceed further.

Regards
Connect Admin Team                             
</pre></body></html>', 'en_US', 'UNREGISTERED_DEVICE_MP24', 'Connections', 1, 'Warning', 'Unregistered MP24 device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (110, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The LP24 device {0} is not registered and is sending messages to Connect.
Please register the device to proceed further. 

Regards
Connect Admin Team                             
</pre></body></html>', 'en_US', 'UNREGISTERED_DEVICE_LP24', 'Connections', 1, 'Warning', 'Unregistered LP24 device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (111, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The MP96 device {0} is not registered and is sending messages to Connect.
Please register the device to proceed further. 

Regards
Connect Admin Team                             
</pre></body></html>', 'en_US', 'UNREGISTERED_DEVICE_MP96', 'Connections', 1, 'Warning', 'Unregistered MP96 device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (112, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The dPCR device {0} is not registered and is sending messages to Connect.
Please register the device to proceed further. 

Regards
Connect Admin Team                             
</pre></body></html>', 'en_US', 'UNREGISTERED_DEVICE_dPCR', 'Connections', 1, 'Warning', 'Unregistered dPCR device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (113, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2}, 
                   
No order exists for the Order ID {0} from the dPCR device {1}. 
Please enter a valid order to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'NO_ORDER_FOUND_dPCR', 'Orders', 1, 'Error', 'Order from dPCR is not found in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (114, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1},

Equipment status of the dPCR device {0} message has missing fields.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'MISSING_INFO_DPCR', 'Connections', 1, 'Error', 'Missing mandatory details from DPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (115, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
The Sample {0} loaded on MP96 device {1} is invalid. 
Please rectify to proceed further.
                     
Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_SAMPLE_MP96', 'Orders', 1, 'Error', 'Invalid sample loaded on MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (116, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {1}, 
                   
The HTP device {0} is not registered and is sending messages to Connect.
Please register the device to proceed further. 

Regards
Connect Admin Team                             
</pre></body></html>', 'en_US', 'UNREGISTERED_DEVICE_HTP', 'Connections', 1, 'Warning', 'Unregistered HTP device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (117, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
            
Multiple status messages of different values have been received from the MP96 device {0} for multiple orders of the plate {1} . 
Please check the values to proceed further.                     

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'MUL_MSG_DIFF_STATUS_MP96', 'Workflow', 1, 'Warning', 'Multiple status messages received from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (118, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
            
Multiple status messages of different values have been received from the dPCR device {0} of the run {1} . 
Please check the values to proceed further.                     

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'MUL_MSG_DIFF_STATUS_dPCR', 'Workflow', 1, 'Warning', 'Multiple status messages received from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (119, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},

The result for order {0} has been rejected by the LP24 device {1}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'RESULT_REJECTED_LP24', 'Workflow', 1, 'Error', 'Result rejected by LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (120, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
The result for order {0} has been rejected by the MP96 device {1}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'RESULT_REJECTED_MP96', 'Workflow', 1, 'Error', 'Result rejected by MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (121, 'Y', 'UI', NULL, NULL, 'The result for order {0} has been rejected by the MP24 device {1}. Please rectify to proceed further.', 'en_US', 'RESULT_REJECTED_MP24', 'Workflow', NULL, 'Error', 'Result rejected by MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (122, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
The result for order {0} has been rejected by the MP24 device {1}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'RESULT_REJECTED_MP24', 'Workflow', 1, 'Error', 'Result rejected by MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (123, 'Y', 'UI', NULL, NULL, 'The run status {0} received from the HTP device {1} is invalid. Please rectify to proceed further.', 'en_US', 'INVALID_RUN_STATUS_HTP', 'Workflow', NULL, 'Error', 'Invalid run status received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (124, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
The result for order {0} has been rejected by the MP24 device {1}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'INVALID_RUN_STATUS_HTP', 'Workflow', 1, 'Error', 'Invalid run status received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (125, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},
                 
The result for run {0} has been rejected by the dPCR device {1}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'RESULT_REJECTED_dPCR', 'Workflow', 1, 'Error', 'Result rejected by dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (126, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
Dear {2},

The order response has been rejected by the dPCR device {0} for the run {1}.
Please rectify to proceed further.

Regards
Connect Admin Team
</pre></body></html>', 'en_US', 'ORDER_RESPONSE_REJ_dPCR', 'Workflow', 1, 'Error', 'Order response rejected by dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (129, 'Y', 'UI', NULL, NULL, 'DeviceType:{0};DeviceName:{1};DeviceSerialNumber:{2};DescriptionCode:{3};Message:{4}', 'en_US', 'INSTRU_TRIGGER_HTP_3', 'Connections', NULL, 'Error', 'HTP triggered notification', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (169, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1},
L''état de l''équipement reçu du périphérique MP24 {0} n''est pas pris en charge.
Veuillez vérifier les valeurs pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'UNSUPPORTED_EQUIP_STATUS_MP24', 'Connections', 1, 'Error', 'Unsupported equipment status from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (127, 'Y', 'UI', NULL, NULL, 'DeviceType:{0};DeviceName:{1};DeviceSerialNumber:{2};DescriptionCode:{3};Message:{4}', 'en_US', 'INSTRU_TRIGGER_HTP_1', 'Connections', NULL, 'Error', 'HTP triggered notification', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (128, 'Y', 'UI', NULL, NULL, 'DeviceType:{0};DeviceName:{1};DeviceSerialNumber:{2};DescriptionCode:{3};Message:{4}', 'en_US', 'INSTRU_TRIGGER_HTP_2', 'Connections', NULL, 'Warning', 'HTP triggered notification', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (130, 'Y', 'UI', NULL, NULL, '{0} System backup has failed to start on {1}.', 'en_US', 'BACKUP_TRIGGER_GEN_1', 'Backup', NULL, 'Warning', 'Connect system backup', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (131, 'Y', 'UI', NULL, NULL, '{0} System backup has failed on {1}', 'en_US', 'BACKUP_TRIGGER_GEN_2', 'Backup', NULL, 'Warning', 'Connect system backup', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (134, 'Y', 'UI', NULL, NULL, 'Error occurred while creating problem report for the date range {0} to {1}. Please try again.', 'en_US', 'PRBLM_RPRT_NOT_GEN', 'Administration', NULL, 'Error', 'Problem report not created', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (133, 'Y', 'UI', NULL, NULL, 'Problem report successfully created for the date range {0} to {1}.', 'en_US', 'PRBLM_RPRT_GEN', 'Administration', NULL, 'Info', 'Problem report created', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (132, 'Y', 'UI', NULL, NULL, '{0} System backup has been completed on {1}.', 'en_US', 'BACKUP_TRIGGER_GEN_3', 'Backup', NULL, 'Info', 'Connect system backup', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (135, 'Y', 'UI', NULL, NULL, '{0} full system backup has been started on {1}', 'en_US', 'BACKUP_TRIGGER_GEN_4', 'Backup', NULL, 'Info', 'Backup started', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (170, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1},
                 

L''état de l''équipement reçu du périphérique LP24 {0} n''est pas pris en charge.
Veuillez vérifier les valeurs pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'UNSUPPORTED_EQUIP_STATUS_LP24', 'Connections', 1, 'Error', 'Unsupported equipment status from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (136, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1},


La version HL7 utilisée par le périphérique MP24 {0} n''est pas valide.
Veuillez rectifier pour continuer. 

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_HL7_VER_MP24', 'Connections', 1, 'Error', 'Invalid HL7 version - MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (171, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 

Plusieurs messages d''état de valeurs différentes ont été reçus de l''appareil MP24 {0} pour l''ordre {1}.
Veuillez vérifier les valeurs pour continuer.                    

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'MUL_MSG_DIFF_STATUS_MP24', 'Workflow', 1, 'Warning', 'Multiple status messages received from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (172, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 

Plusieurs messages d''état de valeurs différentes ont été reçus de l''appareil LP24 {0} pour l''ordre {1}.
Veuillez vérifier les valeurs pour continuer.                     

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'MUL_MSG_DIFF_STATUS_LP24', 'Workflow', 1, 'Warning', 'Multiple status messages received from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (173, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2}, 

Il manque dans la commande {0} des champs obligatoires du périphérique MP24 {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'MISSING_INFO_MP24', 'Orders', 1, 'Error', 'Missing mandatory details from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (174, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2}, 

La commande {0} contient des champs obligatoires manquants sur le périphérique LP24 {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'MISSING_INFO_LP24', 'Orders', 1, 'Error', 'Missing mandatory details from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (175, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 

L''ID de périphérique n''est pas valide pour le périphérique HTP {0}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_DEVICE_ID_HTP', 'Connections', 1, 'Error', 'Invalid HTP device ID', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (176, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 

Le périphérique HTP {0} est hors ligne.
Veuillez établir la connectivité pour recevoir des messages.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'DEVICE_OFFLINE_HTP', 'Connections', 1, 'Error', 'HTP device offline', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (177, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {3}, 


Le numéro de cycle reçu du périphérique HTP {0} est hors séquence pour l''ID d''analyse {1}. Le ou les numéros de cycle {2} sont manquants. Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'OUT_OF_SEQ_CYCLE_NUMBER_HTP', 'Workflow', 1, 'Error', 'Out of sequence cycle number from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (178, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2}, 


Le périphérique HTP {0} a envoyé un message hors séquence pour l''ID d''analyse {1}. Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'OUT_OF_SEQ_MSG_HTP', 'Workflow', 1, 'Error', 'Out of sequence message from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (179, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   

Le périphérique MP24 {0} n''est pas enregistré et envoie des messages à Connect.
Veuillez enregistrer l''appareil pour continuer.

Cordialement
Équipe d''administration connect                             
</pre></body></html>', 'French', 'UNREGISTERED_DEVICE_MP24', 'Connections', 1, 'Warning', 'Unregistered MP24 device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (180, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   

Le périphérique LP24 {0} n''est pas enregistré et envoie des messages à Connect.
Veuillez enregistrer l''appareil pour continuer. 

Cordialement
Équipe d''administration connect                             
</pre></body></html>', 'French', 'UNREGISTERED_DEVICE_LP24', 'Connections', 1, 'Warning', 'Unregistered LP24 device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (181, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   

Le périphérique MP96 {0} n''est pas enregistré et envoie des messages à Connect.
Veuillez enregistrer l''appareil pour continuer.

Cordialement
Équipe d''administration connect                             
</pre></body></html>', 'French', 'UNREGISTERED_DEVICE_MP96', 'Connections', 1, 'Warning', 'Unregistered MP96 device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (182, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   
Le périphérique dPCR {0} n''est pas enregistré et envoie des messages à Connect.
Veuillez enregistrer l''appareil pour continuer.

Cordialement
Équipe d''administration connect                             
</pre></body></html>', 'French', 'UNREGISTERED_DEVICE_dPCR', 'Connections', 1, 'Warning', 'Unregistered dPCR device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (183, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2}, 
                   

Aucune commande n''existe pour l''ID de commande {0} à partir du périphérique dPCR {1}.
Veuillez entrer une commande valide pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'NO_ORDER_FOUND_dPCR', 'Orders', 1, 'Error', 'Order from dPCR is not found in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (184, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1},

L''état de l''équipement du message du périphérique dPCR {0} comporte des champs manquants.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'MISSING_INFO_DPCR', 'Connections', 1, 'Error', 'Missing mandatory details from DPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (185, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 

L''échantillon {0} chargé sur le périphérique MP96 {1} n''est pas valide.
Veuillez rectifier pour continuer.
                     
Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_SAMPLE_MP96', 'Orders', 1, 'Error', 'Invalid sample loaded on MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (186, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   
Le périphérique HTP {0} n''est pas enregistré et envoie des messages à Connect.
Veuillez enregistrer l''appareil pour continuer.

Cordialement
Équipe d''administration connect                             
</pre></body></html>', 'French', 'UNREGISTERED_DEVICE_HTP', 'Connections', 1, 'Warning', 'Unregistered HTP device', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (187, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
            

Plusieurs messages d''état de valeurs différentes ont été reçus de l''appareil MP96 {0} pour plusieurs ordres de la plaque {1}.
Veuillez vérifier les valeurs pour continuer.                    

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'MUL_MSG_DIFF_STATUS_MP96', 'Workflow', 1, 'Warning', 'Multiple status messages received from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (188, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
            
Plusieurs messages d''état de valeurs différentes ont été reçus du périphérique dPCR {0} de l''exécution {1}.
Veuillez vérifier les valeurs pour continuer.                    

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'MUL_MSG_DIFF_STATUS_dPCR', 'Workflow', 1, 'Warning', 'Multiple status messages received from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (189, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},

Le résultat de la commande {0} a été rejeté par le périphérique LP24 {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'RESULT_REJECTED_LP24', 'Workflow', 1, 'Error', 'Result rejected by LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (190, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 
Le résultat de la commande {0} a été rejeté par le périphérique MP96 {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'RESULT_REJECTED_MP96', 'Workflow', 1, 'Error', 'Result rejected by MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (191, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 

Le résultat de la commande {0} a été rejeté par le périphérique MP24 {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'RESULT_REJECTED_MP24', 'Workflow', 1, 'Error', 'Result rejected by MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (148, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {4}, 
             

Aucun mappage n''existe pour L''ID d''accession {2} dans l''emplacement de la plaque à 96 puits {3}.
Veuillez mapper L''ID d''acquisition en conséquence pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'ORDER_MAPPING_MISSING_MP96', 'Orders', 1, NULL, 'Order mapping not found - {1} in MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (149, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2}, 
                   

Aucune commande n''existe pour L''ID de commande {0} à partir du périphérique MP24 {1}.
Veuillez entrer une commande valide pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'NO_ORDER_FOUND_MP24', 'Orders', 1, 'Error', 'Order from MP24 is not found in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (150, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2}, 


Aucune commande n''existe pour L''ID de commande {0} à partir du périphérique LP24 {1}.
Veuillez entrer une commande valide pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'NO_ORDER_FOUND_LP24', 'Orders', 1, 'Error', 'Order from LP24 is not found in Connect', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (151, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2}, 


L''ID de plaque {0} reçu du périphérique dPCR {1} n''est pas valide.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_PLATE_ID_DPCR', 'Workflow', 1, 'Error', 'Invalid Plate ID received from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (152, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 


L''ID de plaque {0} reçu de dPCR est incomplet.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INCOMPLETE_PLATE_DPCR', 'Workflow', 1, 'Error', 'Incomplete Plate information from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (153, 'N', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 


Connect dispose de plusieurs exemples de mappages pour L''ID de plaque {0}.
Veuillez supprimer les valeurs en double pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'DUPLICATE_PLATE_ID_DPCR', 'Workflow', 1, 'Error', 'Duplicate Plate ID received from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (154, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {0}, 

Le message de requête reçu de dPCR n''est pas pris en charge.
Veuillez vérifier les valeurs pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'UNSUPPORTED_QUERY_MSG_DPCR', 'Workflow', 1, NULL, 'Unsupported query message from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (155, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {0},


Le message d''état reçu de dPCR n''est pas pris en charge.
Veuillez vérifier les valeurs pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'UNSUPPORTED_STATUS_MSG_DPCR', 'Workflow', 1, NULL, 'Unsupported status message from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (156, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 

Le message HL7 reçu du périphérique dPCR {0} n''est pas pris en charge.
Veuillez vérifier les valeurs pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'UNSUPPORTED_HL7_MSG_DPCR', 'Workflow', 1, 'Warning', 'Unsupported HL7 message from dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (157, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   

Le message HL7 reçu du périphérique MP24 {0} n''est pas pris en charge.
Veuillez vérifier les valeurs pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'UNSUPPORTED_HL7_MSG_MP24', 'Workflow', 1, 'Warning', 'Unsupported HL7 message from MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (158, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   
Le message HL7 reçu du périphérique LP24 {0} n''est pas pris en charge.
Veuillez vérifier les valeurs pour continuer.

Cordialement
Équipe d''administration connect

</pre></body></html>', 'French', 'UNSUPPORTED_HL7_MSG_LP24', 'Workflow', 1, 'Warning', 'Unsupported HL7 message from LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (159, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   

Le message HL7 reçu du périphérique MP96 {0} n''est pas pris en charge.
Veuillez vérifier les valeurs pour continuer.
Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'UNSUPPORTED_HL7_MSG_MP96', 'Workflow', 1, 'Warning', 'Unsupported HL7 message from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (160, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1},

Des exemples de champs obligatoires sont manquants dans l''exemple {0}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'MISSING_INFO_MP96', 'Orders', 1, 'Error', 'Missing mandatory details from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (161, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   

L''ID d''exécution {0} reçu de MP96 n''est pas valide.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect

</pre></body></html>', 'French', 'INVALID_RUN_ID_MP96', 'Orders', 1, NULL, 'Invalid Run ID received from MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (162, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 

Les détails d''exécution reçus pour l''exécution {0} à partir du périphérique HTP {1} sont incorrects / manquants.
Veuillez rectifier pour continuer.
Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INCORRECT_RUN_DETAILS_HTP', 'Workflow', 1, 'Error', 'Incorrect/missing run details from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (163, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 

L''état d''exécution {0} reçu du périphérique HTTP {1} n''est pas valide. Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_RUN_STATUS_RECEIVED_FROM_HTP', 'Workflow', 1, NULL, 'Invalid run status received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (164, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 

L''ID complexe {0} reçu du périphérique HTTP {1} n''est pas valide. Veuillez rectifier pour continuer.
                     
Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_COMPLX_ID_HTP', 'Orders', 1, 'Error', 'Invalid Complex ID received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (165, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},


L''ID d''exécution {0} reçu du périphérique HTP {1} est incorrect.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INCORRECT_RUN_ID_HTP', 'Orders', 1, 'Error', 'Incorrect Run ID received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (166, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},


L''ID d''exécution {0} a reçu des informations de transfert de données non valides du périphérique HTP {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_DATA_TRANSFER_INFO_HTP', 'Workflow', 1, 'Error', 'Invalid data transfer information from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (167, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},


Le numéro de cycle est manquant pour L''ID d''exécution {0} reçu du périphérique HTP {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'MISSING_CYCLE_NUMBER_HTP', 'Workflow', 1, NULL, 'Missing cycle number from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (168, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},


Le fichier reçu dépasse la taille maximale autorisée pour l''exécution {0} dans le périphérique HTP {1}. Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'FILE_SIZE_EXCEEDS_LIMIT_HTP', 'Workflow', 1, 'Warning', 'File exceeds allowed size limit in HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (137, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1},
                 
La version HL7 utilisée par le périphérique LP24 {0} n''est pas valide.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_HL7_VER_LP24', 'Connections', 1, 'Error', 'Invalid HL7 version - LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (138, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1},


La version HL7 utilisée par le périphérique MP96 {0} n''est pas valide.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_HL7_VER_MP96', 'Connections', 1, 'Error', 'Invalid HL7 version - MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (139, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1},
                 

La version HL7 utilisée par le périphérique dPCR {0} n''est pas valide.
Veuillez rectifier pour continuer. 

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_HL7_VER_dPCR', 'Connections', 1, 'Error', 'Invalid HL7 version - dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (140, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   

Le modèle de périphérique n''est pas valide pour le périphérique MP96 {0}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_DEVICE_MODEL_MP96', 'Connections', 1, 'Error', 'Invalid device model - MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (141, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   

Le modèle de périphérique n''est pas valide pour le périphérique LP24 {0}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_DEVICE_MODEL_LP24', 'Connections', 1, 'Error', 'Invalid device model - LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (142, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                   

Le modèle de périphérique n''est pas valide pour le périphérique MP24 {0}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_DEVICE_MODEL_MP24', 'Connections', 1, 'Error', 'Invalid device model - MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (143, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
            

Le modèle de périphérique n''est pas valide pour le périphérique dPCR {0}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_DEVICE_MODEL_dPCR', 'Connections', 1, 'Error', 'Invalid device model - dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (144, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                  

Le périphérique MP96 {0} est hors ligne.
Veuillez établir la connectivité pour recevoir des messages.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'DEVICE_OFFLINE_MP96', 'Connections', 1, 'Error', 'Device offline - MP96', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (145, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                  

Le périphérique MP24 {0} est hors ligne.
Veuillez établir la connectivité pour recevoir des messages.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'DEVICE_OFFLINE_MP24', 'Connections', 1, 'Error', 'Device offline - MP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (146, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1}, 
                  

Le périphérique LP24 {0} est hors ligne.
Veuillez établir la connectivité pour recevoir des messages.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'DEVICE_OFFLINE_LP24', 'Connections', 1, 'Error', 'Device offline - LP24', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (147, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {1},


Le périphérique dPCR {0} est hors ligne.
Veuillez établir la connectivité pour recevoir des messages.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'DEVICE_OFFLINE_dPCR', 'Connections', 1, 'Error', 'Device offline - dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (192, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 
Le résultat de la commande {0} a été rejeté par le périphérique MP24 {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'INVALID_RUN_STATUS_HTP', 'Workflow', 1, 'Error', 'Invalid run status received from HTP', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (193, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},
                 
Le résultat de l''exécution {0} a été rejeté par le périphérique dPCR {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'RESULT_REJECTED_dPCR', 'Workflow', 1, 'Error', 'Result rejected by dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (194, 'Y', 'EMAIL', NULL, NULL, '<html><body><pre>
cher {2},

Le résultat de l''exécution {0} a été rejeté par le périphérique dPCR {1}.
Veuillez rectifier pour continuer.

Cordialement
Équipe d''administration connect
</pre></body></html>', 'French', 'ORDER_RESPONSE_REJ_dPCR', 'Workflow', 1, 'Error', 'Order response rejected by dPCR', NULL, NULL, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_template (template_id, active_flag, channel, created_by, created_date_time, description, locale, message_group, module, priority, severity, title, updated_by, updated_date_time, companyid, createdate, createdby, editedby, modifieddate) VALUES (195, 'Y', 'UI', NULL, NULL, 'DeviceType:{0};DeviceName:{1};DeviceSerialNumber:{2};DescriptionCode:{3};Message:{4}', 'en_US', 'INSTRU_TRIGGER_HTP', 'Connections', NULL, NULL, 'HTP triggered notification', NULL, NULL, 1, NULL, NULL, NULL, NULL);


INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (1, NULL, NULL, 1, 1, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (2, NULL, NULL, 1, 1, NULL, NULL, 1, 2, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (3, NULL, NULL, 1, 1, NULL, NULL, 1, 3, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (4, NULL, NULL, 1, 1, NULL, NULL, 1, 4, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (5, NULL, NULL, 1, 1, NULL, NULL, 1, 5, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (6, NULL, NULL, 1, 1, NULL, NULL, 1, 6, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (7, NULL, NULL, 1, 1, NULL, NULL, 1, 7, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (8, NULL, NULL, 1, 1, NULL, NULL, 1, 8, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (9, NULL, NULL, 1, 1, NULL, NULL, 1, 9, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (10, NULL, NULL, 1, 1, NULL, NULL, 1, 10, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (11, NULL, NULL, 1, 1, NULL, NULL, 1, 11, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (12, NULL, NULL, 1, 1, NULL, NULL, 1, 12, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (13, NULL, NULL, 1, 1, NULL, NULL, 1, 13, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (14, NULL, NULL, 1, 1, NULL, NULL, 1, 14, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (15, NULL, NULL, 1, 1, NULL, NULL, 1, 15, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (16, NULL, NULL, 1, 1, NULL, NULL, 1, 16, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (17, NULL, NULL, 2, 1, NULL, NULL, 1, 17, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (18, NULL, NULL, 1, 1, NULL, NULL, 1, 18, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (19, NULL, NULL, 1, 1, NULL, NULL, 1, 19, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (20, NULL, NULL, 1, 1, NULL, NULL, 1, 20, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (21, NULL, NULL, 1, 1, NULL, NULL, 1, 21, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (22, NULL, NULL, 1, 1, NULL, NULL, 1, 22, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (23, NULL, NULL, 1, 1, NULL, NULL, 1, 23, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (24, NULL, NULL, 1, 1, NULL, NULL, 1, 24, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (25, NULL, NULL, 1, 1, NULL, NULL, 1, 25, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (26, NULL, NULL, 1, 1, NULL, NULL, 1, 26, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (27, NULL, NULL, 1, 1, NULL, NULL, 1, 27, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (28, NULL, NULL, 1, 1, NULL, NULL, 1, 28, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (29, NULL, NULL, 1, 1, NULL, NULL, 1, 29, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (30, NULL, NULL, 1, 1, NULL, NULL, 1, 30, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (31, NULL, NULL, 1, 1, NULL, NULL, 1, 31, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (32, NULL, NULL, 1, 1, NULL, NULL, 1, 32, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (33, NULL, NULL, 1, 1, NULL, NULL, 1, 33, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (34, NULL, NULL, 1, 1, NULL, NULL, 1, 34, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (35, NULL, NULL, 1, 1, NULL, NULL, 1, 35, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (37, NULL, NULL, 1, 1, NULL, NULL, 1, 37, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (38, NULL, NULL, 1, 1, NULL, NULL, 1, 38, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (39, NULL, NULL, 1, 1, NULL, NULL, 1, 39, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (40, NULL, NULL, 1, 1, NULL, NULL, 1, 40, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (41, NULL, NULL, 1, 1, NULL, NULL, 1, 41, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (42, NULL, NULL, 1, 1, NULL, NULL, 1, 42, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (43, NULL, NULL, 1, 1, NULL, NULL, 1, 43, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (44, NULL, NULL, 1, 1, NULL, NULL, 1, 44, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (45, NULL, NULL, 1, 1, NULL, NULL, 1, 45, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (46, NULL, NULL, 1, 1, NULL, NULL, 1, 46, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (47, NULL, NULL, 1, 1, NULL, NULL, 1, 47, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (48, NULL, NULL, 1, 1, NULL, NULL, 1, 48, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (49, NULL, NULL, 1, 1, NULL, NULL, 1, 49, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (50, NULL, NULL, 2, 1, NULL, NULL, 1, 50, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (51, NULL, NULL, 2, 1, NULL, NULL, 1, 51, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (52, NULL, NULL, 2, 1, NULL, NULL, 1, 52, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (53, NULL, NULL, 2, 1, NULL, NULL, 1, 53, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (54, NULL, NULL, 2, 1, NULL, NULL, 1, 54, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (55, NULL, NULL, 1, 1, NULL, NULL, 1, 55, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (56, NULL, NULL, 1, 1, NULL, NULL, 1, 56, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (57, NULL, NULL, 2, 1, NULL, NULL, 1, 57, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (58, NULL, NULL, 2, 1, NULL, NULL, 1, 58, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (59, NULL, NULL, 2, 1, NULL, NULL, 1, 59, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (60, NULL, NULL, 2, 1, NULL, NULL, 1, 60, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (61, NULL, NULL, 2, 1, NULL, NULL, 1, 61, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (62, NULL, NULL, 1, 1, NULL, NULL, 1, 62, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (63, NULL, NULL, 1, 1, NULL, NULL, 1, 63, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (64, NULL, NULL, 1, 1, NULL, NULL, 1, 64, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (65, NULL, NULL, 1, 1, NULL, NULL, 1, 65, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (66, NULL, NULL, 1, 1, NULL, NULL, 1, 66, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (67, NULL, NULL, 1, 1, NULL, NULL, 1, 67, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (68, NULL, NULL, 1, 1, NULL, NULL, 1, 68, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (69, NULL, NULL, 1, 1, NULL, NULL, 1, 69, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (70, NULL, NULL, 2, 1, NULL, NULL, 1, 70, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (71, NULL, NULL, 1, 1, NULL, NULL, 1, 71, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (72, NULL, NULL, 1, 1, NULL, NULL, 1, 72, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (73, NULL, NULL, 1, 1, NULL, NULL, 1, 73, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (74, NULL, NULL, 2, 1, NULL, NULL, 1, 74, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (75, NULL, NULL, 1, 1, NULL, NULL, 1, 75, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (76, NULL, NULL, 2, 1, NULL, NULL, 1, 76, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (77, NULL, NULL, 1, 1, NULL, NULL, 1, 77, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (78, NULL, NULL, 2, 1, NULL, NULL, 1, 78, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (79, NULL, NULL, 1, 1, NULL, NULL, 1, 79, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (80, NULL, NULL, 2, 1, NULL, NULL, 1, 80, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (81, NULL, NULL, 1, 1, NULL, NULL, 1, 81, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (82, NULL, NULL, 2, 1, NULL, NULL, 1, 82, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (83, NULL, NULL, 1, 1, NULL, NULL, 1, 83, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (84, NULL, NULL, 1, 1, NULL, NULL, 1, 84, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (85, NULL, NULL, 1, 1, NULL, NULL, 1, 85, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (86, NULL, NULL, 1, 1, NULL, NULL, 1, 86, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (87, NULL, NULL, 1, 1, NULL, NULL, 1, 87, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (88, NULL, NULL, 1, 1, NULL, NULL, 1, 88, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (89, NULL, NULL, 1, 1, NULL, NULL, 1, 89, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (90, NULL, NULL, 1, 1, NULL, NULL, 1, 90, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (91, NULL, NULL, 1, 1, NULL, NULL, 1, 91, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (92, NULL, NULL, 1, 1, NULL, NULL, 1, 92, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (93, NULL, NULL, 2, 1, NULL, NULL, 1, 93, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (94, NULL, NULL, 2, 1, NULL, NULL, 1, 94, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (95, NULL, NULL, 1, 1, NULL, NULL, 1, 95, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (96, NULL, NULL, 2, 1, NULL, NULL, 1, 96, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (97, NULL, NULL, 1, 1, NULL, NULL, 1, 97, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (98, NULL, NULL, 2, 1, NULL, NULL, 1, 98, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (99, NULL, NULL, 2, 1, NULL, NULL, 1, 99, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (100, NULL, NULL, 2, 1, NULL, NULL, 1, 100, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (101, NULL, NULL, 1, 1, NULL, NULL, 1, 101, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (102, NULL, NULL, 1, 1, NULL, NULL, 1, 102, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (103, NULL, NULL, 1, 1, NULL, NULL, 1, 103, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (104, NULL, NULL, 1, 1, NULL, NULL, 1, 104, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (105, NULL, NULL, 1, 1, NULL, NULL, 1, 105, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (106, NULL, NULL, 2, 1, NULL, NULL, 1, 106, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (107, NULL, NULL, 1, 1, NULL, NULL, 1, 107, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (108, NULL, NULL, 2, 1, NULL, NULL, 1, 108, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (109, NULL, NULL, 1, 1, NULL, NULL, 1, 109, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (110, NULL, NULL, 2, 1, NULL, NULL, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (111, NULL, NULL, 2, 1, NULL, NULL, 1, 109, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (112, NULL, NULL, 1, 1, NULL, NULL, 1, 110, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (113, NULL, NULL, 2, 1, NULL, NULL, 1, 2, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (114, NULL, NULL, 2, 1, NULL, NULL, 1, 110, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (115, NULL, NULL, 1, 1, NULL, NULL, 1, 111, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (116, NULL, NULL, 2, 1, NULL, NULL, 1, 3, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (117, NULL, NULL, 2, 1, NULL, NULL, 1, 111, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (118, NULL, NULL, 1, 1, NULL, NULL, 1, 112, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (119, NULL, NULL, 2, 1, NULL, NULL, 1, 4, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (120, NULL, NULL, 2, 1, NULL, NULL, 1, 112, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (121, NULL, NULL, 2, 1, NULL, NULL, 1, 113, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (122, NULL, NULL, 2, 1, NULL, NULL, 1, 114, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (123, NULL, NULL, 2, 1, NULL, NULL, 1, 115, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (124, NULL, NULL, 1, 1, NULL, NULL, 1, 116, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (125, NULL, NULL, 2, 1, NULL, NULL, 1, 116, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (126, NULL, NULL, 2, 1, NULL, NULL, 1, 117, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (127, NULL, NULL, 2, 1, NULL, NULL, 1, 118, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (128, NULL, NULL, 2, 1, NULL, NULL, 1, 119, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (129, NULL, NULL, 2, 1, NULL, NULL, 1, 120, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (130, NULL, NULL, 2, 1, NULL, NULL, 1, 122, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (131, NULL, NULL, 2, 1, NULL, NULL, 1, 124, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (132, NULL, NULL, 2, 1, NULL, NULL, 1, 125, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (133, NULL, NULL, 2, 1, NULL, NULL, 1, 126, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (134, NULL, NULL, 2, 1, NULL, NULL, 1, 127, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (135, NULL, NULL, 2, 1, NULL, NULL, 1, 128, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (136, NULL, NULL, 2, 1, NULL, NULL, 1, 129, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (140, NULL, NULL, 2, 1, NULL, NULL, 1, 133, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (141, NULL, NULL, 3, 1, NULL, NULL, 1, 134, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (142, NULL, NULL, 2, 1, NULL, NULL, 1, 133, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (143, NULL, NULL, 3, 1, NULL, NULL, 1, 134, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (137, NULL, NULL, 1, 1, NULL, NULL, 1, 130, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (138, NULL, NULL, 1, 1, NULL, NULL, 1, 131, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (139, NULL, NULL, 1, 1, NULL, NULL, 1, 132, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (145, NULL, NULL, 1, 1, NULL, NULL, 1, 135, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (146, NULL, NULL, 1, 1, NULL, NULL, 1, 136, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (147, NULL, NULL, 1, 1, NULL, NULL, 1, 137, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (148, NULL, NULL, 1, 1, NULL, NULL, 1, 138, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (149, NULL, NULL, 1, 1, NULL, NULL, 1, 139, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (150, NULL, NULL, 1, 1, NULL, NULL, 1, 140, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (151, NULL, NULL, 1, 1, NULL, NULL, 1, 141, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (152, NULL, NULL, 1, 1, NULL, NULL, 1, 142, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (153, NULL, NULL, 1, 1, NULL, NULL, 1, 143, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (154, NULL, NULL, 1, 1, NULL, NULL, 1, 144, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (155, NULL, NULL, 1, 1, NULL, NULL, 1, 145, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (156, NULL, NULL, 1, 1, NULL, NULL, 1, 146, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (157, NULL, NULL, 1, 1, NULL, NULL, 1, 147, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (158, NULL, NULL, 1, 1, NULL, NULL, 1, 148, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (159, NULL, NULL, 2, 1, NULL, NULL, 1, 149, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (160, NULL, NULL, 2, 1, NULL, NULL, 1, 150, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (161, NULL, NULL, 2, 1, NULL, NULL, 1, 151, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (162, NULL, NULL, 2, 1, NULL, NULL, 1, 152, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (163, NULL, NULL, 2, 1, NULL, NULL, 1, 153, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (164, NULL, NULL, 1, 1, NULL, NULL, 1, 154, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (165, NULL, NULL, 1, 1, NULL, NULL, 1, 155, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (166, NULL, NULL, 2, 1, NULL, NULL, 1, 156, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (167, NULL, NULL, 2, 1, NULL, NULL, 1, 157, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (168, NULL, NULL, 2, 1, NULL, NULL, 1, 158, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (169, NULL, NULL, 2, 1, NULL, NULL, 1, 159, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (170, NULL, NULL, 2, 1, NULL, NULL, 1, 160, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (171, NULL, NULL, 1, 1, NULL, NULL, 1, 161, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (172, NULL, NULL, 2, 1, NULL, NULL, 1, 162, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (173, NULL, NULL, 1, 1, NULL, NULL, 1, 163, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (174, NULL, NULL, 2, 1, NULL, NULL, 1, 164, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (175, NULL, NULL, 2, 1, NULL, NULL, 1, 165, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (176, NULL, NULL, 2, 1, NULL, NULL, 1, 166, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (177, NULL, NULL, 2, 1, NULL, NULL, 1, 167, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (178, NULL, NULL, 2, 1, NULL, NULL, 1, 168, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (179, NULL, NULL, 2, 1, NULL, NULL, 1, 169, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (180, NULL, NULL, 2, 1, NULL, NULL, 1, 170, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (181, NULL, NULL, 2, 1, NULL, NULL, 1, 171, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (182, NULL, NULL, 2, 1, NULL, NULL, 1, 172, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (183, NULL, NULL, 2, 1, NULL, NULL, 1, 173, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (184, NULL, NULL, 2, 1, NULL, NULL, 1, 174, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (185, NULL, NULL, 1, 1, NULL, NULL, 1, 175, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (186, NULL, NULL, 1, 1, NULL, NULL, 1, 176, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (187, NULL, NULL, 2, 1, NULL, NULL, 1, 177, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (188, NULL, NULL, 2, 1, NULL, NULL, 1, 178, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (189, NULL, NULL, 1, 1, NULL, NULL, 1, 179, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (190, NULL, NULL, 2, 1, NULL, NULL, 1, 179, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (191, NULL, NULL, 1, 1, NULL, NULL, 1, 180, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (192, NULL, NULL, 2, 1, NULL, NULL, 1, 180, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (193, NULL, NULL, 1, 1, NULL, NULL, 1, 181, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (194, NULL, NULL, 2, 1, NULL, NULL, 1, 181, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (195, NULL, NULL, 1, 1, NULL, NULL, 1, 182, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (196, NULL, NULL, 2, 1, NULL, NULL, 1, 182, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (197, NULL, NULL, 2, 1, NULL, NULL, 1, 183, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (198, NULL, NULL, 2, 1, NULL, NULL, 1, 184, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (199, NULL, NULL, 2, 1, NULL, NULL, 1, 185, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (200, NULL, NULL, 1, 1, NULL, NULL, 1, 186, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (201, NULL, NULL, 2, 1, NULL, NULL, 1, 186, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (202, NULL, NULL, 2, 1, NULL, NULL, 1, 187, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (203, NULL, NULL, 2, 1, NULL, NULL, 1, 188, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (204, NULL, NULL, 2, 1, NULL, NULL, 1, 189, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (205, NULL, NULL, 2, 1, NULL, NULL, 1, 190, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (206, NULL, NULL, 2, 1, NULL, NULL, 1, 191, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (207, NULL, NULL, 2, 1, NULL, NULL, 1, 192, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (208, NULL, NULL, 2, 1, NULL, NULL, 1, 193, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (209, NULL, NULL, 2, 1, NULL, NULL, 1, 194, NULL, NULL, NULL, NULL);
INSERT INTO public.message_recipient (id, created_by, created_date_time, role_id, type, updated_by, updated_date_time, companyid, template_id, createdate, createdby, editedby, modifieddate) VALUES (210, NULL, NULL, 2, 1, NULL, NULL, 1, 195, NULL, NULL, NULL, NULL);

insert into system_settings(id,active_flag,attribute_name,attribute_type,attribute_value,created_by,created_date_time, image, updated_by, updated_date_time, companyid) values (1,null, 'backup_interval', 'Backup','Weekly', null,null,null, null, null, 1);

-- Assay Management

\connect "connect-amm"

INSERT INTO assay_type(assaytype_id, active_flag, assay_type, assay_version, workflow_def_file, workflow_file_version,companyid) VALUES(1,'Y','NIPTHTP','V1','NIPTHTP_Wfm_v1.bpmn.xml','V1',1);

INSERT INTO sample_type(sampletype_id, active_flag, max_age_days, sample_type, assaytype_id, assay_type,companyid) VALUES (1, 'Y', 7, 'Plasma', 1, 'NIPTHTP',1);

INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (1, 'Y', 'MP24', 'Harmony', 'Cfna ss 2000', 1, 'NIPTHTP', 'NA Extraction',1,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (2, 'Y', 'MP24', 'SCAP', 'Cfna ss 2000', 1, 'NIPTHTP', 'NA Extraction',2,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (3, 'Y', 'MP24', 'Fetal Sex', 'Cfna ss 2000', 1, 'NIPTHTP', 'NA Extraction',3,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (4, 'Y', 'MP24', 'MX', 'Cfna ss 2000', 1, 'NIPTHTP', 'NA Extraction',4,1);
--INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (5, 'Y', 'LP24', 'Harmony', 'LP24 Protocol', 1, 'NIPTHTP','LP Pre PCR',1,1);
--INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (6, 'Y', 'LP24', 'SCAP', 'LP24 Protocol', 1, 'NIPTHTP','LP Post PCR/Pooling',2,1);
--INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (7, 'Y', 'LP24', 'Fetal Sex', 'LP24 Protocol', 1, 'NIPTHTP','LP Sequencing prep',3,1);
--INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (8, 'Y', 'LP24', 'MX', 'LP24 Protocol', 1, 'NIPTHTP','',4,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (5, 'Y', 'LP24', 'Harmony', 'LP PrePCR Protocol', 1, 'NIPTHTP','LP Pre PCR',1,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (6, 'Y', 'LP24', 'SCAP', 'LP PrePCR Protocol', 1, 'NIPTHTP','LP Pre PCR',2,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (7, 'Y', 'LP24', 'Fetal Sex', 'LP PrePCR Protocol', 1, 'NIPTHTP','LP Pre PCR',3,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (8, 'Y', 'LP24', 'MX', 'LP PrePCR Protocol', 1, 'NIPTHTP','LP Pre PCR',4,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (9, 'Y', 'LP24', 'Harmony', 'LP PostPCR Protocol', 1, 'NIPTHTP','LP Post PCR/Pooling',1,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (10, 'Y', 'LP24', 'SCAP', 'LP PostPCR Protocol', 1, 'NIPTHTP','LP Post PCR/Pooling',2,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (11, 'Y', 'LP24', 'Fetal Sex', 'LP PostPCR Protocol', 1, 'NIPTHTP','LP Post PCR/Pooling',3,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (12, 'Y', 'LP24', 'MX', 'LP PostPCR Protocol', 1, 'NIPTHTP','LP Post PCR/Pooling',4,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (13, 'Y', 'LP24', 'Harmony', 'LP SeqPREP Protocol', 1, 'NIPTHTP','LP Sequencing Prep',1,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (14, 'Y', 'LP24', 'SCAP', 'LP SeqPREP Protocol', 1, 'NIPTHTP','LP Sequencing Prep',2,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (15, 'Y', 'LP24', 'Fetal Sex', 'LP SeqPREP Protocol', 1, 'NIPTHTP','LP Sequencing Prep',3,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (16, 'Y', 'LP24', 'MX', 'LP SeqPREP Protocol', 1, 'NIPTHTP','LP Sequencing Prep',4,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (17, 'Y', 'HTP', 'Harmony', 'HTP Protocol', 1, 'NIPTHTP','Sequencing',1,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (18, 'Y', 'HTP', 'SCAP', 'HTP Protocol', 1, 'NIPTHTP','Sequencing',2,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (19, 'Y', 'HTP', 'Fetal Sex', 'HTP Protocol', 1, 'NIPTHTP','Sequencing',3,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (20, 'Y', 'HTP', 'MX', 'HTP Protocol', 1, 'NIPTHTP','Sequencing',4,1);

INSERT INTO test_options(test_option_id, active_flag, sequence_id, test_name,test_description, assaytype_id, assay_type,companyid) VALUES (1, 'Y', 1, 'Harmony', 'Harmony', 1, 'NIPTHTP',1);
INSERT INTO test_options(test_option_id, active_flag, sequence_id, test_name,test_description, assaytype_id, assay_type,companyid) VALUES (2, 'Y', 2, 'SCAP', 'SCAP', 1, 'NIPTHTP',1);
INSERT INTO test_options(test_option_id, active_flag, sequence_id, test_name,test_description, assaytype_id, assay_type,companyid) VALUES (3, 'Y', 3, 'FetalSex', 'Fetal Sex', 1, 'NIPTHTP',1);
INSERT INTO test_options(test_option_id, active_flag, sequence_id, test_name,test_description, assaytype_id, assay_type,companyid) VALUES (4, 'Y', 4, 'MX', 'MX', 1, 'NIPTHTP',1);

INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name,assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,companyid) VALUES (1, 'Y','MP24', 'Y',1,'NA Extraction', 1, 'NIPTHTP','Plasma tube','8-tube strip',200,100,1);
INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,companyid) VALUES (2, 'Y', 'LP24', 'Y',2,'LP Pre PCR', 1, 'NIPTHTP','8-tube strip','PCR plate',100,50,1);
INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,companyid) VALUES (3, 'Y', 'LP24', 'Y',3,'LP Post PCR/Pooling', 1, 'NIPTHTP','PCR plate','PCR plate','','',1);
INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,companyid) VALUES (4, 'Y', 'LP24', 'Y',4,'LP Sequencing Prep', 1, 'NIPTHTP','PCR plate','Pool tube','','',1);
INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,companyid) VALUES (5, 'Y', 'HTP', 'Y',5,'Sequencing', 1, 'NIPTHTP','Pool tube','File path','','',1);
INSERT INTO process_step_action(proc_step_action_id, active_flag,device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,companyid) VALUES (6, 'Y', 'FORTE', 'Y',6,'Analysis', 1, 'NIPTHTP','Sequencing run directory','Analysis output directory','','',1);

INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, max_val, min_val, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (1, 'Y', 'NA', 'Maternal Age', 100, 0, 1, 'NIPTHTP',1,'data validation','NA');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, max_val, min_val, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (2, 'Y', 'NA', 'Gestational Age Weeks', 40, 10, 1, 'NIPTHTP',1,'data validation','NA');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, max_val, min_val, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (3, 'Y', 'NA', 'Gestational Age Days', 6, 0, 1, 'NIPTHTP',1,'data validation','NA');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, max_val, min_val, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (7, 'Y', 'NA', 'Egg Donor Age', 76, 12, 1, 'NIPTHTP',1,'data validation','NA');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (9, 'Y', 'NA', 'Comments', 1, 'NIPTHTP',1,'mandatory flag','false');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (10, 'Y', 'NA', 'Maternal age', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (11, 'Y', 'NA', 'Gestational age', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (12, 'Y', 'NA', 'IVF status', 1, 'NIPTHTP',1,'mandatory flag','true');
--INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (13, 'Y', 'NA', 'Egg Donor', 1, 'NIPTHTP',1,'mandatory flag','true');
--INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (14, 'Y', 'NA', 'Egg donor age', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (15, 'Y', 'NA', 'Number of fetus', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (16, 'Y', 'NA', 'First name', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (17, 'Y', 'NA', 'Last name', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (18, 'Y', 'NA', 'Medical record number', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (19, 'Y', 'NA', 'DOB', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (20, 'Y', 'NA', 'Referring clinician', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (21, 'Y', 'NA', 'Laboratory ID', 1, 'NIPTHTP',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (22, 'Y', 'NA', 'Other clinician', 1, 'NIPTHTP',1,'mandatory flag','false');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (23, 'Y', 'NA', 'Clinic name', 1, 'NIPTHTP',1,'mandatory flag','false');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (24, 'Y', 'NA', 'Reason for Referral', 1, 'NIPTHTP',1,'mandatory flag','false');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (25, 'Y', 'NA', 'Account #', 1, 'NIPTHTP',1,'mandatory flag','false');

INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (1, 'Y', 'ivf status', 'Yes', 1, 'NIPTHTP',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (2, 'Y', 'ivf status', 'No', 1, 'NIPTHTP',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (3, 'Y', 'ivf status', 'Missing', 1, 'NIPTHTP',1);
--INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (4, 'Y', 'egg donor', 'No', 1, 'NIPTHTP',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (4, 'Y', 'egg donor', 'Self', 1, 'NIPTHTP',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (5, 'Y', 'egg donor', 'Non-self', 1, 'NIPTHTP',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (6, 'Y', 'egg donor', 'Missing', 1, 'NIPTHTP',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (7, 'Y', 'Number of Fetus', '1', 1, 'NIPTHTP',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (8, 'Y', 'Number of Fetus', '2', 1, 'NIPTHTP',1);
--INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (10, 'Y', 'Number of Fetus', '>2', 1, 'NIPTHTP',1);
--INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (11, 'Y', 'Number of Fetus', 'Missing', 1, 'NIPTHTP',1);

INSERT INTO assay_type(assaytype_id, active_flag, assay_type, assay_version, workflow_def_file, workflow_file_version,companyid) VALUES(2,'Y','NIPTDPCR','V1','NIPTDPCR_Wfm_v1.bpmn.xml','V1',1);

INSERT INTO sample_type(sampletype_id, active_flag, max_age_days, sample_type, assaytype_id, assay_type,companyid) VALUES (2, 'Y', 7, 'Plasma', 2, 'NIPTDPCR',1);

INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (21, 'Y', 'MP96', 'Harmony Prenatal Test (T21, T18, T13)', 'Cellular RNA LV', 2, 'NIPTDPCR', 'NA Extraction',1,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (22, 'Y', 'MP96', 'Fetal Sex', 'Cellular RNA LV', 2, 'NIPTDPCR', 'NA Extraction',2,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (23, 'Y', 'LP24', 'Harmony Prenatal Test (T21, T18, T13)', 'LPdPCR', 2, 'NIPTDPCR','Library Preparation',1,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid) VALUES (24, 'Y', 'LP24', 'Fetal Sex', 'LPdPCR', 2, 'NIPTDPCR','Library Preparation',2,1);
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid,analysis_package_name) VALUES (25, 'Y', 'dPCR Analyzer', 'Harmony Prenatal', 'dPCRAnalyzerAssay1', 2, 'NIPTDPCR','dPCR',1,1,'AP1');
INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid,analysis_package_name) VALUES (26, 'Y', 'dPCR Analyzer', 'Fetal Sex', 'dPCRAnalyzerAssay1', 2, 'NIPTDPCR','dPCR',2,1,'AP1');
--INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid,test_protocol_version) VALUES (27, 'Y', 'dPCR Analyzer', 'Harmony Prenatal', 'dPCRAnalyzerAssay2', 2, 'NIPTDPCR','dPCR',1,1,'1.1');
--INSERT INTO device_test_options(device_test_option_id, active_flag, device_type, test_name, test_protocol, assaytype_id, assay_type, process_step_name, test_option_id,companyid,test_protocol_version) VALUES (28, 'Y', 'dPCR Analyzer', 'Fetal Sex', 'dPCRAnalyzerAssay2', 2, 'NIPTDPCR','dPCR',2,1,'1.1');




INSERT INTO test_options(test_option_id, active_flag, sequence_id, test_name,test_description, assaytype_id, assay_type,companyid) VALUES (5, 'Y', 5, 'Harmony Prenatal Test (T21, T18, T13)', 'Harmony Prenatal Test (T21, T18, T13)', 2, 'NIPTDPCR',1);
INSERT INTO test_options(test_option_id, active_flag, sequence_id, test_name,test_description, assaytype_id, assay_type,companyid) VALUES (6, 'Y', 6, 'Fetal Sex', 'Fetal Sex', 2, 'NIPTDPCR',1);
--INSERT INTO test_options(test_option_id, active_flag, sequence_id, test_name,test_description, assaytype_id, assay_type,companyid) VALUES (7, 'Y', 7, 'FetalSex', 'Fetal Sex', 2, 'NIPTDPCR',1);
--INSERT INTO test_options(test_option_id, active_flag, sequence_id, test_name,test_description, assaytype_id, assay_type,companyid) VALUES (8, 'Y', 8, 'MX', 'MX', 2, 'NIPTDPCR',1);

INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name,assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,reagent,companyid) VALUES (7, 'Y','MP96', 'Y',1,'NA Extraction', 2, 'NIPTDPCR','96 Well plate','96 Well plate','200','50','Cellular RNA LV',1);
INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,reagent,companyid) VALUES (8, 'Y', 'LP24', 'Y',2,'Library Preparation', 2, 'NIPTDPCR','96 Well plate','dPCR Chip','50','40','LP24_dPCR Protocol',1);
--INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,reagent,companyid) VALUES (9, 'Y', '', 'Y',3,'dPCR', 2, 'NIPTDPCR','','','','','',1);
INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,reagent,companyid) VALUES (9, 'Y', 'dPCR Analyzer', 'Y',3,'dPCR', 2, 'NIPTDPCR',null,null,null,null,null,1);
INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,sample_volume,eluate_volume,reagent,companyid) VALUES (10, 'Y', null, 'Y',4,'Analysis', 2, 'NIPTDPCR','','','','','',1);
--INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,companyid) VALUES (9, 'Y', 'LP24', 'Y',9,'LP Post PCR/Pooling',2, 'NIPTDPCR','PCR plate','PCR plate',1);
--INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,companyid) VALUES (10, 'Y', 'LP24', 'Y',10,'LP Sequencing Prep',2, 'NIPTDPCR','PCR plate','Pool tube',1);
--INSERT INTO process_step_action(proc_step_action_id, active_flag, device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,companyid) VALUES (11, 'Y', 'HTP', 'Y',11,'Sequencing',2, 'NIPTDPCR','Pool tube','Consumable device',1);
--INSERT INTO process_step_action(proc_step_action_id, active_flag,device_type, manual_verification_flag,process_step_seq,process_step_name, assaytype_id, assay_type,input_container_type, output_container_type,companyid) VALUES (12, 'Y', 'FORTE', 'Y',12,'Analysis',2, 'NIPTDPCR','Sequencing run directory','Analysis output directory',1);

INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, max_val, min_val, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (4, 'Y', 'NA', 'Maternal Age', 100, 0, 2, 'NIPTDPCR',1,'data validation','NA');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, max_val, min_val, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (5, 'Y', 'NA', 'Gestational Age Weeks', 40, 10,2, 'NIPTDPCR',1,'data validation','NA');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, max_val, min_val, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (6, 'Y', 'NA', 'Gestational Age Days', 6, 0,2, 'NIPTDPCR',1,'data validation','NA');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, max_val, min_val, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (8, 'Y', 'NA', 'Egg Donor Age', 76, 12,2, 'NIPTDPCR',1,'data validation','NA');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (26, 'Y', 'NA', 'Comments', 2, 'NIPTDPCR',1,'mandatory flag','false');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (27, 'Y', 'NA', 'Maternal age', 2, 'NIPTDPCR',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (28, 'Y', 'NA', 'Gestational age', 2, 'NIPTDPCR',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (29, 'Y', 'NA', 'IVF status', 2, 'NIPTDPCR',1,'mandatory flag','true');
--INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (30, 'Y', 'NA', 'Egg Donor', 2, 'NIPTDPCR',1,'mandatory flag','false');
--INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (31, 'Y', 'NA', 'Egg donor age', 2, 'NIPTDPCR',1,'mandatory flag','false');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (32, 'Y', 'NA', 'Number of fetus', 2, 'NIPTDPCR',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (33, 'Y', 'NA', 'First name', 2, 'NIPTDPCR',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (34, 'Y', 'NA', 'Last name', 2, 'NIPTDPCR',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (35, 'Y', 'NA', 'Medical record number', 2, 'NIPTDPCR',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (36, 'Y', 'NA', 'DOB', 2, 'NIPTDPCR',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (37, 'Y', 'NA', 'Referring clinician', 2, 'NIPTDPCR',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (38, 'Y', 'NA', 'Laboratory ID', 2, 'NIPTDPCR',1,'mandatory flag','true');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (39, 'Y', 'NA', 'Other clinician', 2, 'NIPTDPCR',1,'mandatory flag','false');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (40, 'Y', 'NA', 'Clinic name', 2, 'NIPTDPCR',1,'mandatory flag','false');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (41, 'Y', 'NA', 'Reason for Referral', 2, 'NIPTDPCR',1,'mandatory flag','false');
INSERT INTO assay_data_validations(assay_validation_id, active_flag, expression, field_name, assaytype_id, assay_type,companyid,group_name,is_mandatory) VALUES (42, 'Y', 'NA', 'Account #', 2, 'NIPTDPCR',1,'mandatory flag','false');

INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (9, 'Y', 'ivf status', 'Yes', 2, 'NIPTDPCR',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (10, 'Y', 'ivf status', 'No', 2, 'NIPTDPCR',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (11, 'Y', 'ivf status', 'Missing', 2, 'NIPTDPCR',1);
--INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (15, 'Y', 'egg donor', 'No',2, 'NIPTDPCR',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (12, 'Y', 'egg donor', 'Self',2, 'NIPTDPCR',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (13, 'Y', 'egg donor', 'Non-self',2, 'NIPTDPCR',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (14, 'Y', 'egg donor', 'Missing',2, 'NIPTDPCR',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (15, 'Y', 'Number of Fetus', '1',2, 'NIPTDPCR',1);
INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (16, 'Y', 'Number of Fetus', '2',2, 'NIPTDPCR',1);
--INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (21, 'Y', 'Number of Fetus', '>2',2, 'NIPTDPCR',1);
--INSERT INTO assay_listdata(assay_listdata_id, active_flag, list_type, "value", assaytype_id, assay_type,companyid) VALUES (22, 'Y', 'Number of Fetus', 'Missing',2, 'NIPTDPCR',1);

INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(1,'A','A1','MID1','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(2,'A','B1','MID2','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(3,'A','C1','MID3','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(4,'A','D1','MID4','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(5,'A','E1','MID5','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(6,'A','F1','MID6','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(7,'A','G1','MID7','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(8,'A','H1','MID8','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(9,'A','A2','MID9','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(10,'A','B2','MID10','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(11,'A','C2','MID11','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(12,'A','D2','MID12','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(13,'A','E2','MID13','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(14,'A','F2','MID14','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(15,'A','G2','MID15','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(16,'A','H2','MID16','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(17,'A','A3','MID17','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(18,'A','B3','MID18','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(19,'A','C3','MID19','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(20,'A','D3','MID20','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(21,'A','E3','MID21','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(22,'A','F3','MID22','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(23,'A','G3','MID23','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(24,'A','H3','MID24','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(25,'B','A1','MID25','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(26,'B','B1','MID26','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(27,'B','C1','MID27','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(28,'B','D1','MID28','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(29,'B','E1','MID29','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(30,'B','F1','MID30','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(31,'B','G1','MID31','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(32,'B','H1','MID32','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(33,'B','A2','MID33','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(34,'B','B2','MID34','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(35,'B','C2','MID35','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(36,'B','D2','MID36','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(37,'B','E2','MID37','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(38,'B','F2','MID38','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(39,'B','G2','MID39','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(40,'B','H2','MID40','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(41,'B','A3','MID41','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(42,'B','B3','MID42','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(43,'B','C3','MID43','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(44,'B','D3','MID44','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(45,'B','E3','MID45','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(46,'B','F3','MID46','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(47,'B','G3','MID47','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(48,'B','H3','MID48','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(49,'C','A1','MID49','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(50,'C','B1','MID50','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(51,'C','C1','MID51','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(52,'C','D1','MID52','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(53,'C','E1','MID53','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(54,'C','F1','MID54','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(55,'C','G1','MID55','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(56,'C','H1','MID56','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(57,'C','A2','MID57','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(58,'C','B2','MID58','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(59,'C','C2','MID59','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(60,'C','D2','MID60','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(61,'C','E2','MID61','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(62,'C','F2','MID62','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(63,'C','G2','MID63','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(64,'C','H2','MID64','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(65,'C','A3','MID65','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(66,'C','B3','MID66','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(67,'C','C3','MID67','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(68,'C','D3','MID68','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(69,'C','E3','MID69','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(70,'C','F3','MID70','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(71,'C','G3','MID71','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(72,'C','H3','MID72','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(73,'D','A1','MID73','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(74,'D','B1','MID74','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(75,'D','C1','MID75','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(76,'D','D1','MID76','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(77,'D','E1','MID77','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(78,'D','F1','MID78','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(79,'D','G1','MID79','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(80,'D','H1','MID80','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(81,'D','A2','MID81','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(82,'D','B2','MID82','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(83,'D','C2','MID83','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(84,'D','D2','MID84','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(85,'D','E2','MID85','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(86,'D','F2','MID86','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(87,'D','G2','MID87','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(88,'D','H2','MID88','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(89,'D','A3','MID89','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(90,'D','B3','MID90','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(91,'D','C3','MID91','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(92,'D','D3','MID92','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(93,'D','E3','MID93','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(94,'D','F3','MID94','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(95,'D','G3','MID95','Y',1,'NIPTHTP',1);
INSERT INTO MOLECULAR_TYPE(molecular_listdata_id,plate_type,plate_location,molecular_id,active_flag,assaytype_id, assay_type,companyid) VALUES(96,'D','H3','MID96','Y',1,'NIPTHTP',1);


INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (1,'Y','NIPTDPCR', 'MP96', 'F1',  'Warning','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (2,'Y', 'NIPTDPCR', 'MP96', 'F2', 'Fatal','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (3,'Y', 'NIPTDPCR', 'MP96', 'F3',  'Fatal','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (4,'Y', 'NIPTDPCR', 'MP96', 'F4',  'Warning','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (5,'Y', 'NIPTDPCR', 'MP96', 'F5','Fatal','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (6,'Y', 'NIPTDPCR', 'MP96', 'F6', 'Fatal','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (7,'Y', 'NIPTDPCR', 'MP96', 'F7',  'Fatal','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (8,'Y', 'NIPTDPCR', 'MP96', 'F8',  'Fatal','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (9,'Y', 'NIPTDPCR', 'MP96', 'F9',  'Warning','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (10,'Y', 'NIPTDPCR', 'MP96', 'F10',  'Warning','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (11,'Y', 'NIPTDPCR', 'MP96', 'F11', 'Warning','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (12,'Y', 'NIPTDPCR', 'MP96', 'F12',  'Warning','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (13,'Y', 'NIPTDPCR', 'MP96', 'F13', 'Fatal','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (14,'Y', 'NIPTDPCR', 'MP96', 'F14',  'Warning','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (15,'Y', 'NIPTDPCR', 'MP96', 'F15',  'Fatal','NA Extraction', 'All samples in a run',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (16,'Y', 'NIPTDPCR', 'MP96', 'F16',  'Fatal','NA Extraction', 'Samples',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (17,'Y', 'NIPTDPCR', 'MP96', 'F17',  'Fatal','NA Extraction', 'Samples',2, 1);

INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (18,'Y', 'NIPTDPCR', 'LP24', 'F1', 'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (19,'Y', 'NIPTDPCR', 'LP24', 'F2', 'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (20,'Y', 'NIPTDPCR', 'LP24', 'F3', 'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (21,'Y', 'NIPTDPCR', 'LP24', 'F4',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (22,'Y', 'NIPTDPCR', 'LP24', 'F5',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (23,'Y', 'NIPTDPCR', 'LP24', 'F6',  'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (24,'Y', 'NIPTDPCR', 'LP24', 'F7', 'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (25,'Y', 'NIPTDPCR', 'LP24', 'F8', 'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (26,'Y', 'NIPTDPCR', 'LP24', 'F9',  'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (27,'Y', 'NIPTDPCR', 'LP24', 'F10', 'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (28,'Y', 'NIPTDPCR', 'LP24', 'F12', 'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (29,'Y', 'NIPTDPCR', 'LP24', 'F13',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (30,'Y', 'NIPTDPCR', 'LP24', 'F14', 'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (31,'Y', 'NIPTDPCR', 'LP24', 'F15',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (32,'Y', 'NIPTDPCR', 'LP24', 'F17', 'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (33,'Y', 'NIPTDPCR', 'LP24', 'F18',  'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (34,'Y', 'NIPTDPCR', 'LP24', 'F19',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (35,'Y', 'NIPTDPCR', 'LP24', 'F20', 'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (36,'Y', 'NIPTDPCR', 'LP24', 'F21',  'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (37,'Y', 'NIPTDPCR', 'LP24', 'F22', 'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (38,'Y', 'NIPTDPCR', 'LP24', 'F23', 'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (39,'Y', 'NIPTDPCR', 'LP24', 'F24',  'Information','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (40,'Y', 'NIPTDPCR', 'LP24', 'F25',  'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (41,'Y', 'NIPTDPCR', 'LP24', 'F26',  'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (42,'Y', 'NIPTDPCR', 'LP24', 'F27',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (43,'Y', 'NIPTDPCR', 'LP24', 'F28',  'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (44,'Y', 'NIPTDPCR', 'LP24', 'F29','Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (45,'Y', 'NIPTDPCR', 'LP24', 'F30',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (46,'Y', 'NIPTDPCR', 'LP24', 'F32',  'Information','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (47,'Y', 'NIPTDPCR', 'LP24', 'F33', 'Information','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (48,'Y', 'NIPTDPCR', 'LP24', 'F34', 'Information','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (49,'Y', 'NIPTDPCR', 'LP24', 'F35',  'Information','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (50,'Y', 'NIPTDPCR', 'LP24', 'F36',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (51,'Y', 'NIPTDPCR', 'LP24', 'F37',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (52,'Y', 'NIPTDPCR', 'LP24', 'F38', 'Information','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (53,'Y', 'NIPTDPCR', 'LP24', 'F39', 'Critical','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (54,'Y', 'NIPTDPCR', 'LP24', 'F40',  'Information','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (55,'Y', 'NIPTDPCR', 'LP24', 'F41',  'Fatal','Library Preparation', 'Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (56,'Y', 'NIPTDPCR', 'LP24', 'F42',  'Critical','Library Preparation', 'Sample',2, 1);

INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (57,'Y', 'NIPTDPCR', 'dPCR Analyzer', 'AT001',  'Fatal','dPCR', 'Sample', 2,1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (58,'Y', 'NIPTDPCR', 'dPCR Analyzer', 'AT002',  'Fatal', 'dPCR','Sample', 2,1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (59,'Y', 'NIPTDPCR', 'dPCR Analyzer', 'AT003',  'Critical','dPCR', 'Sample', 2,1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (60,'Y', 'NIPTDPCR', 'dPCR Analyzer', 'AT004', 'Warning', 'dPCR','Sample', 2,1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (61,'Y', 'NIPTDPCR', 'dPCR Analyzer', 'AT005', 'Fatal', 'dPCR','Sample', 2,1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (62,'Y', 'NIPTDPCR', 'dPCR Analyzer', 'AT006', 'Fatal', 'dPCR','Sample',2, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (63,'Y', 'NIPTDPCR', 'dPCR Analyzer', 'AT007', 'Fatal','dPCR', 'Sample',2, 1);

INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (64,'Y', 'NIPTHTP', 'MP24', 'F1', 'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (65,'Y', 'NIPTHTP', 'MP24', 'F2', 'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (66,'Y', 'NIPTHTP', 'MP24', 'F3', 'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (67,'Y', 'NIPTHTP', 'MP24', 'F4',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (68,'Y', 'NIPTHTP', 'MP24', 'F5',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (69,'Y', 'NIPTHTP', 'MP24', 'F6',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (70,'Y', 'NIPTHTP', 'MP24', 'F7',  'Critical','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (71,'Y', 'NIPTHTP', 'MP24', 'F8',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (72,'Y', 'NIPTHTP', 'MP24', 'F9',  'Critical','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (73,'Y', 'NIPTHTP', 'MP24', 'F10',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (74,'Y', 'NIPTHTP', 'MP24', 'F12',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (75,'Y', 'NIPTHTP', 'MP24', 'F13',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (76,'Y', 'NIPTHTP', 'MP24', 'F14',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (77,'Y', 'NIPTHTP', 'MP24', 'F15', 'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (78,'Y', 'NIPTHTP', 'MP24', 'F17', 'Critical','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (79,'Y', 'NIPTHTP', 'MP24', 'F18',  'Critical','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (80,'Y', 'NIPTHTP', 'MP24', 'F19',  'Critical','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (81,'Y', 'NIPTHTP', 'MP24', 'F20', 'Critical','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (82,'Y', 'NIPTHTP', 'MP24', 'F21',  'Critical','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (83,'Y', 'NIPTHTP', 'MP24', 'F22',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (84,'Y', 'NIPTHTP', 'MP24', 'F23', 'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (85,'Y', 'NIPTHTP', 'MP24', 'F24',  'Information','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (86,'Y', 'NIPTHTP', 'MP24', 'F25',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (87,'Y', 'NIPTHTP', 'MP24', 'F26',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (88,'Y', 'NIPTHTP', 'MP24', 'F27',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (89,'Y', 'NIPTHTP', 'MP24', 'F28', 'Critical','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (90,'Y', 'NIPTHTP', 'MP24', 'F29',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (91,'Y', 'NIPTHTP', 'MP24', 'F30', 'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (92,'Y', 'NIPTHTP', 'MP24', 'F32',  'Information','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (93,'Y', 'NIPTHTP', 'MP24', 'F33',  'Information','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (94,'Y', 'NIPTHTP', 'MP24', 'F34','Information','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (95,'Y', 'NIPTHTP', 'MP24', 'F35', 'Information','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (96,'Y', 'NIPTHTP', 'MP24', 'F36',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (97,'Y', 'NIPTHTP', 'MP24', 'F37',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (98,'Y', 'NIPTHTP', 'MP24', 'F38', 'Information','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (99,'Y', 'NIPTHTP', 'MP24', 'F39', 'Critical','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (100 ,'Y','NIPTHTP', 'MP24', 'F40', 'Information','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (101,'Y', 'NIPTHTP', 'MP24', 'F41',  'Fatal','NA Extraction', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (102,'Y', 'NIPTHTP', 'MP24', 'F42',  'Critical','NA Extraction', 'Sample',1, 1);

INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (103,'Y', 'NIPTHTP', 'LP24', 'F1', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (104,'Y', 'NIPTHTP', 'LP24', 'F2',  'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (105,'Y', 'NIPTHTP', 'LP24', 'F3', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (106,'Y', 'NIPTHTP', 'LP24', 'F4',  'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (107,'Y', 'NIPTHTP', 'LP24', 'F5', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (108,'Y', 'NIPTHTP', 'LP24', 'F6',  'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (109,'Y', 'NIPTHTP', 'LP24', 'F7', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (110,'Y', 'NIPTHTP', 'LP24', 'F8',  'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (111,'Y', 'NIPTHTP', 'LP24', 'F9',  'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (112,'Y', 'NIPTHTP', 'LP24', 'F10', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (113,'Y', 'NIPTHTP', 'LP24', 'F12',  'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (114,'Y', 'NIPTHTP', 'LP24', 'F13', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (115,'Y', 'NIPTHTP', 'LP24', 'F14',  'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (116,'Y', 'NIPTHTP', 'LP24', 'F15', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (117,'Y', 'NIPTHTP', 'LP24', 'F17', 'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (118,'Y', 'NIPTHTP', 'LP24', 'F18', 'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (119,'Y', 'NIPTHTP', 'LP24', 'F19', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (120,'Y', 'NIPTHTP', 'LP24', 'F20', 'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (121,'Y', 'NIPTHTP', 'LP24', 'F21',  'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (122,'Y', 'NIPTHTP', 'LP24', 'F22', 'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (123,'Y', 'NIPTHTP', 'LP24', 'F23', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (124,'Y', 'NIPTHTP', 'LP24', 'F24', 'Information','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (125,'Y', 'NIPTHTP', 'LP24', 'F25', 'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (126,'Y', 'NIPTHTP', 'LP24', 'F26', 'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (127,'Y', 'NIPTHTP', 'LP24', 'F27',  'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (128,'Y', 'NIPTHTP', 'LP24', 'F28', 'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (129,'Y', 'NIPTHTP', 'LP24', 'F29','Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (130,'Y', 'NIPTHTP', 'LP24', 'F30',  'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (131,'Y', 'NIPTHTP', 'LP24', 'F32', 'Information','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (132,'Y', 'NIPTHTP', 'LP24', 'F33',  'Information','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (133,'Y', 'NIPTHTP', 'LP24', 'F34',  'Information','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (134,'Y', 'NIPTHTP', 'LP24', 'F35', 'Information','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (135,'Y', 'NIPTHTP', 'LP24', 'F36', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (136,'Y', 'NIPTHTP', 'LP24', 'F37', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (137,'Y', 'NIPTHTP', 'LP24', 'F38', 'Information','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (138,'Y', 'NIPTHTP', 'LP24', 'F39', 'Critical','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (139,'Y', 'NIPTHTP', 'LP24', 'F40', 'Information','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (140,'Y', 'NIPTHTP', 'LP24', 'F41', 'Fatal','Library Preparation', 'Sample',1, 1);
INSERT INTO flags_data(flag_id,active_flag, assay_type,device_type,flag_code, flag_severity, process_step_name, sample_or_runlevel, assaytype_id, companyid)
VALUES (141,'Y', 'NIPTHTP', 'LP24', 'F42', 'Critical','Library Preparation', 'Sample',1, 1);