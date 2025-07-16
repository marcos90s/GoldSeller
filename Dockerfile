FROM eclipse-temurin:17
LABEL maintainer='marcosvieira90ss@gmail.com'
WORKDIR /goldseller
COPY target/goldSellerAPI-0.0.1-SNAPSHOT.jar /goldseller/gold-seller.jar
ENTRYPOINT ["java","-jar", "gold-seller.jar"]
