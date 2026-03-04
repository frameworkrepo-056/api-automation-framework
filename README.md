# API Automation Framework

Production-ready REST API test automation framework built with:
**RestAssured 5.3.2 · Cucumber 7 (BDD) · JUnit Platform Suite · Allure Reports · Java 21 · Maven**

---

## Prerequisites
- Java 21 (LTS) — `java -version`
- Maven 3.9+ — `mvn -version`

---

## Running Tests

### Run all tests (QA environment)
```bash
mvn clean test
```

### Run against a specific environment
```bash
mvn clean test -Denv=staging
```

### Run only smoke tests (@severity:critical)
```bash
mvn clean test -Dtest=SmokeTestRunner
```

### Run with a custom tag filter
```bash
mvn clean test -Dcucumber.filter.tags="@story:Fetch-All-Users"
```

### Generate and open Allure report
```bash
mvn allure:report
open target/site/allure-maven-plugin/index.html
```

---

## Project Structure