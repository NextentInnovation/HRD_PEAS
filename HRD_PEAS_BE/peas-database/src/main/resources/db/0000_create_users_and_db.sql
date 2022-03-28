
-- drop own root
DROP USER if exists peas_root;
-- drop peas users
DROP USER if exists peas_maintainer;
DROP USER if exists peas_backend;
-- drop database
DROP DATABASE IF EXISTS peas;

-- Create database
CREATE DATABASE peas CHARACTER SET = 'utf8';

-- create own root
CREATE USER 'peas_root'@'%' IDENTIFIED BY 'F3i@XEP9';
ALTER USER 'peas_root'@'%' IDENTIFIED WITH mysql_native_password BY 'F3i@XEP9';
GRANT ALL PRIVILEGES ON *.* TO 'peas_root'@'%';
GRANT GRANT OPTION ON *.* TO 'peas_root'@'%';

-- create peas users
-- Aki kibocsájtja az SQL-eket
CREATE USER 'peas_maintainer'@'%' IDENTIFIED BY 'F3i@XEP9';
ALTER USER 'peas_maintainer'@'%' IDENTIFIED WITH mysql_native_password BY 'F3i@XEP9';
GRANT ALL ON peas.* TO peas_maintainer@'%';
GRANT SELECT ON *.* TO peas_maintainer@'%';


-- Backend oldali felhasználó
CREATE USER 'peas_backend'@'%' IDENTIFIED BY 'F3i@XEP9';
ALTER USER 'peas_backend'@'%' IDENTIFIED WITH mysql_native_password BY 'F3i@XEP9';
GRANT ALL ON peas.* TO peas_backend@'%';

