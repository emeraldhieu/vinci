#!/bin/bash

set -e
set -u

function create_user_and_database() {
	local database=$1
	local user=$database
	echo "  Creating user and database '$database'"
	PGPASSWORD=$POSTGRES_USER psql --variable ON_ERROR_STOP=1 --username $POSTGRES_PASSWORD <<-EOSQL
	    CREATE USER "$user" WITH PASSWORD '$user';

	    CREATE DATABASE "$database" ENCODING "UTF8";

	    GRANT ALL PRIVILEGES ON DATABASE "$database" TO "$user";
EOSQL

  # Log in with superuser on the database to grant permissions on public schema
	PGPASSWORD=$POSTGRES_USER psql --variable ON_ERROR_STOP=1 --username $POSTGRES_PASSWORD --dbname $database  <<-EOSQL
      /*
        Grant permission to create tables on public schema for a user.
        Otherwise, "ERROR:  permission denied for schema public" occurs.
      */
      GRANT ALL ON SCHEMA public TO "$user";
EOSQL
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
	echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
	for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
		create_user_and_database $db
	done
	echo "Multiple databases created"
fi