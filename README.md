# ABN AMRO Java Exercise

## Task

### Objective
Create a standalone java application which allows users to manage their favourite recipes. It should
allow adding, updating, removing and fetching recipes. Additionally users should be able to filter
available recipes based on one or more of the following criteria:

1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.

  For example, the API should be able to handle the following search requests:
   * All vegetarian recipes
   * Recipes that can serve 4 persons and have “potatoes” as an ingredient
   * Recipes without “salmon” as an ingredient that has “oven” in the instructions.

### Requirements
   Please ensure that we have some documentation about the architectural choices and also how to
   run the application. The project is expected to be delivered as a GitHub (or any other public git
   hosting) repository URL.
   All these requirements needs to be satisfied:

1. It must be a REST application implemented using Java (use a framework of your choice)
2. Your code should be production-ready.
3. REST API must be documented
4. Data must be persisted in a database
5. Unit tests must be present
6. Integration tests must be present

-----------------------------------------------------------------

# Installation

### Description
+ Application is build on top of Spring Boot Framework and uses Elasticsearch as a main database
+ Application provides CRUD functionality for recipes via REST API and structured search with paging capability
+ OpenAPI documentation 

### Requirements
+ **JDK 17** - to compile and run application
+ **mvn** - to compile and run java version
+ **docker + docker-compose** - to build and run application stack
+ **linux or linux virtual machine** - all provided scripts are written specifically for Linux based OS

### Build & Run

+ clone repository ```git clone https://github.com/mbaydukov/yummy.git```
+ switch to the project folder ```cd ./yummy```
+ build and run docker-compose stack with provided script ```bash run.sh```
+ openapi docs/swagger-ui available by link http://localhost:8080/swagger-ui/index.html
+ application API is available via http://localhost:8080/api/recipes
+ link to spring actuator healthcheck http://localhost:8080/actuator/health 
