#!/bin/sh
set -e

if [ ! -f "$1" ];
then
	echo "Backup file not provided"
	exit
fi

if [ ! -f "restore.json" ];
then 
	echo "restore.json file not found"
	exit
fi

db_base=$(jq -r '.dbbase' restore.json)

db_base_temp=$(jq -r '.dbbasetemp' restore.json)

db_before_restore=$(jq -r '.dbbeforerestore' restore.json)

pas_jars=$(jq -r '.pasjars' restore.json)

pas_jars_temp=$(jq -r '.pasjarstemp' restore.json)

connect_jars=$(jq -r '.connectjars' restore.json)

connect_jars_temp=$(jq -r '.connectjarstemp' restore.json)

db_conf=$(jq -r '.dbconf' restore.json)

pg_wal_files=$(jq -r '.pgwalfiles' restore.json)

pg_wal_files_temp=$(jq -r '.pgwalfilestemp' restore.json)

temp_folder=$(jq -r '.tempfolder' restore.json)

web_apps=$(jq -r '.webapps' restore.json)

web_apps_temp=$(jq -r '.webappstemp' restore.json)

yaml=$(jq -r '.yaml' restore.json)

yaml_temp=$(jq -r '.yamltemp' restore.json)

restore_json=$(jq -r '.restorejson' restore.json)

instance_list=$(jq -r '.instancelist' restore.json)


echo "Validating checksum"
if [ ! -d "$temp_folder" ]
then
    mkdir -p $temp_folder   
fi
sha256sum $1 | awk '{print $1}' > $temp_folder/checksum.sha256
DIR=$(dirname "${1}")
BASENAME=$(basename "${1}")
checksum_file=${DIR}/${BASENAME}.sha256
value1=`cat $temp_folder/checksum.sha256`
value2=`cat $checksum_file`
echo "Value1: $value1"
echo "Value2: $value2"
if [ $value1 != $value2 ]
        then
                echo "Mismatch in checksum values"
		exit
fi
 

for i in $(echo $instance_list | tr "," "\n")
do
  # process
	PID=`ps -eaf | grep $i  | grep -v grep | awk '{print $2}'`
        if [ "" !=  "$PID" ]; then
                echo "Instance of $i is running."
                exit
        fi

done


lock_file=$PWD/`basename $0`.lock

if fuser $lock_file > /dev/null 2>&1; then
    echo "WARNING: Other instance of $(basename $0) running."
    exit
fi
exec 3> $lock_file 

echo "Restore job started"

if [ ! -d "$temp_folder" ]
then
    mkdir -p $temp_folder   
fi

echo "Extracting tar " $1
tar -xvf $1 -C $temp_folder

echo "Stopping Postgresql"
service postgresql stop

rm -rf $db_before_restore
if [ ! -d "$db_before_restore" ]
then
    mkdir -p $db_before_restore
fi

echo "Copying database to " $db_before_restore
cp -R $db_base $db_before_restore
if [ $? -ne 0 ]; then
    rm -rf $db_before_restore
    exit
fi

echo "Deleting database from " $db_base
rm -rf $db_base

echo "Restoring database from " $temp_folder$db_base
cp -R $temp_folder$db_base $db_base_temp

echo "Deleting obsolete WAL files"
rm -rf $db_base/pg_xlog

echo "Removing postmaster.pid"
rm -rf $db_base/postmaster.pid

echo "Removing postmaster.opts"
rm -rf $db_base/postmaster.opts

echo "Removing pg_replslot files"
rm -rf $db_base/pg_replslot/*

echo "Removing backup label files"
rm -rf $db_base/backup_label
rm -rf $db_base/backup_label.old

echo "Removing pg_tblspc files"
rm -rf $db_base/pg_tblspc/*

if [ -d "$db_before_restore/main/pg_xlog" ]
then
echo "Copying unarchived WAL files to " $db_base/pg_xlog
cp -R $db_before_restore/main/pg_xlog $db_base
fi

if [ ! -d "$db_before_restore/main/pg_xlog" ]
then
	mkdir $db_base/pg_xlog
fi

echo "Restoring PAS jars from " $temp_folder$pas_jars
cp -R $temp_folder$pas_jars $pas_jars_temp

echo "Restoring Roche Connect jars from " $temp_folder$connect_jars
cp -R $temp_folder$connect_jars $connect_jars_temp

echo "Restoring Roche Connect web files from " $temp_folder$web_apps
cp -R $temp_folder$web_apps $web_apps_temp

echo "Restoring Roche Connect yaml files from " $temp_folder$yaml
cp -R $temp_folder$yaml $yaml_temp

echo "Restoring restore.json from " $temp_folder$restore_json
cp -R $temp_folder$restore_json/restore.json $restore_json/restore.json

echo "Restoring postgresql.conf file from " $temp_folder$db_conf
cp -R $temp_folder$db_conf/postgresql.conf $db_conf/postgresql.conf

echo "Restoring pg_hba.conf from " $temp_folder$db_conf
cp -R $temp_folder$db_conf/pg_hba.conf $db_conf/pg_hba.conf

echo "Restoring pg_ident.conf from " $temp_folder$db_conf
cp -R $temp_folder$db_conf/pg_ident.conf $db_conf/pg_ident.conf

echo "Removing obsolete archived WAL file"
rm -rf $pg_wal_files

echo "Restoring WAL files from " $temp_folder$pg_wal_files
cp -R $temp_folder$pg_wal_files $pg_wal_files_temp
chown -R postgres:postgres $pg_wal_files

echo "Copying recovery.conf from " $PWD
cp -R $PWD/recovery.conf $db_base

echo "Starting Postgresql"
chown -R postgres:postgres $db_base
service postgresql start

echo "Removing temporary folder " $temp_folder
rm -rf $temp_folder

rm -f $lock_file

echo $"\nRun the below commands if the database crashed or in case of invalid checkpoint error.\n
Step 1: sudo su \n
Step 2: su postgres \n
Step 3: Run this below command \n
/usr/lib/postgresql/9.6/bin/pg_resetxlog -f -D /var/lib/postgresql/9.6/main \n
step 4: Restart Postgresql"

