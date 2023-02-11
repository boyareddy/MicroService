log_path=/var/log/connect
mkdir -p $log_path
read -p "Please enter postgres password: "  pgpassword
export PGPASSWORD=$pgpassword
psql -h localhost -U postgres -p 5432 -a -w -f MasterScript.sql  > $log_path/MasterScript.log 2>&1
if [ $? -eq 0 ]; then
    echo "MasterScript script executted successfully..."
else
    echo "MasterScript script execution failed..."
    exit
fi


