
# Projeto NiceBank API

Uma API bem simples para simular transações de banco.

## Arquitetura

* Java 11 
* Maven 3.8
* Spring Boot 2.7
* Oracle 11 XE

## Setup

O sistema espera alguns objetos para funcionar, o script [oracle-setup.sql](doc/sql/oracle-setup.sql) deve ser executado no schema da aplicação.

## Documentação 

A documentação em formato OpenAPI Specification está disponível [em arquivo](doc/api/nicebank-api-docs.json) ou online quando o sistema subir. 

## Run 

Para executar o sistema localmente, execute:

```
mvn spring-boot:run
```

Em seguida acesse com o navegador: [http://localhost:8080](http://localhost:8080)


## Build 

Para criar uma imagem do docker para rodar em containers, execute:

```
mvn spring-boot:build-image
```

Para executar a imagem do docker criada, execute:

```
docker run -it -p8000:8080 -e SPRING_DATASOURCE_URL='jdbc:oracle:thin:@//192.168.1.20:1521/xe' -e SPRING_DATASOURCE_USERNAME='fb' -e SPRING_DATASOURCE_PASSWORD='fb' nicebank-api:1.0.0-SNAPSHOT
```

Nessa chamada é possível customizar a chamada do banco de dados remoto através das variáveis SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME e SPRING_DATASOURCE_PASSWORD.

Em seguida acesse com o navegador: [http://localhost:8000](http://localhost:8000)


Obs: existe um bug no driver da Oracle com docker , se ocorrer o erro  _ORA-01882: timezone region not found_  adicione também a flag: 

```
-e JAVA_TOOL_OPTIONS='-Doracle.jdbc.timezoneAsRegion=false'
```

