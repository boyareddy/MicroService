package com.roche.connect.htp.adapter.util;

public enum HTPAdapterConstants {
    
    
    INVALID_RUN_INFO("invalid run Info"),
    ACCEPTED("Accepted"),
    CONFLICT("Conflict"), 
    CREATED("Created"),
    DEVICE("device not registered in connect"),
    WARNING("warning"),
    ERROR("error"),
    COOKIE("Cookie"),
    BROWN_STONE_AUTH_COOKIE("brownstoneauthcookie="),
    
    SEQ_MISMATCH("Sequence mismatch"),
    COMPLEX_ID("complex_id"),
    CYCLE("cycle"),
    RUN_PROTOCOL("run_protocol"),
    RUN_DATA_FOLDER_TMP("run_data_folder_tmp"),
    INSTRUMENT_ID("instrument_id"),
    RUN_DATA_FOLDER_PRIMARY("run_data_folder_primary"),
    REFERENCE_ID("reference_id"),
    TYPE("type"),
    RUN_STATUS("run_status"),
    PATH("path"),
    CHECKSUM("checksum"),
    RUN_ID("run_id"),
    OFFLINE_STATUS("offline"),
    ONLINE_STATUS("online"),
    
    EST_TIME_REM("estimated_time_remaining"),
    SEQUENCING_RUN_START_TIME("sequencing_run_start_datetime"),
    SEQUENCING_RUN_END_TIME("sequencing_run_end_datetime"),
    RUN_START_USER_ID("run_start_user_id"),
    CONSUMABLE_DEVICE_FIRST_USE_DATE("consumable_device_first_use_date"),
    CONSUMABLE_DEVICE_PART_NUMBER("consumable_device_part_number"),
    CONSUMABLE_DEVICE_EXPIRATION("consumable_device_expiration"),
    SEQUENCING_RUN_KIT_PART_NUMBER("sequencing_run_kit_part_number"),
    SEQUENCING_RUN_KIT_EXPIRATION("sequencing_run_kit_expiration"),
    SYSTEM_FLUID_PART_NUMBER("system_fluid_part_number"),
    SYSTEM_FLUID_EXPIRATION("system_fluid_expiration"),
    SOFTWARE_VERSION_NSE("software_version_nse"),
    SOFTWARE_VERSION_NSP("software_version_nsp"),
    SOFTWARE_VERSION_NSC("software_version_nsc"),
    SOFTWARE_VERSION_NSA("software_version_nsa"),
    SEQUENCING_VERIFICATION_LOAD_TIME("sequencing_verification_load_datetime"),
    LANE_NUMBER("lane_number"),
    RUN_NUMBER("run_number"),
    TOTAL_CYCLES("total_cycles"),
    COMPLEXID("complexId"),
    DATE_FORMAT("yyyy-MM-dd HH:mm:ss"),
    SERIAL_NO("serialNo"),
    SIMPLE_DATE_FORMAT_RMM("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    
  

    private final String text;

    HTPAdapterConstants(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
