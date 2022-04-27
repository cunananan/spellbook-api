# Spellbook API (Team Epimetheus Project 2)

Spellbook is a REST API that allows users to browse, manage, and purchase from a collection of magic spells that they can use on their adventures in the Lands Between. Buyers can view and purchase available spells, while sellers can add, remove, or update spells in the listing. The application is designed to run off of a Kubernetes cluster with Grafan, Loki, and Prometheus services deployed for monitoring purposes.

## Technologies Used

* Java 8
* Spring Boot
* Kubernetes
* Jenkins

## Features

* Uses JWTs and password hashing for increased security
* Deploys with a Promtail sidecar for log aggregation on the cluster
* Exposes custom metrics to measure runtime of individual methods
* Includes a Jenkins pipeline for continuous delivery

Future Developments:
* Implement token expiration

## Getting Started
   
> TODO

## Usage

> Also TODO

## Contributors

* Aaron Cunanan
* Jazlyn Maxwell
* Craig Zamora