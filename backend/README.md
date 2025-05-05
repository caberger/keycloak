# Demo Backend



## Purpose

This is a backend that demonstrates the basics of an Elipse Microprofile REST Server that uses a plain java object oriented data store as a database. Its purpose is to give an introduction to using quarkus. 

To give a smooth start to readers who com from other fields than quarkus or Jakarta EE we made some decisions for didactical reasons:
- the application has a *main* entypoint. This is not strictly required, but is familiar to users that are only used to console applications.

- This example also shows how to correctly respond to HTTP - POST requests, i.e. with a 201 created Response code and a "Location" header, that contains the URI for the new created resource. See "Testing" below and the UserResource.java file.



