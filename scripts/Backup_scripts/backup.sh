#!/bin/sh

reset_func()
{ 
    PGPASSWORD=$db_password psql -h localhost -p 5432 -U postgres -c "select pg_stop_backup();"
    rm -rf $result_location/result.log
    logger -s "Result=-1" 2>> $result_location/result.log
    rm -rf $temp_file/Connect-Backup-$current_time.tar.gz
    exit
}


script_location='/opt/roche/connect/scripts/Backup_scripts'

if [ -z "$1" ];
then
	echo "Backup location not provided"
	exit
fi

if [ ! -f "$script_location/backup.json" ];
then 
	echo "backup.json file not found"
	exit
fi

pas_jars=$(jq -r '.pasjars' $script_location/backup.json)

connect_jars=$(jq -r '.connectjars' $script_location/backup.json)

temp_file=$(jq -r '.tempfile' $script_location/backup.json)

pg_wal_files=$(jq -r '.pgwalfiles' $script_location/backup.json)

db_base=$(jq -r '.dbbase' $script_location/backup.json)

db_conf=$(jq -r '.dbconf' $script_location/backup.json)

web_apps=$(jq -r '.webapps' $script_location/backup.json)

result_location=$(jq -r '.resultlocation' $script_location/backup.json)

yaml=$(jq -r '.yaml' $script_location/backup.json)

restore_json=$(jq -r '.restorejson' $script_location/backup.json)

db_password=$(jq -r '.postgrespassword' $script_location/backup.json)


current_time=$(date "+%Y.%m.%d-%H.%M.%S")

echo "Backup job started"

if [ ! -d "$1" ]
then
    mkdir -p $1   
fi

echo "Removing recovery.done from " $db_base
rm -rf $db_base/recovery.done

PGPASSWORD=$db_password psql -h localhost -p 5432 -U postgres -c "select pg_start_backup('backup');"
if [ $? -eq 0 ]; then
    rm -rf $result_location/result.log
    logger -s "Result=1" 2>> $result_location/result.log
else
    exit
fi

echo "Creating temporary folder " $temp_file
if [ ! -d "$temp_file" ]
then
    mkdir -p $temp_file
fi


tar -cf $temp_file/Connect-Backup-$current_time.tar.gz $db_base
if [ $? -ne 0 ]; then
    reset_func
fi

tar -rf $temp_file/Connect-Backup-$current_time.tar.gz $pg_wal_files
if [ $? -ne 0 ]; then
    reset_func
fi

tar -rf $temp_file/Connect-Backup-$current_time.tar.gz $db_conf/postgresql.conf
if [ $? -ne 0 ]; then
    reset_func
fi

tar -rf $temp_file/Connect-Backup-$current_time.tar.gz $db_conf/pg_hba.conf
if [ $? -ne 0 ]; then
    reset_func
fi


tar -rf $temp_file/Connect-Backup-$current_time.tar.gz $db_conf/pg_ident.conf
if [ $? -ne 0 ]; then
    reset_func
fi


tar -rf $temp_file/Connect-Backup-$current_time.tar.gz $pas_jars/*.jar
if [ $? -ne 0 ]; then
    reset_func
fi

tar -rf $temp_file/Connect-Backup-$current_time.tar.gz $connect_jars/*.jar
if [ $? -ne 0 ]; then
    reset_func
fi

tar -rf $temp_file/Connect-Backup-$current_time.tar.gz $web_apps/*
if [ $? -ne 0 ]; then
    reset_func
fi

tar -rf $temp_file/Connect-Backup-$current_time.tar.gz $yaml/*.yaml
if [ $? -ne 0 ]; then
    reset_func
fi

tar -rf $temp_file/Connect-Backup-$current_time.tar.gz $restore_json/restore.json
if [ $? -ne 0 ]; then
    reset_func
fi


scp $temp_file/Connect-Backup-$current_time.tar.gz $1
if [ $? -ne 0 ]; then
    reset_func
fi

sha256sum $1/Connect-Backup-$current_time.tar.gz | awk '{print $1}' > $1/Connect-Backup-$current_time.tar.gz.sha256
if [ $? -ne 0 ]; then
    reset_func
fi

rm -rf $temp_file
if [ $? -ne 0 ]; then
    reset_func
fi

PGPASSWORD=$db_password psql -h localhost -p 5432 -U postgres -c "select pg_stop_backup();"
if [ $? -ne 0 ]; then
    rm -rf $result_location/result.log
    logger -s "Result=-1" 2>> $result_location/result.log
    exit
fi

rm -rf $result_location/result.log
logger -s "Result=0" 2>> $result_location/result.log
