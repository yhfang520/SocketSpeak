# SocketSpeak  👨‍💼💻💬

![](https://img.shields.io/badge/java-^17.0.8-red.svg) ![](https://img.shields.io/badge/JDK-17.0.0-red.svg) ![](https://img.shields.io/badge/gradle-8.2.0-blue.svg) ![](https://img.shields.io/badge/junit-^5.9.1-green.svg) ![](https://img.shields.io/badge/jacoco-^0.8.7-yellow.svg) <br>

Socket Speak is a chat room consisting of a server and multiple clients. The server is responsible for the client connections, accepting messages from one client, and sending the messages to all attached clients. Clients can open a socket to communicate with the server and the user may either send a message that is public
in the chat room, or that goes directly to a single, specified client. This program also includes the feature to generate and send a randomly generated sentence.   

### Get Start

- invoke `main` in `ChatServer`
```
[port]
5010
```
- invoke `main` in `ChatClient`
```
[port] [username]
5010 Anne
5010 Bob
5010 Tom
or 
[port] [username] [IP]
5010 Anne 127.0.0.1
```
##### tips:
- `IP` must be same with server IP address. It will be 'localhost' if not given.
- you can launch up to 10 ChatClient.



##### file structure:

```
  - src
    - main
      - java
        - Client
           - ChatClient (main method) 
        - Server
           - ChatSrever (main method)
  - insult_grammar.json
```
##### tip:
- `implementation files('lib/RandomInsult.jar')` make sure it is added to Gradle Dependencies.
- when run or build, make sure working directory is on /Assignment6, and json file is on working directory, so json file can be accessed.
----