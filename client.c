#include "server.h"

/*
 *Josh Melton
 *Riley Shelton
 */
int main(int argc, char** argv) {

    int server_socket;                 // descriptor of server socket
    struct sockaddr_in server_address; // for naming the server's listening socket
    int client_socket;
    char message[1024];
	  printf("Socket: Initializing...\n");
    // create unnamed network socket for server to listen on
    if ((server_socket = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
        perror("Error creating socket...\n");
        exit(EXIT_FAILURE);
    }
    printf("Socket: Created...\n");
    // name the socket (making sure the correct network byte ordering is observed)
    server_address.sin_family      = AF_INET;           // accept IP addresses
    server_address.sin_addr.s_addr = htonl(INADDR_ANY); // accept clients on any interface
    server_address.sin_port        = htons(PORT);       // port to listen on
    printf("Connecting...\n");
    if (connect(server_socket, (struct sockaddr *) &server_address, sizeof(server_address)) < 0)
    {
        perror("ERROR connecting...\n");
    }
	//Server loop send input to the server and recieves it back
	while(1){
		printf("Enter a message: ");
		scanf("%s", message);
		strcat(message, "\n");
		if (write(server_socket, message, strlen(message))< 0)
		{
			perror("ERROR writing to socket...\n");
			break;
		}
		//closes the client and the thread
		if (message == "q"){
			break;
		}else{
			puts("Server reply: ");
			puts(message);
		}
	}
    close(server_socket);
	return 0;
}
