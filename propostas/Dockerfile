#Definição de imagem base
FROM openjdk:11

#Caminho do JAR como argumento
ARG JAR_FILE=target/*.jar

#Copiar JAR_FILE e renomear para app.jar
COPY ${JAR_FILE} app.jar

#Comando necessário para start
ENTRYPOINT ["java","-jar","/app.jar"]


#Gerar JAR (Utilizar no PATH de pom.xml): mvn clean package
#Criar Imagem: docker image build -t zupacademy/propostas:latest .
#Rodar: docker container run -d -p 8080:8080 zupacademy/propostas --name proposta

#Obs.: Como precisa de Dependências, o ideal é dar o build e rodar o docker-compose.