----------------------------------------------------
-- run_reagents_and_consumables table indexes --
----------------------------------------------------
CREATE INDEX run_reagents_and_consumables_index_1
ON run_reagents_and_consumables (run_results_id);
commit;


----------------------------------------------------
-- run_results_detail table indexes --
----------------------------------------------------
CREATE INDEX run_results_detail_index_1
ON run_results_detail (run_results_id);
commit;


----------------------------------------------------
-- run_results table indexes --
----------------------------------------------------
CREATE INDEX run_results_index_1
ON run_results (device_id,process_step_name,run_status);
commit;

CREATE INDEX run_results_index_2
ON run_results (device_id,process_step_name);
commit;

CREATE INDEX run_results_index_3
ON run_results (device_run_id);
commit;

CREATE INDEX run_results_index_4
ON run_results (device_id,run_status);
commit;

CREATE INDEX run_results_index_5
ON run_results (device_run_id,process_step_name);
commit;


----------------------------------------------------
-- sample_protocol table indexes --
----------------------------------------------------
CREATE INDEX sample_protocol_index_1
ON sample_protocol (sample_results_id);
commit;


----------------------------------------------------
-- sample_reagents_and_consumables table indexes --
----------------------------------------------------
CREATE INDEX sample_reagents_and_consumables_index_1
ON sample_reagents_and_consumables (sample_results_id);
commit;


----------------------------------------------------
-- sample_results_detail table indexes --
----------------------------------------------------
CREATE INDEX sample_results_detail_index_1
ON sample_results_detail (sample_results_id);
commit;


----------------------------------------------------
-- sample_results table indexes --
----------------------------------------------------
CREATE INDEX sample_results_index_1
ON sample_results (output_container_id);
commit;

CREATE INDEX sample_results_index_2
ON sample_results (accessioning_id,input_container_id);
commit;

CREATE INDEX sample_results_index_3
ON sample_results (accessioning_id,input_container_id,input_container_position);
commit;

CREATE INDEX sample_results_index_4
ON sample_results (accessioning_id);
commit;

CREATE INDEX sample_results_index_5
ON sample_results (run_results_id);
commit;
