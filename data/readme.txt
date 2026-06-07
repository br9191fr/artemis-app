# ---------------------------------------------------
set password encryption in postgres
show password_encryption ;
select * from pg_catalog.pg_authid ;
set password_encryption = 'scram-sha-256';
alter role seal set password_encryption = 'scram-sha-256';
alter role seal with password 'the-password';
select * from pg_catalog.pg_authid ;

# ---------------------------------------------------

check ip address of the host
In windows as admin
ipconfig /all
then check with value in DatabaseService.java

From powershell connect to pg messages via docker @ 172.28.160.1