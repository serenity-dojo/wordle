# Adding Jacoco code coverage to a Spring microservice with Serenity tests

Test Coverage is a commonly requested quality metric in large organisations. And for projects using Sonar to track code quality, it is the responsibily of the projects themselves to produce the code coverage data.

Sonar, widely recognized for its project SonarQube, is a platform dedicated to continuous inspection of code quality. SonarQube provides developers with a clear visualization of code quality metrics and potential issues, covering aspects such as bugs, vulnerabilities, code smells, and even technical debt.

[Jacoco](https://www.eclemma.org/jacoco/) is the defacto standard way to produce code coverage for Java projects. However adding complete test coverage with Jacoco to a Spring boot application can be challenging. In particular, jacoco by default will only provide coverage for unit tests, not for integration tests.

## Maven lifecycle test phases
In a Maven project, tests are run in two separate phases. Unit tests, run during the are typically for fast, in-memory tests (i.e. tests that run in milliseconds). They are run during the `test` phase, and are configured using the `maven-surefire-plugin`.

Integration tests are tests that are typically slower to run - for example, a Spring integration test may need to start up some or all of the services of the application before running. These tests are typically run in the `integration-test` phase, and are configured using the `maven-failsafe-plugin`.

Two major advantages of having integration tests in the `integration-test` phase are that they allow much faster feedback if there are failing unit tests, and that they allow maven to generate test reports (such as the Serenity reports) should any of the integration tests fail. Using this distinction also allows us to prepare and set up resources such as databases or the Spring application automatically before running the integration tests.

There are many ways to distinguish unit tests from integration tests in Maven. The default plugin configurations will consider tests that end in `*Test` as unit tests, and tests that end in `*IT` as integration tests.

Another common convention is to use specific packages for integration tests. For example the following configuration reserves the `integrationtests` and `acceptancetests` packages for integration tests to be run during the `mvn verify` phase.

For example, the following surefire and failsafe configuration will reserve the `integrationtests` and `acceptancetests` packages for integration tests, and only run tests in these packages during the `integration-test` phase:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <excludes>
            <exclude>**/integrationtests/**</exclude>
            <exclude>**/acceptancetests/**</exclude>
        </excludes>
    </configuration>
</plugin>
<plugin>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <includes>
            <include>**/integrationtests/api/*.java</include>
            <include>**/acceptancetests/*.java</include>
        </includes>
        <properties>
            <configurationParameters>
                junit.jupiter.extensions.autodetection.enabled = true
            </configurationParameters>
        </properties>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Now let's see how we can add jacoco test coverage to our Maven SpringBoot project.

## Configuring unit and integration code coverage

### Add unit-level test coverage:

The first step is to add jacoco code coverage for your unit tests. The default jacoco configuration looks something like this:
```xml
<!-- Code coverage reporting -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>prepare-package</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

_However_ this will only provide reporting for your unit tests (those executed during the `test` phase, not your integration tests).

To cater for both Unit and Integration tests, we need to tell jacoco to store the unit test coverage data in a specific file, and to generate the XML coverage report in a specific folder. We can do this as follows.

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <!-- Unit test coverage -->
        <execution>
            <id>prepare-unit-tests</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
            <configuration>
                <destFile>${project.build.directory}/jacoco-output/jacoco-unit-tests.exec</destFile>
                <propertyName>surefire.jacoco.args</propertyName>
            </configuration>
        </execution>
        <!-- Generate unit test coverage data -->
        <execution>
            <id>after-unit-test-execution</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
            <configuration>
                <dataFile>${project.build.directory}/jacoco-output/jacoco-unit-tests.exec</dataFile>
                <outputDirectory>${project.reporting.outputDirectory}/jacoco-unit-test-coverage-report</outputDirectory>
            </configuration>
        </execution>
    </executions>
</plugin>
```

We also use the `propertyName` element to define the `surefire.jacoco.args` property. We need to include this property in the `maven-surefire-plugin` configuration, so that the coverage data is reported in the right place:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <excludes>
            <exclude>**/integrationtests/**</exclude>
            <exclude>**/acceptancetests/**</exclude>
        </excludes>
        <argLine>${surefire.jacoco.args}</argLine>
    </configuration>
</plugin>
```

### Add Integration Test Coverage

Next, we need to configure jacoco to produce separate data for the integration tests.
Add the following `execution` sections to the `jacoco` configuration:

```xml

<!-- Integration test coverage -->
<execution>
    <id>before-integration-test-execution</id>
    <phase>pre-integration-test</phase>
    <goals>
        <goal>prepare-agent</goal>
    </goals>
    <configuration>
        <destFile>${project.build.directory}/jacoco-output/jacoco-integration-tests.exec</destFile>
        <propertyName>failsafe.jacoco.args</propertyName>
    </configuration>
</execution>

<execution>
    <id>after-integration-test-execution</id>
    <phase>post-integration-test</phase>
    <goals>
        <goal>report</goal>
    </goals>
    <configuration>
        <dataFile>${project.build.directory}/jacoco-output/jacoco-integration-tests.exec</dataFile>
        <outputDirectory>
            ${project.reporting.outputDirectory}/jacoco-integration-test-coverage-report
        </outputDirectory>
    </configuration>
</execution>
```

Test coverage data for the integration tests will go in the `jacoco-integration-test-coverage-report` folder.

Once again we use the `<propertyName>` property to hold the jacoco configuration parameters for these tests, so we need to update our `maven-failsafe-plugin` accordingly:

```xml
<plugin>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <includes>
            <include>**/integrationtests/api/*.java</include>
            <include>**/acceptancetests/*.java</include>
        </includes>
        <argLine>${failsafe.jacoco.args}</argLine>
        <properties>
            <configurationParameters>
                junit.jupiter.extensions.autodetection.enabled = true
            </configurationParameters>
        </properties>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```    

### Merge Code Coverage Data

The final step is to merge the coverage data from both types of tests and produce a merged set of coverage data to produce local reports and to provide to Sonar. We do this in two stages: first we merge the binary coverage data into a single file, then we use this file to produce the final coverage data in the `target/site/jacoco` folder:

```xml
<!-- Merge unit and integration test coverage -->
<execution>
    <id>merge-unit-and-integration</id>
    <phase>post-integration-test</phase>
    <goals>
        <goal>merge</goal>
    </goals>
    <configuration>
        <fileSets>
            <fileSet>
                <directory>${project.build.directory}/jacoco-output/</directory>
                <includes>
                    <include>*.exec</include>
                </includes>
            </fileSet>
        </fileSets>
        <destFile>${project.build.directory}/jacoco-output/merged.exec</destFile>
    </configuration>
</execution>

<!-- Create the merged unit and integration test coverage report -->
<execution>
    <id>create-merged-report</id>
    <phase>post-integration-test</phase>
    <goals>
        <goal>report</goal>
    </goals>
    <configuration>
        <dataFile>${project.build.directory}/jacoco-output/merged.exec</dataFile>
        <outputDirectory>${project.reporting.outputDirectory}/jacoco</outputDirectory>
    </configuration>
</execution>
```

## Ensure your Spring tests play nicely with jacoco

There are a few ways to run integration tests with Spring. Each has pros and cons. One simple way is to start the application using the `spring-boot-maven-plugin`, and then test your microservice using RestAssured:

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>3.1.3</version>
    <executions>
        <execution>
            <goals>
                <goal>repackage</goal>
            </goals>
        </execution>
        <execution>
            <id>pre-integration-test</id>
            <goals>
                <goal>start</goal>
            </goals>
        </execution>
        <execution>
            <id>post-integration-test</id>
            <goals>
                <goal>stop</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

However, if you test against a running application in this way, it is very hard to collect test coverage metrics. So if you need to report test coverage on your integration tests, you will need a different approach.

You can also test your application using the `@AutoConfigureMockMvc` annotation and the `MockMvc` class. In this case, you need to use either the native MockMvc verification methods, or the `RestAssuredMockMvc` class to perform your RestAssured tests.

```java
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Wordle Status Check")
@SpringBootTest(classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
@AutoConfigureMockMvc
class ApplicationStatusCheck {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("We can check the status of the Wordle service by sending a GET to /api/service")
    public void checkStatus() {
        RestAssuredMockMvc.get("/api/status")
                .then()
                .statusCode(200);
    }
}
```

This approach is quite flexible, and makes it easy to inject mocked components for different tests. It does have the disadvantage of having more limited Serenity BDD integration - this test will produce correct Serenity BDD reports, but it will not document the REST queries themselves, which can be a limitation if you are using the Serenity reports as living documentation for your microservice.

If you want to benefit from the full Serenity BDD reporting (in particular, Serenity BDD documents the REST API calls performed during the tests), the best approach is to use the `@SpringBootTest` annotation with the `webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT` option. Then you can use the `@LocalServerPort` annotation to grab the port, and use RestAssured and Serenity BDD to test your microservices as in the following example:

```java
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Wordle Status Check")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.serenitydojo.wordle.microservices.WordleApplication.class)
class ApplicationStatusCheck {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI= "http://localhost:" + port;
    }

    @Test
    @DisplayName("We can check the status of the service by sending a GET to /api/status")
    public void checkStatus() {
        SerenityRest.get("/api/status")
                .then()
                .statusCode(200);
    }
}
```

This approach allows us to both get full Serenity BDD reporting, and also allows us to mock out components selectivly. Here is an example of how to do this:

```java
@ExtendWith(SerenityJUnit5Extension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SomeIntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private SomeService someService;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
        Mockito.when(someService.someMethod()).thenReturn(someMockedValue);
    }

    @Test
    public void someTest() {
        ...
    }
}
```

## Sonar Integration

Finally, if you are using Sonar, make sure you add the following property to your project properties:

```
<sonar.coverage.jacoco.xmlReportPaths>${basedir}/target/site/jacoco/jacoco.xml</sonar.coverage.jacoco.xmlReportPaths>
```

## Conclusion
Integrating test coverage into a Spring microservice project provides invaluable insights into the quality and reliability of the codebase. By leveraging tools like Jacoco in conjunction with Maven, we can systematically gather coverage data from both unit and integration tests. This process, though intricate, ensures a comprehensive view of the project's health. However, it's essential to select the right Spring testing approach to maximize the benefits of tools like Serenity for reporting and documentation purposes. By following the methods and configurations outlined in this article, teams can achieve a robust testing framework that not only validates the functionality but also offers a detailed view of coverage and documentation.
