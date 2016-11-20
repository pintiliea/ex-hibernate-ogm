# The Hibernate OGM + Infinity tutorial running on Spring Boot

Build with

```
mvn clean install
```

Run with

```
java -jar target/tut-hiber-ogm-0.1.0-SNAPSHOT.jar
```

You should see something like this

```
2016-11-20 22:57:54.513  INFO 15544 --- [main] tu.hiber.Main  : Creating collie 'Dina'...
2016-11-20 22:57:54.708  INFO 15544 --- [main] tu.hiber.Main  : Dina-ID = 1
2016-11-20 22:57:54.708  INFO 15544 --- [main] tu.hiber.Main  : Retrieving entity of class 'Dog' with ID = 1
2016-11-20 22:57:54.712  INFO 15544 --- [main] tu.hiber.Main  : Found 'Collie' named 'Dina'
2016-11-20 22:57:54.713  INFO 15544 --- [main] tu.hiber.Main  : Exit with Ctrl+C
2016-11-20 22:57:54.714  INFO 15544 --- [main] tu.hiber.Main  : Started Main in 13.951 seconds (JVM running for 14.752)
```

Press `Ctrl+C` to quit the JVM.
