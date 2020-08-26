This is a demonstration project for Java Remote Methode Invocation (RMI). It is a simple, 
command line based, chat application.

# Starting the Application

The easiest way of starting the application is by using the supplied gradle-wrapper, you can compile and
run it directly or use your favorite IDE if you find that easier.\
You first have to start the server by executing "./gradlew run -Pserver --console plain" (Unix) 
or "gradlew.bat run -Pserver --console plain" (Windows). You can then start as many clients as you want on any machine 
by executing "./gradlew run --args='\[hostname of the server] \[local port]' --console plain" (Unix) or "gradlew.bat run --args
='\[hostname of the server] \[local port]' --console plain" (Windows)".
Please make sure to choose a different port when running multiple clients on the same machine.

# Usage / Commands

Both server and clients can be stopped by typing "exit" or "quit". You can type "list" to get a list of all currently
connected clients. Inputting any other message on a client will send it to all other clients.
If you wish to only send it to a specific client, put their id before the message separated by ": ".
Example: id-of-the-recipient: Hello World.
