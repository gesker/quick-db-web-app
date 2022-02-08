# A Quick Database Aware Web App

### Set environment variables for that the application can reference in order to reach your database

```bash
export POSTGRESQL_HOST=server.withyourdatabase.com
export POSTGRESQL_PORT=5432
export POSTGRESQL_DATABASE=quickDb
export POSTGRESQL_USER=yourUsername
export POSTGRESQL_PASSWORD=yourSecretPassword
```

### Run the application using the wildfly-jar-maven-plugin

```bash
mvn wildfly-jar:dev-watch
```

### You'll be able to reach the running application [here] (http://localhost:8080/quick)

http://localhost:8080/quick