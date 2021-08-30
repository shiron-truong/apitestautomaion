# **API Automation Testing (For my home assignment: test openweathermap api)**

API Test Automation in Java language using Java libraries; RestAssured (for API Service), along with Maven as a build
tool and Testng as Test management (support data-driven testing).

# Table of Contents

* [Overview](#overview)
* [How to setup](#howtosetup)
* [How to run tests and generate reports](#howtoruntests)
* [Where to find Reports](#reports)
* [Examples of Test Execution and HTML report](#outputexamples)
* [Jenkins (CI/CD) Integration setup guideline](#jenkins)

<a name="overview"></a>

## 1. Overview

* This API test framework uses Java as a main language with 'RestAssured' libraries.

* Test will run by testng according to testng.xml file.
* Support data-driven testing (Testng with @DataProvider), test data file which are stored under
  ./src/test/resources/testdata folder.

> Note: Test data file format .csv and structure as below:
> * Testcasename;data_input;method;response_status_code;expected_value

* Using Allure for tests report, outputs will be produced under ./target/site/allure-maven-plugin folder.

<a name="howtosetup"></a>

## 2. How to setup

___

Pull (clone) source code from Git as below command:

```git
git clone https://github.com/shiron-truong/apitestautomation.git
```

Then tests can be run as mentioned in the next step.

> Note: Git client and Maven are required to setup and run
> * [Git Installation](https://www.atlassian.com/git/tutorials/install-git)
> * [Apache Maven Installation](http://maven.apache.org/install.html/)
--- 

<a name="howtoruntests"></a>

## 3. How to run tests and generate reports

Run CLI: "<code>mvn test </code>" to build and execute tests

```batch
 mvn clean -Denv=prod test
 
 env = prod,staging,test
```
or run a specific testcase:
```batch
mvn -Dtest=SearchWeather#checkCityName -Denv=prod test
```
This command will run only test case checkCityName in SearchWeather suite.

Once finished, there will be reports in ./target/surefire-reports/* folder.

To generate Allure report, run below command:

```batch
mvn allure:report 
or
mvn allure:serve
```

Once finished, there will be reports in ./target/site/allure-maven-plugin/* folder.

---

<a name="reports"></a>

## 4. Where to find reports

Testng reports:

* XML file: ./target/surefire-reports/testng-result.xml
* HTML file: ./target/surefire-reports/index.html

Allure reports:

* ./target/site/allure-maven-plugin/*
* or local server will run and public to show on local browser (mvn allure:serve).

---
<a name="jenkins"></a>

## 5. Jenkins (CI/CD) Integration setup guideline

In your new Jenkins Job:

5.1) Under Git Integration: pull source code from github:

```git
git clone https://github.com/shiron-truong/apitestautomation.git
```

5.2) Add step: to run maven (build and run goal as 'test')

Two options:

* Run with the 'default' in testng.xml
     ```batch
     mvn -Denv=prod test
     ```  


5.3). If needed, Allure reports can be used for further integrations (Jenkins build itself).

Reference: [Allure-report-integration-with-jenkins](https://www.qaautomation.co.in/2018/12/allure-report-integration-with-jenkins.html)

---
<a name="outputexamples"></a>

## 6. Examples of Test Execution and HTML report

![Image](./screenshots/testExecution.JPG)

![Image](./screenshots/htmlReport.JPG)

--- 