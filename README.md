# **This is my repo for the test home assignment: test openweathermap api)**

This API Test Automation using Java libraries; RestAssured (for API Service), along with Maven as a build
tool and Testng as Test management (support data-driven testing).

# Table of Contents

* [Overview](#overview)
* [Project structure](#structure)
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

> Note: Test data file is formatted as below (named as test_suite_name.cvs):
> * Testcasename;data_input;response_status_code;expected_value(optional)

* Using Allure for tests report, outputs will be produced under ./target/site/allure-maven-plugin folder.

<a name="strucure"></a>
## 2. Project structure
```
apitestautomation/
┣ .circleci/     
┃ ┗ config.yml                                => Config file for Circleci
┣ demo/                 
┃ ┣ run_test.gif                              => clip demo for run a test
┃ ┣ circleci.gif                              => clip demo for run pipeline on CircleCI
┣ src/
┃ ┣ main/
┃ ┃ ┗ java/
┃ ┃   ┗ com/
┃ ┃ ┃   ┗ shiron/
┃ ┃ ┃ ┃   ┗ api/
┃ ┃ ┃ ┃ ┃   ┣ config/                         => Configuration manage
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ Configuration.java            
┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ ConfigurationManager.java
┃ ┃ ┃ ┃ ┃   ┗ specs/
┃ ┃ ┃ ┃ ┃ ┃   ┗ InitialStateSpecs.java        => RequestSpecification class
┃ ┗ test/
┃   ┣ java/
┃ ┃ ┃ ┗ com/
┃ ┃ ┃   ┣ baseclass/
┃ ┃ ┃ ┃ ┃ ┗ TestBase.java                     => Test base class
┃ ┃ ┃   ┗ testcase/
┃ ┃ ┃ ┃   ┗ weather/
┃ ┃ ┃ ┃ ┃   ┗ SearchWeather.java              => Testsuite SearchWeather
┃   ┗ resources/
┃ ┃   ┣ schemas/                              => store expected schema for Contract testting
┃ ┃ ┃ ┃ ┗ weather/
┃ ┃ ┃ ┃   ┣ weather_response_schema.json      
┃ ┃ ┃ ┃   ┗ weather_response_schema.xsd
┃ ┃   ┣ testdata/                             => store testdata files
┃ ┃ ┃ ┃ ┗ weather/
┃ ┃ ┃ ┃   ┗ SearchByCityNameData.csv
┃ ┃   ┣ allure.properties                     => Allure configuration file
┃ ┃   ┣ api.properties                        => API configuration file
┃ ┃   ┗ log4j.properties                      => log4j configuration file
┣ target/
┃ ┣ allure-results/
┃ ┣ site/
┃ ┃ ┗ allure-maven-plugin/
┃ ┣ surefire-reports/
┣ pom.xml                                     => Maven configuration file
┣ README.md
┗ testng.xml                                  => Testng configuration file
```

<a name="howtosetup"></a>
## 3. How to setup

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

## 4. How to run tests and generate reports

Run CLI: "<code>mvn -Denv=prod test </code>" to build and execute tests

```batch
 mvn -Denv=prod test
 
 env is prod,staging,test (match with value that define in test/resources/api.properties)
```
or run a specific testcase :
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

## 5. Where to find reports

Testng reports:

* XML file: ./target/surefire-reports/testng-result.xml
* HTML file: ./target/surefire-reports/index.html

Allure reports:

* ./target/site/allure-maven-plugin/*
* or local server will run and public to show on local browser (mvn allure:serve).

---
<a name="jenkins"></a>

## 6. CircleCI (CI/CD) Integration setup guideline
CircleCI configuration file is located at ./circleci/config.yml

## 7. Jenkins (CI/CD) Integration setup guideline
In your new Jenkins Job:

7.1) Under Git Integration: pull source code from github:

```git
git clone https://github.com/shiron-truong/apitestautomation.git
```

7.2) Add step: to run maven (build and run goal as 'test')

Two options:

* Run with the 'default' in testng.xml
     ```batch
     mvn -Denv=prod test
     ```  
* Run with the test parameters:
  ```
  mvn -Dtest=SearchWeather#checkXMLSchema -Denv=prod test
  ```

7.3). If needed, Allure reports can be used for further integrations (Jenkins build itself).

Reference: [Allure-report-integration-with-jenkins](https://www.qaautomation.co.in/2018/12/allure-report-integration-with-jenkins.html)

---
<a name="outputexamples"></a>

## 8. Examples of Test Execution on loca and on cloud(CircleCI)

![Image](./demo/run_test.gif)

![Image](./demo/circleci.gif)

--- 
