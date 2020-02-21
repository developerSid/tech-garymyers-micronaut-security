# Replicating Nullable Auth Bug

## Steps used to create project
1. `mn create-app tech-garymyers-comics-api --lang kotlin --features=spock,security-jwt`
2. `rm -rf tech-garymyers-micronaut-security/src/test/kotlin/io/`
3. Created in *src/main/kotlin* 
   1. __tech.garymyers.micronaut.security.authentication.AuthenticatedUserInformation.kt__
   2. __tech.garymyers.micronaut.security.authentication.infrastructure.AuthenticatedController.kt__
   3. __tech.garymyers.micronaut.security.authentication.infrastructure.AuthenticationJwtClaimSetGenerator.kt__
   4. __tech.garymyers.micronaut.security.authentication.infrastructure.RSAOAEPEncryptionConfiguration.kt__
   5. __tech.garymyers.micronaut.security.authentication.infrastructure.UserAuthenticationProvider.kt__
4. Updated/Created/Generated files in *src/main/resources*
   1. Updated __application.yml__ to add in Universal JWT security configuration
   2. Created __application-local.yml__ to house the local JWT security configurations for a "local" environment
   3. Generated __security/local-jwt.pem__ using `openssl genrsa -out security/local-jwt.pem 2048`
5. Created in *src/test/groovy*
   1. __tech.garymyers.micronaut.security.authentication.infrastructure.AuthenticatedControllerSpecification.groovy__
      1. Contains 2 tests one for testing a successful login and one that doesn't
6. Created in *src/test/resources*
   1. Created __application-local.yml__ to house the local JWT security configurations for "test" environment
   2. Created __logback-test.xml__ to house special test configuration for logging such as turning up Micronaut's HttpClient's logging to trace to view request/response bodies
Other miscellaneous changes done to build.gradle take out some dependencies I'm not using such as the Kotlin testing frameworks.
Also moved down the http client dependency to the test section to more closely mirror my actual project.

## To reproduce
1. `git clone https://github.com/developerSid/tech-garymyers-micronaut-security.git`
2. `./gradlew clean check`
   1. Both tests should pass
3. `git checkout broken`
4. `./gradlew clean check`
   1. "login and check auth success" test should fail
   
## Problems
Problem appears to be with Micronaut 1.3.1 if an __io.micronaut.security.authentication.Authentication__ is requested in 
a controller method and it is nullable (AKA kotlin `?`) then it is always null regardless if the user is logged in or not.
In Micronaut 1.3.0 this worked.

You can check the __tech.garymyers.micronaut.security.authentication.infrastructure.AuthenticatedController.authenticated__ method and how it works.
I'm totally up for the possibility I'm doing it wrong.

The only change I made between the 2 branches is the *micronautVersion* in the __gradle.properties__ file.