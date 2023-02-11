log_path=/var/log/connect
mkdir -p $log_path
read -p "Please enter postgres password: "  pgpassword
export PGPASSWORD=$pgpassword
psql -h localhost -U postgres -p 5432 -a -w -f drop_db.sql  > $log_path/dropDB.log 2>&1
if [ $? -eq 0 ]; then
    echo "Drop DB script executted successfully..."
else
    echo "Drop DB script execution failed..."
    exit
fi
