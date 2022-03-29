# easybim-back-end
**Project: Spring Boot, MongoDB, API REST, JUnit5 and MockMVC**

Link to the interface:
> http://200.19.33.250:9004/

## Spring REST API

Documentation is an essential part of building REST APIs.

We can access the documents in JSON format at:
> http://200.19.33.250:9004/api-docs/

The OpenAPI definitions are in JSON format by default. For yaml format, we can obtain the definitions at:
> http://200.19.33.250:9004/api-docs.yaml

To view and interact with the API’s resources without having any of the implementation logic in place, try Swagger UI:
> http://200.19.33.250:9004/swagger.html

## Remains to do
* Deploy application

## Project properties and requirements: 
* Java 11
* Spring Boot 2.5.6
* Project lombok 1.18.22
* MongoDB 50
* JUnit5 and MockMVC


> https://dicasdejava.com.br/como-configurar-o-lombok-no-eclipse/

# - para o ambiente de desenvolvimento, instale o mongodb desktop e execute o seguintes comandos
docker pull mongo
docker run --name mongodb --hostname mongodb -d -p 27017:27017 mongo

# altere seu arquivo de hosts e aponte o 'mongodb' para o seu local host. Vai precisar reiniciar o serviço de rede
Windows = c:\Windows\System32\Drivers\etc\hosts
