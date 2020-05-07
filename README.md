# distributed-shared-white-board

## Introduction
Shared whiteboards allow multiple users to draw simultaneously on a canvas. There are multiple examples found on the Internet that support a range of features such as freehand drawing with the mouse, drawing lines and shapes such as circles and squares that can be moved and resized, and inserting text.

## How to run

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

### Whiteboard
- join in means the peer name will appear in the user list
    - An online peer list should be maintained and displayed
- All the peers will see the identical image of the whiteboard, as well as 
    - have the privilege of doing all the operations.

### Server
- Users must provide a username when joining the whiteboard. 
    - There should be a way of uniquely identifying users, either by 
        - enforcing unique usernames 
        - or automatically generating a unique identifier and associating it with each username.
