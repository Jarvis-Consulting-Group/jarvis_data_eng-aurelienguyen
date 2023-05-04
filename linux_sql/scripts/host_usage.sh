#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# need 5 parameters
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

#Save machine statistics in MB and current machine hostname to variables
vmstat_mb=$(vmstat --unit M)

#Retrieve usage specification variables
memory_free=$(echo "$vmstat_mb" | awk '{print $4}'| tail -n1 | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk -v col="15" '{print $col}' | xargs)
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk -v col="14" '{print $col}' | xargs)
disk_io=$(vmstat -d | tail -1 | awk -v col="10" '{print $col}' | xargs)
disk_available=$(df -BM / | tail -1 | awk -v col="4" '{print $col}' | xargs)
timestamp=$(date '+%Y-%m-%d %H:%M:%S')

# Insert server usage data into host_usage table1
insert_stmt="INSERT INTO host_usage(
                timestamp, host_id, memory_free, cpu_idel, cpu_kernel, disk_io, disk_available
                )
                SELECT
                '$timestamp', id, '$memory_free', '$cpu_idle', '$cpu_kernel',
                '$disk_io', '${disk_available%%M}'
                FROM host_info
                WHERE host_info.hostname='$psql_host'";

#set up env var for pql cmd
export PGPASSWORD=$psql_password
#Insert date into a database
psql -h $psql_host -p $psql_port -d $db_name -U $psql_user -c "$insert_stmt"
exit $?