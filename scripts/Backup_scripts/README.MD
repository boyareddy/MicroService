Run the below commands if the database crashed or in case of invalid checkpoint error. 
Step 1: sudo su
Step 2: su postgres
Step 3: Run this below command 
/usr/lib/postgresql/9.6/bin/pg_resetxlog -f -D /var/lib/postgresql/9.6/main
step 4: Restart Postgres
