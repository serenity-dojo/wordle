# Wordle Project

Welcome to the Wordle BDD project, a comprehensive demonstration of Behavior-Driven Development (BDD) and Test-Driven Development (TDD) methodologies in action. This project leverages the power of Serenity BDD, Cucumber, and JUnit 5 to build, specify, and document a microservice-style application.

## Aim

The goal of the Wordle project is to serve as an illustrative example of how modern software development principles, such as BDD and TDD, are used in conjunction with API-driven acceptance tests. Whether you're a seasoned developer or a beginner venturing into the world of microservices, this project has valuable insights to offer.

## Tech Stack

- **Java (JDK 17)**
- **Maven**
- **Serenity BDD:** An open-source library that helps you write higher-quality automated acceptance criteria.
- **Cucumber:** A tool that supports BDD, enabling you to define application behavior in plain meaningful English language.
- **JUnit 5:** The next generation testing framework for Java and JVM.

## How to Run the Tests

To execute the tests, you will need to run the following command:

```bash
mvn clean verify
```

You can also use the Maven wrapper if you don't have Maven installed:
```bash
./mvnw clean verify
```

## How to run the application

You can run the backend application locally as follows. Open a terminal window and run the following:
```bash
mvn spring-boot:run -pl wordle-backend
```

You should see the following console output:

```bash
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.1.3)

08:31:55.571 [main] INFO  c.s.w.m.WordleApplication - Starting WordleApplication using Java 21 with PID 42693 (/Users/john/Projects/SerenityDojoExpansion/wordle/wordle-backend/target/classes started by john in /Users/john/Projects/SerenityDojoExpansion/wordle/wordle-backend)
08:31:55.572 [main] INFO  c.s.w.m.WordleApplication - No active profile set, falling back to 1 default profile: "default"
08:31:57.491 [main] WARN  o.s.b.a.o.j.JpaBaseConfiguration$JpaWebConfiguration - spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
08:31:57.868 [main] INFO  c.s.w.m.WordleApplication - Started WordleApplication in 2.45 seconds (process running for 2.646)
 __        _____  ____  ____  _     _____   ____  _____ ______     _____ ____ _____      _    ____ ___ 
 \ \      / / _ \|  _ \|  _ \| |   | ____| / ___|| ____|  _ \ \   / /_ _/ ___| ____|    / \  |  _ \_ _|
  \ \ /\ / / | | | |_) | | | | |   |  _|   \___ \|  _| | |_) \ \ / / | | |   |  _|     / _ \ | |_) | | 
   \ V  V /| |_| |  _ <| |_| | |___| |___   ___) | |___|  _ < \ V /  | | |___| |___   / ___ \|  __/| | 
    \_/\_/  \___/|_| \_\____/|_____|_____| |____/|_____|_| \_\ \_/  |___\____|_____| /_/   \_\_|  |___|
```

Once running, you can access the application at http://localhost:9999/wordle/api/status and see the Swagger documentation at http://localhost:9999/wordle/swagger-ui/index.html

## Running the UI

Once the backend service layer is running, you can start the front-end application in a different terminal window with the following instructions:

```bash 
cd wordle-frontend
npm install
npm run dev
```

You can access the application on http://localhost:5173/

## Reports

Upon completion of the tests, Serenity reports are generated in the `wordle-backend/target/site/serenity` directory. You can view a sample report [here](https://serenity-dojo.github.io/wordle/).

## Learn More

If you're interested in learning more about Serenity BDD, visit the official [Serenity BDD website](https://serenity-bdd.github.io/).

## Training Courses

For more in-depth learning, check out the training courses available [here](https://expansion.serenity-dojo.com/). There are numerous resources available to you, including free introductory courses, to enhance your understanding of Serenity BDD.

## Contributing

We welcome contributions from everyone. Whether you're a seasoned developer or a beginner, your inputs are valuable to us. Feel free to open an issue or submit a pull request.

## License

This project is licensed under the terms of the MIT license.
