FROM openjdk:17.0

# Make folders for log files and access token
# It allows to mount these folders to outside of container (docker volume or host)
RUN mkdir -p /opt/yummy-recipes/logs && mkdir -p /opt/yummy-recipes/data
COPY target/yummy-recipes.jar /opt/yummy/
WORKDIR /opt/yummy/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/yummy/yummy-recipes.jar"]