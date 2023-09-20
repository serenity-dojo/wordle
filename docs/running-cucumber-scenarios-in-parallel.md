# How to run tests in parallel with Cucumber and Serenity BDD

Running your BDD scenarios can give significant improvements in performance and accelerated feedback. But for a long time, it has been challenging to run Cucumber scenarios in parallel. This tutorial explains how to run Cucumber features and scenarios in parallel with Serenity BDD and Maven.

## Setting up your dependencies
You will need JUnit 5 and Cucumber 7 for parallel execution support, so you will need the latest versions of the corresponding dependencies. Here is an example of the dependencies you will need:

```xml
        <!-- JUNIT 5 DEPENDENCIES-->

        <!-- Standard JUnit 5 Dependencies -->
        <dependency>
            <groupId>org.junit.platform</groupId>
            <artifactId>junit-platform-suite-api</artifactId>
            <version>1.10.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.platform</groupId>
            <artifactId>junit-platform-suite</artifactId>
            <version>1.10.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
        <!-- Cucumber JUnit Integration -->
        <dependency>
            <groupId>io.cucumber</groupId>
            <artifactId>cucumber-junit-platform-engine</artifactId>
            <version>7.13.0</version>
            <scope>test</scope>
        </dependency>
```

## Setting up your test runner

Next, make sure your Cucumber runner class uses the JUnit 5 runners. You only need one runner class, though you might have more than one as a way to better organise your tests:

```java
import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class CucumberTestSuite {}
```

## Configure your JUnit platform properties

Create a file called `junit-platform.properties` in `src/test/resources`. This will contain the configuration options for your parallel execution. To enable parallel execution, set the following property to true:

```properties
cucumber.execution.parallel.enabled=true
```

To enable Serenity BDD reporting (and to be able to run your Serenity BDD scenarios in parallel), you will need to use the `io.cucumber.core.plugin.SerenityReporterParallel` plugin. You can configure this plugin with the following code:

```properties
cucumber.plugin=io.cucumber.core.plugin.SerenityReporterParallel
```

You can also fine-tune how you would like your features and scenarios to run in parallel. You can find all the options [here](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-junit-platform-engine), but the three most useful configurations are variations of the following:

### Run each feature file in parallel

You can run each feature file in a separate thread, but still run each scenario within a feature file sequentially, with the following configuration. This is less optimal in terms of performance, but can be useful for test suites where the individual scenarios are not thread-safe (yet).

```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.strategy=dynamic
cucumber.execution.execution-mode.feature=same_thread
cucumber.plugin=io.cucumber.core.plugin.SerenityReporterParallel,pretty
```

## Run each scenario in a parallel

You can also run each scenario in parallel. The `dynamic` strategy option tells Cucumber to decide the most appropriate number of parallel threads based on the number of available CPUs.

```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.strategy=dynamic
cucumber.plugin=io.cucumber.core.plugin.SerenityReporterParallel,pretty
```

## Run each scenario in a parallel, but limit the number of threads

Sometimes you might want to limit the number of threads explicitly. This can be useful if you are running tests on a Selenium Grid and don't want to overload the Grid nodes, for example. In the following example, Cucumber will limit the total number of threads to 5. (Note that while the `fixed.max-pool-size` propertie effectively limits the maximum number of concurrent threads, Cucumber does not guarantee that the number of concurrently executing scenarios will not exceed this. See [junit5/#3108](https://github.com/junit-team/junit5/issues/3108) for details.)

```properties
cucumber.execution.parallel.enabled=true
cucumber.execution.parallel.config.strategy=fixed
cucumber.execution.parallel.config.fixed.parallelism=5
cucumber.execution.parallel.config.fixed.max-pool-size=5
cucumber.plugin=io.cucumber.core.plugin.SerenityReporterParallel,pretty
```

## Run individal tests in isolation

Sometimes certain scenarios need to be run in isolation (i.e. while no other scenarios are running). You can do this by using the `org.junit.platform.engine.support.hierarchical.ExclusiveResource.GLOBAL_KEY` property. To do this, you can add the `@isolated` tag to the scenarios that need to be run in isolation, like this:

```gherkin
Feature: Isolated scenarios

   @isolated
   Scenario: isolated example
      Given this scenario runs isolated
      When it is executed
      Then it will not be executed concurrently with the second or third example

   Scenario: second example
      When it is executed
      Then it will not be executed concurrently with the isolated example
      And it will be executed concurrently with the third example

   Scenario: third example
      When it is executed
      Then it will not be executed concurrently with the isolated example
      And it will be executed concurrently with the second example
```

Next, set the `cucumber.execution.exclusive-resources.isolated.read-write` property as follows:
```
cucumber.execution.exclusive-resources.isolated.read-write=org.junit.platform.engine.support.hierarchical.ExclusiveResource.GLOBAL_KEY
```

