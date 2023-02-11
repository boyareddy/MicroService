-----------------------------------
--CONTAINER_SAMPLES Table Indexes--
-----------------------------------
CREATE INDEX container_samples_index_1
ON container_samples (status,companyid);
commit;

CREATE INDEX container_samples_index_2
ON container_samples (accessioning_id);
commit;

CREATE INDEX container_samples_index_3
ON container_samples (accessioning_id,companyid);
commit;

CREATE INDEX container_samples_index_4
ON container_samples (container_id,position,companyid);
commit;

CREATE INDEX container_samples_index_5
ON container_samples (companyid,container_id,status);
commit;

CREATE INDEX container_samples_index_6
ON container_samples (accessioning_id,companyid,device_run_id);
commit;

CREATE INDEX container_samples_index_7
ON container_samples (device_run_id,companyid);
commit;

CREATE INDEX container_samples_index_8
ON container_samples (companyid);
commit;

------------------------
--ORDERS Table Indexes--
------------------------
CREATE INDEX orders_index_1
ON orders (patient_sample_id);
commit;

CREATE INDEX orders_index_2
ON orders (patient_id);
commit;

CREATE INDEX orders_index_3
ON orders (patient_sample_id,companyid,active_flag,order_status);
commit;

CREATE INDEX orders_index_4
ON orders (accessioning_id,companyid);
commit;

CREATE INDEX orders_index_5
ON orders (accessioning_id);
commit;

CREATE INDEX orders_index_6
ON orders (active_flag,order_id);
commit;

CREATE INDEX orders_index_7
ON orders (order_id,companyid);
commit;

-------------------------
--PATIENT Table Indexes--
-------------------------
CREATE INDEX patient_index_1
ON patient (patient_id,companyid);
commit;

--------------------------------
--PATIENT_ASSAY Table Indexes--
--------------------------------
CREATE INDEX patient_assay_index_1
ON patient_assay (patient_id);
commit;

---------------------------------
--PATIENT_SAMPLE Table Indexes--
---------------------------------
CREATE INDEX patient_sample_index_1
ON patient_sample (sample_id,companyid);
commit;

CREATE INDEX patient_sample_index_2
ON patient_sample (patient_id);
commit;

--------------------------------------
--ORDER_TEST_MAPPING Table Indexes--
--------------------------------------
CREATE INDEX order_test_mapping_index_1
ON order_test_mapping (order_id);
commit;