# Sample Spring Boot Project for Kubernetes Replicated StatefulSet Solution - Core Banking Transaction (K8S-TRN) Microservice

## Description
This spring based java project has been written to create a microservice which will expose the endpoints for basic core banking functionalities for example, deposit by cash, withdrawal by cash, fund transfer, dual bank ledger entry on transaction, enquiry and many more.

To run this microservice, the static data microservice must be running beasue it contains only trasaction part so the static data like bank and branch, customer and account must be provided by the said static microservie. 

The static data microservice named Customer Service Management (CSM) microservice. Link: https://github.com/siddhivinayak-sk/k8s-csm

To read full article visit URL: https://medium.com/@siddhivinayak.sk/kubernetes-and-replicated-statefulset-with-real-world-application-9bf21fca9eb


## Use Case
This microservice is part of Kubernetes Replicated StatefulSet Solution and it depicts typical core banking scenario where a microservice is created for monitery transactions. This microservice only contains core banking transactions with help of CSM microservice which contains the static data for processing of bank transactions.

Link to Kubernetes Replicated StatefulSet Solution: https://github.com/siddhivinayak-sk/kb-statefulset-sample-artifacts

## Technology Stack
- Java 8 or later
- Spring boot 2.x
- Maven as build tool
- Spotify docker plugin
- Docker
- Kubernetes 1.16 or later
- MySQL 5.7


## Build
Maven has been used as build tool for this project with default Spring boot build plugin.
Hence, we can use below maven code to build the runnable jar as microservice:

```
mvn clean package
```

It also has maven spotify plug in which is used for docker image creation from maven build tool by using below command:

```
mvn clean package docker:build -DpushImageTag
```
The above command will craete pacakge (jar file) after that create docker image, tag it as specifed into spotify configuration into pom.xml and push it to the registry.


## Deployment on Kubernetes - Stateless deployment
This project also contains Kubernetes declarative configuration (in form of yaml) for stateless deployment. Once, docker image created and pushed into registry, the below command can be used to create deployment into the the Kubernetes. (Pre-condidtion, there must be running Kubernetes cluster)

```
kubectl create -f trn-kb8-deployment.yaml
```

## Configuraiton
The maven project contains resource directory which contains applicaiton.yaml. This configuration file will be used to configure database and other properties.

Disclaimer: This code has been written for testing purpose, the execution and correctness depends on various envrironment parameters. It should not be used for any live usages.
