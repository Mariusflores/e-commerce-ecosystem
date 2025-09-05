#!/bin/bash
set -e

# Create databases
mysql -u root -p"$MYSQL_ROOT_PASS" -e "CREATE DATABASE IF NOT EXISTS $MYSQL_INVENTORY_DB;"
mysql -u root -p"$MYSQL_ROOT_PASS" -e "CREATE DATABASE IF NOT EXISTS $MYSQL_ORDER_DB;"

# Create user if it doesn't exist
mysql -u root -p"$MYSQL_ROOT_PASS" -e "CREATE USER IF NOT EXISTS '$MYSQL_USER'@'%' IDENTIFIED BY '$MYSQL_PASS';"

# Grant privileges for both DBs
mysql -u root -p"$MYSQL_ROOT_PASS" -e "GRANT ALL PRIVILEGES ON $MYSQL_INVENTORY_DB.* TO '$MYSQL_USER'@'%';"
mysql -u root -p"$MYSQL_ROOT_PASS" -e "GRANT ALL PRIVILEGES ON $MYSQL_ORDER_DB.* TO '$MYSQL_USER'@'%';"

# Apply privileges immediately
mysql -u root -p"$MYSQL_ROOT_PASS" -e "FLUSH PRIVILEGES;"
