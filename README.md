## Это дипломный проект для колледжа ГБПОУ "КТК"

### Взаимодействие с docker через Makefile
```bash
make restart
```

```bash
make up
```

```bash
make logs
```


### Файлы для старта проекта

src/main/resources/application.yaml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:${DB_PORT}/${POSTGRES_DB}
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl_auto:validate
  flyway:
    enabled: true
    baseline-on-migrate: true
  jackson:
    default-property-inclusion: non_null
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB
  mvc:
    log-request-details: true

minio:
  url: http://localhost:9000
  access-key: ${STORAGE_USER}
  secret-key: ${STORAGE_PASSWORD}

logging:
  level:
    org.springframework.web: DEBUG
    org.springframework.web.servlet: DEBUG
    org.springframework.http.converter: DEBUG

jwt:
  secret: ${JWT_SECRET}
```

.env
```.env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=my_secret_password
POSTGRES_DB=lms_db
DB_PORT=5433

STORAGE_USER=detalit
STORAGE_PASSWORD=secretpassword
STORAGE_PORT=9090
STORAGE_UI_PORT=9091
STORAGE_BUCKET=lms-bucket
STORAGE_ENDPOINT=localhost:9000


JWT_SECRET=super_long_secret_key_change_me
```