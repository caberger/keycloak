# Désarmement

We spend too much time working into useless frameworks. Let us try to build a lean and mean REST Service with all bells and whistles.

## First reading
see [restlet-app](https://github.com/restlet/restlet-framework-java)

## Start a minimum project
see [Restlet in Action](https://github.com/karygauss03/CS-Library)

### create project
```bash
mvn archetype:generate -DgroupId=at.aberger.demo -DartifactId=rest-demo  -DarchetypeVersion=1.5 -DinteractiveMode=false -DarchetypeArtifactId=maven-archetype-quickstart
rm -rf rest-demo/.mvn
```

## add dependencies

see [Sample POM](https://restlet.github.io/downloads/current/)

## Development

### mvnd

Use [mvnd](https://github.com/apache/maven-mvnd) for development