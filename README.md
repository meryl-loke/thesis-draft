# Simple example of using Rosette API with the Java Binding

## For Maven User

See details in [`pom.xml`](pom.xml) and [`Example.java`](src/main/java/Example.java)

- Compile the code

```
mvn clean verify
```

- Run the example

```
mvn exec:java -Dexec.mainClass=Example -Dapi.key=<your_api_key>

...
Washington D.C. (Q61) - LOCATION
USA (Q30) - LOCATION
...
```

## For Gradle User

See details in [`build.gradle`](build.gradle) and [`Example.java`](src/main/java/Example.java)

```
gradle runExample -Dapi.key=<your_api_key>

...
Washington D.C. (Q61) - LOCATION
USA (Q30) - LOCATION
...
```

## For Ant/Ivy User

See details in [`ivy.xml`](ivy.xml), [`build.xml`](build.xml), and [`Example.java`](src/main/java/Example.java)

```
ant -Dapi.key=<your_api_key>

...
     [java] Washington D.C. (Q61) - LOCATION
     [java] USA (Q30) - LOCATION
...
```

