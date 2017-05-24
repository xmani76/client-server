# client-server

Simple client server example. 

To run the server, simply execute "java server.Main <port>" where <port> is your chosen port to listen on.

The server exposes an HTTP GET endpoint for invocations by clients. A sample invocation from command line would be
curl http://localhost:<configured port>/ping/<client ID> where <client ID> is a unique identifier for the client.

Once invoked, the server will spit out a running counter for each client ID every second. The client gets "disconnected" if it does not invoke the API again within 5 seconds. Upon disconnection, the server will stop spitting out the counter for that particular client.

Check the **server.ServerITest** junit class for integration tests.