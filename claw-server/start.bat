@echo off
setlocal

set M2_REPO=C:\Users\Challenge\.m2\repository
set CP=target\classes

call :add_jar org/springframework/boot/spring-boot/3.2.5/spring-boot-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-autoconfigure/3.2.5/spring-boot-autoconfigure-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-starter/3.2.5/spring-boot-starter-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-starter-web/3.2.5/spring-boot-starter-web-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-starter-json/3.2.5/spring-boot-starter-json-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-starter-tomcat/3.2.5/spring-boot-starter-tomcat-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-starter-security/3.2.5/spring-boot-starter-security-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-starter-validation/3.2.5/spring-boot-starter-validation-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-starter-websocket/3.2.5/spring-boot-starter-websocket-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-starter-aop/3.2.5/spring-boot-starter-aop-3.2.5.jar
call :add_jar org/springframework/boot/spring-boot-starter-logging/3.2.5/spring-boot-starter-logging-3.2.5.jar
call :add_jar org/springframework/spring-core/6.1.6/spring-core-6.1.6.jar
call :add_jar org/springframework/spring-context/6.1.6/spring-context-6.1.6.jar
call :add_jar org/springframework/spring-beans/6.1.6/spring-beans-6.1.6.jar
call :add_jar org/springframework/spring-aop/6.1.6/spring-aop-6.1.6.jar
call :add_jar org/springframework/spring-expression/6.1.6/spring-expression-6.1.6.jar
call :add_jar org/springframework/spring-web/6.1.6/spring-web-6.1.6.jar
call :add_jar org/springframework/spring-webmvc/6.1.6/spring-webmvc-6.1.6.jar
call :add_jar org/springframework/spring-messaging/6.1.6/spring-messaging-6.1.6.jar
call :add_jar org/springframework/spring-websocket/6.1.6/spring-websocket-6.1.6.jar
call :add_jar org/springframework/spring-jcl/6.1.6/spring-jcl-6.1.6.jar
call :add_jar org/springframework/security/spring-security-core/6.2.4/spring-security-core-6.2.4.jar
call :add_jar org/springframework/security/spring-security-web/6.2.4/spring-security-web-6.2.4.jar
call :add_jar org/springframework/security/spring-security-config/6.2.4/spring-security-config-6.2.4.jar
call :add_jar org/springframework/security/spring-security-crypto/6.2.4/spring-security-crypto-6.2.4.jar
call :add_jar org/springframework/security/spring-security-messaging/6.2.4/spring-security-messaging-6.2.4.jar
call :add_jar com/fasterxml/jackson/core/jackson-databind/2.15.4/jackson-databind-2.15.4.jar
call :add_jar com/fasterxml/jackson/core/jackson-core/2.15.4/jackson-core-2.15.4.jar
call :add_jar com/fasterxml/jackson/core/jackson-annotations/2.15.4/jackson-annotations-2.15.4.jar
call :add_jar com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.15.4/jackson-datatype-jsr310-2.15.4.jar
call :add_jar com/fasterxml/jackson/dataformat/jackson-dataformat-csv/2.15.4/jackson-dataformat-csv-2.15.4.jar
call :add_jar com/fasterxml/jackson/module/jackson-module-parameter-names/2.15.4/jackson-module-parameter-names-2.15.4.jar
call :add_jar org/apache/tomcat/embed/tomcat-embed-core/10.1.24/tomcat-embed-core-10.1.24.jar
call :add_jar org/apache/tomcat/embed/tomcat-embed-el/10.1.24/tomcat-embed-el-10.1.24.jar
call :add_jar org/apache/tomcat/embed/tomcat-embed-websocket/10.1.24/tomcat-embed-websocket-10.1.24.jar
call :add_jar org/apache/tomcat/embed/tomcat-annotations-api/10.1.24/tomcat-annotations-api-10.1.24.jar
call :add_jar jakarta/servlet/jakarta.servlet-api/6.0.0/jakarta.servlet-api-6.0.0.jar
call :add_jar jakarta/validation/jakarta.validation-api/3.0.2/jakarta.validation-api-3.0.2.jar
call :add_jar jakarta/annotation/jakarta.annotation-api/2.1.1/jakarta.annotation-api-2.1.1.jar
call :add_jar jakarta/persistence/jakarta.persistence-api/3.1.0/jakarta.persistence-api-3.1.0.jar
call :add_jar jakarta/websocket/jakarta.websocket-api/2.1.1/jakarta.websocket-api-2.1.1.jar
call :add_jar jakarta/xml/bind/jakarta.xml.bind-api/4.0.1/jakarta.xml.bind-api-4.0.1.jar
call :add_jar org/hibernate/validator/hibernate-validator/8.0.1.Final/hibernate-validator-8.0.1.Final.jar
call :add_jar com/baomidou/mybatis-plus-spring-boot3-starter/3.5.5/mybatis-plus-spring-boot3-starter-3.5.5.jar
call :add_jar com/baomidou/mybatis-plus-core/3.5.5/mybatis-plus-core-3.5.5.jar
call :add_jar com/baomidou/mybatis-plus-extension/3.5.5/mybatis-plus-extension-3.5.5.jar
call :add_jar org/mybatis/spring/boot/mybatis-spring-boot-starter/3.0.3/mybatis-spring-boot-starter-3.0.3.jar
call :add_jar org/mybatis/spring/mybatis-spring/3.0.3/mybatis-spring-3.0.3.jar
call :add_jar org/mybatis/mybatis/3.5.13/mybatis-3.5.13.jar
call :add_jar com/alibaba/druid-spring-boot-3-starter/1.2.21/druid-spring-boot-3-starter-1.2.21.jar
call :add_jar com/alibaba/druid/1.2.21/druid-1.2.21.jar
call :add_jar com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
call :add_jar io/jsonwebtoken/jjwt-api/0.12.5/jjwt-api-0.12.5.jar
call :add_jar io/jsonwebtoken/jjwt-impl/0.12.5/jjwt-impl-0.12.5.jar
call :add_jar io/jsonwebtoken/jjwt-jackson/0.12.5/jjwt-jackson-0.12.5.jar
call :add_jar org/springdoc/springdoc-openapi-starter-webmvc-ui/2.3.0/springdoc-openapi-starter-webmvc-ui-2.3.0.jar
call :add_jar org/springdoc/springdoc-openapi-starter-webmvc-api/2.3.0/springdoc-openapi-starter-webmvc-api-2.3.0.jar
call :add_jar org/springdoc/springdoc-openapi-starter-common/2.3.0/springdoc-openapi-starter-common-2.3.0.jar
call :add_jar io/swagger/core/v3/swagger-models/2.2.19/swagger-models-2.2.19.jar
call :add_jar io/swagger/core/v3/swagger-annotations/2.2.19/swagger-annotations-2.2.19.jar
call :add_jar io/swagger/core/v3/swagger-core/2.2.19/swagger-core-2.2.19.jar
call :add_jar org/aspectj/aspectjweaver/1.9.21/aspectjweaver-1.9.21.jar
call :add_jar ch/qos/logback/logback-classic/1.4.14/logback-classic-1.4.14.jar
call :add_jar ch/qos/logback/logback-core/1.4.14/logback-core-1.4.14.jar
call :add_jar org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar
call :add_jar org/slf4j/jul-to-slf4j/2.0.9/jul-to-slf4j-2.0.9.jar
call :add_jar org/yaml/snakeyaml/2.2/snakeyaml-2.2.jar
call :add_jar com/fasterxml/classmate/1.6.0/classmate-1.6.0.jar
call :add_jar org/jboss/logging/jboss-logging/3.5.3.Final/jboss-logging-3.5.3.Final.jar
call :add_jar com/google/protobuf/protobuf-java/3.24.4/protobuf-java-3.24.4.jar
call :add_jar org/glassfish/jaxb/jaxb-runtime/4.0.4/jaxb-runtime-4.0.4.jar
call :add_jar org/glassfish/jaxb/jaxb-core/4.0.4/jaxb-core-4.0.4.jar
call :add_jar org/glassfish/jaxb/txw2/4.0.4/txw2-4.0.4.jar
call :add_jar com/sun/istack/istack-commons-runtime/4.1.2/istack-commons-runtime-4.1.2.jar
call :add_jar io/micrometer/micrometer-observation/1.12.5/micrometer-observation-1.12.5.jar
call :add_jar io/micrometer/micrometer-commons/1.12.5/micrometer-commons-1.12.5.jar
call :add_jar org/apache/tomcat/tomcat-util/10.1.24/tomcat-util-10.1.24.jar
call :add_jar org/apache/tomcat/tomcat-util-scan/10.1.24/tomcat-util-scan-10.1.24.jar
call :add_jar org/webjars/swagger-ui/5.10.3/swagger-ui-5.10.3.jar
call :add_jar org/webjars/webjars-locator-core/0.52/webjars-locator-core-0.52.jar
call :add_jar org/springframework/boot/spring-boot-starter-jdbc/3.2.5/spring-boot-starter-jdbc-3.2.5.jar
call :add_jar org/springframework/spring-jdbc/6.1.6/spring-jdbc-6.1.6.jar
call :add_jar org/springframework/spring-tx/6.1.6/spring-tx-6.1.6.jar
call :add_jar com/zaxxer/HikariCP/5.0.1/HikariCP-5.0.1.jar
call :add_jar org/javassist/javassist/3.29.2-GA/javassist-3.29.2-GA.jar

java -Dspring.profiles.active=dev -cp "%CP%" com.claw.ClawApplication
goto :eof

:add_jar
set JAR=%M2_REPO%\%1
if exist "%JAR%" (
    set CP=%CP%;%JAR%
) else (
    echo WARNING: Missing %JAR%
)
goto :eof
