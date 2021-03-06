# distributed-shared-white-board

## Introduction
Shared whiteboards allow multiple users to draw simultaneously on a canvas. There are multiple examples found on the Internet that support a range of features such as freehand drawing with the mouse, drawing lines and shapes such as circles and squares that can be moved and resized, and inserting text.

## result
- 25/25
- ![](./docs/result.png)

## How to run

    The first user creates a whiteboard and becomes the whiteboard’s manager
        - java CreateWhiteBoard <serverIPAddress> <serverPort> username
    Other users can ask to join the whiteboard application any time by inputting server’s IP address and port number
        - java JoinWhiteBoard <serverIPAddress> <serverPort> username

### start rmiregistry
1. ```cd output/production/distributed-shared-white-board```
2. ```start rmiregistry```

### start app
1. ```cd submit```
2. server   
    
    ```java -Djava.rmi.server.codebase=file:"E:\backup\code\java\distributed-shared-white-board\output\production\distributed-shared-white-board" -jar Server.jar 127.0.0.1 8001```
3. create whiteboard 

    ```java -Djava.rmi.server.codebase=file:"E:\backup\code\java\distributed-shared-white-board\output\production\distributed-shared-white-board" -jar CreateWhiteboard.jar 127.0.0.1 8001 manager-xuliny```
4. join whiteboard 

    ```java -Djava.rmi.server.codebase=file:"E:\backup\code\java\distributed-shared-white-board\output\production\distributed-shared-white-board" -jar JoinWhiteboard.jar 127.0.0.1 8001 user-peter```

## todo
### Client
- connect and join in by getting approval from the manager
- choose quit
- drawing together in real time, without appreciable delays between making and observing edits.

### Manager
- choose whether a peer can join in
    - A notification will be delivered to the manager if any peer wants to join. The peer can join in only after the manager approves
    - A dialog showing “someone wants to share your whiteboard”.
- kick out a certain peer/user
- allowed to 
    - create a new whiteboard, 
    - open a previously saved one, 
    - save the current one, and 
    - close the application.
        - close the application, and all peers get notified
- A “File” menu with 
    - new, 
    - open, 
    - save, 
    - saveAs and 
    - close 

    should be provided (only the manager can control this)

### Whiteboard
- join in means the peer name will appear in the user list
    - An online peer list should be maintained and displayed
- All the peers will see the identical image of the whiteboard, as well as 
    - have the privilege of doing all the operations.
- GUI
    - Shapes: 
        - line, 
        - circle, and 
        - rectangle.
    - Text inputting must be implemented 
        - allow user to type text anywhere inside the white board.

### Server
- Users must provide a username when joining the whiteboard. 
    - There should be a way of uniquely identifying users, either by 
        - enforcing unique usernames 
        - or automatically generating a unique identifier and associating it with each username.
