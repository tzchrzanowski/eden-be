#### eden backend 
####
stack used:
  java spring,
  connecting to mySql database

### Preparation:
Local database has to be created and running. 
There is database dump file in root of this repository ```eden_db.sql```.
This file can be used to update/override existing database on local machine:
```mysql -u username -p eden_db < dumpfile.sql``` 
eden_db is name of database for consistency.

Edit config file located at: ```src/main/resources/application.properties```
Fill username and password for local mySQL database. 
```
spring.datasource.username=placeholder
spring.datasource.password=placeholder
```

### install / generate java build file:
```mvn clean install```

### run java using 'nohup':
```nohup java -jar be.jar > spring_boot.log 2>&1 &```
