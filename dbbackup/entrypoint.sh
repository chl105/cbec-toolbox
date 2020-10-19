#!/bin/sh

MYSQL_HOST=mysql
MYSQL_PORT=3306
DB_USER=root
DB_PASSWORD=${MYSQL_ROOT_PASSWORD}

for f in /docker-entrypoint-initdb.d/*.sql; do
  echo "Execute SQL script $f"
  mysql -h${MYSQL_HOST} -P${MYSQL_PORT} -u${DB_USER} -p${DB_PASSWORD} <"$f"
  if [ $? -ne 0 ]; then
    exit 1
  fi
done

echo "Start crond"

crond -f
