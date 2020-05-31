#!/bin/sh

java -jar /app.jar

MYSQL_HOST=mysql
MYSQL_PORT=4000
DB_USER=root
PASSWORD_FILE=/tmp/.pass
DB_PASSWORD=`cat ${PASSWORD_FILE}`

for f in /docker-entrypoint-initdb.d/*.sql; do
    echo "Execute SQL script $f"
    mysql -h${MYSQL_HOST} -P${MYSQL_PORT} -u${DB_USER} -p${DB_PASSWORD} < "$f"
    if [ $? -ne 0 ]; then
        exit 1
    fi
done

echo "Start crond"

crond -f

