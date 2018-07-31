FROM openjdk:8-jdk-alpine
ADD target/CensureRestServices.jar ws_CensureRestServices_sf.jar
EXPOSE 8089
ENTRYPOINT ["java","-jar","ws_CensureRestServices_sf.jar"]