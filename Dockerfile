# base on
FROM eclipse-temurin:21.0.3_9-jdk

# 建立資料夾
WORKDIR /usr/src/app

# 複製檔案到容器內
COPY ./target/lazycat-linebot*.jar ./lazycat-linebot.jar

# 暴露port口
EXPOSE 5000

CMD ["java","-jar","./lazycat-linebot.jar"]