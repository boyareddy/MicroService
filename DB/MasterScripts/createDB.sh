log_path=/var/log/connect
mkdir -p $log_path
read -p "Please enter postgres password: "  pgpassword
export PGPASSWORD=$pgpassword
psql -h localhost -U postgres -p 5432 -a -w -f create_db.sql  > $log_path/CreateDB.log 2>&1
if [ $? -eq 0 ]; then
    echo "Create DB script executted successfully..."
else
    echo "Create DB script execution failed..."
    exit
fi

