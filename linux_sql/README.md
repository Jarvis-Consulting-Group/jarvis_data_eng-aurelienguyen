# Introduction
The Linux Cluster Monitoring Agent is a project that records the hardware specifications of a server as well as its resource usage on a minute by minute. The data is then stored in a PostgreSQL RDBMS for future resource planning purposes.

## Who are the users?
The users of this project are the Jarvis Linux Cluster Administration (LCA) team. They are responsible for the management of a Linux cluster comprised of 10 nodes/servers running CentOS 7. The LCA team needs the following data from each node: hardware specifications and node resource usage in real time. The data would be collected in an RDBMS to create reports for future resource planning and management.

## What are the technologies used?
The technologies used for this project are the following:
- **Git**: code management and storage
- **JRV**: remote desktop
- **Bash**: scripts
- **Docker**: PSQL instance provisioning
- **PostgreSQL**: hardware specifications and resource usage data storage 

# Quick Start
Start a PSQL instance using psql_docker.sh
```
# script usage
./scripts/psql_docker.sh start|stop|create [db_username] [db_password]

# create a psql docker container with the given username and password
./scripts/psql_docker.sh create [db_username] [db_password]

# start the stopped psql docker container
./scripts/psql_docker.sh start

# stop the running psql docker container
./scripts/psql_docker.sh stop
```
Create tables using ddl.sql
```
# execute ddl.sql script on the database
psql -h [psql_host] -U [psql_user] -d [db_name] -f sql/ddl.sql
```
Insert hardware specs data into the DB using host_info.sh
```
# script usage
./scripts/host_info.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```
Insert hardware usage data into the DB using host_usage.sh
```
# script usage
./scripts/host_usage.sh [psql_host] [psql_port] [db_name] [psql_user] [psql_password]
```
Crontab setup
```
#edit crontab jobs
bash > crontab -e

#execute host_usage.sh every minute (add this to crontab)
* * * * * [absolute path of host_usage.sh] [psql_host] [psql_port] [db_name] [psql_user] [psql_password] > [log_file]
```

# Implementation
1. Setup IDE to run Linux server with CentOS 7
2. Provision PSQL instance using Docker
3. Create `psql_docker.sh` script to create, start and stop the PSQL Docker container
4. Create database using PSQL CLI
5. Create `ddl.sql` script to create a table to store hardware specifications and a table to store resource usage data automatically
6. Create `host_info.sh` and `host_usage.sh` scripts to collect hardware specifications and server usage data respectively, and store them into the PSQL database
7. Automate `host_usage.sh` using crontab

## Architecture
![Architecture](./assets/linux_architecture.drawio)

## Scripts
Shell script description and usage 
- **psql_docker.sh**
Creates a PSQL docker container with the given username and password, starts or stops the psql docker container. Takes 3 inputs: command (create, start or stop), username and password.
- **host_info.sh**
Collects hardware specification data and stores them into the PSQL database. Takes 5 inputs: PSQL host, PSQL port, database name, PSQL username, PSQL password.
- **host_usage.sh**
Collects server usage data and stores them into the PSQL database. Takes 5 inputs: PSQL host, PSQL port, database name, PSQL username, PSQL password.  
- **crontab**
Deploys and automates `host_usage.sh` script to be executed every minute.

## Database Modeling
**Host Info Table**
  | Column Name | Data Type |
  | ------------- | ------------- |
  | id | SERIAL NOT NULL |
  | hostname  | VARCHAR NOT NULL |
  | cpu_number  | INT2 NOT NULL |
  | cpu_architecture | VARCHAR NOT NULL |
  | cpu_model  | VARCHAR NOT NULL |
  | cpu_mhz  | FLOAT8 NOT NULL |
  | L2_cache  | INT4 NOT NULL |
  | total_mem | INT4 NOT NULL |
  | timestamp  | TIMESTAMP NOT NULL

**Host Usage Table**
  | host_usage | Data Type |
  | ------------- | ------------- |
  | timestamp | TIMESTAMP NOT NULL |
  | host_id  | SERIAL NOT NULL |
  | memory_free  | INT4 NOT NULL |
  | cpu_idle | INT2 NOT NULL |
  | cpu_kernel  |INT2 NOT NULL |
  | disk_io  | INT4 NOT NULL |
  | disk_available  | INT4 NOT NULL |

# Test
```
# Test psql_docker.sh (by checking if the jrvs-psql container has been created)
docker container ls -a -f name=jrvs-psql
# Test psql_docker.sh (by checking if the jrvs-psql container is running)
docker ps -f name=jrvs-psql
# Test host_info.sh (by checking the content of the host_info database)
SELECT * FROM host_info;
# Test host_usage.sh (by checking the content of the host_usage database)
SELECT * FROM host_usage;
```

# Deployment
1. Download code from GitHub
2. Install Docker
3. Create PSQL docker container using `psql_docker.sh`
4. Create tables using `ddl.sql`
5. Insert hardware specification data into database using `host_info.sh`
6. Insert server usage data into database through `host_usage.sh` automatically every minute with crontab

# Improvements
- Handle hardware updates
- Create website to display hardware and usage data
