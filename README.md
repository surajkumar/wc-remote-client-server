# wc-remote-client-server

## Introduction
The wc-remote-client-server is a program I created in Java to demonstrate creating a [TeamViewer](https://www.teamviewer.com/) or [AnyDesk](https://anydesk.com/) like software is achievable. This by no means is a production ready piece of work and is not recommended to be used in relacement for commerically available remote desktop tools like the above mentioned. However, this was produced as an excerise to replicate that functionality in Java.

## System Requirements
* Java 10+ (var keyword)
* Gradle 6.3 or higher
* This application uses port 43594 however an available port must be available through your firewall

## Libraries
The following libraries are used as part of this application:

* netty-transport-native-epoll 4.1.65.Final
* vertx-web 4.1.2
* vertx-web-client 4.1.2

## Usage
Run the server first then the client on any machine. As this is a mock project, the hostname is harded coded within Client.java

## Contributing
Not required for this project. As such, please feel free to download the code and rewrite as you please. 
