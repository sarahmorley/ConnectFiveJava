# Connect Five #
This is a version of the popular Connect Four game. 

The project uses a client (players) and server (main game) architecture. 

It is a two player game. Whereby two clients connect to the server and play against each other.

Joining a game, playing a turn and checking when it is ones turn is done using http requests from the client to the server. 

## Technologies used
*  Java 8
*  Spring Boot
*  Maven


## Build and Run


### Build and Run the Game Server
```
mvn clean package
```
The built java jar can then be found in the target folder. CD to target and run it using the following command

```
java -jar connectFive-0.0.1-SNAPSHOT.jar
```

### Build and run client

Now that the game server is up and running clients can connect to it. 
```
mvn clean package
```
The built java jar can then be found in the target folder. CD to target and run it using the following command

```
java -jar Connect5Client-0.0.1-SNAPSHOT.jar
```



## Usage

Run two instances of the client and follow the instructions in the command window.


## Testing

### Unit testing
Maven Surefire can be used to run the units tests with the following command
```
mvn clean test
```





