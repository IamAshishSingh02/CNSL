// RollNo_15_Receiver.cpp
#include <iostream>
#include <cstring>

#ifdef _WIN32
#include <winsock2.h>
#include <ws2tcpip.h>
#pragma comment(lib, "ws2_32.lib")
#else
#include <arpa/inet.h>
#include <unistd.h>
#endif

#define PORT 8080
#define IP "127.0.0.15"

int main() {
    int sockfd;
    char buffer[1024];
    struct sockaddr_in servaddr, cliaddr;
    socklen_t len = sizeof(cliaddr);

#ifdef _WIN32
    WSADATA wsa;
    WSAStartup(MAKEWORD(2,2), &wsa);
#endif

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) { perror("Socket creation failed"); return 1; }

    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(PORT);
    inet_pton(AF_INET, IP, &servaddr.sin_addr);

    if (bind(sockfd, (struct sockaddr*)&servaddr, sizeof(servaddr)) < 0) {
        perror("Bind failed");
        return 1;
    }

    std::cout << "Receiver started at " << IP << ":" << PORT << "\n";

    while (true) {
        memset(buffer, 0, sizeof(buffer));
        int n = recvfrom(sockfd, buffer, sizeof(buffer), 0, (struct sockaddr*)&cliaddr, &len);
        if (n <= 0) continue;
        buffer[n] = '\0';

        if (strcmp(buffer, "exit") == 0) {
            std::cout << "Receiver exiting...\n";
            break;
        }

        std::cout << "Received Frame: " << buffer << "\n";
        std::string ack = "ACK " + std::string(buffer);
        sendto(sockfd, ack.c_str(), ack.size(), 0, (struct sockaddr*)&cliaddr, len);
    }

#ifdef _WIN32
    closesocket(sockfd);
    WSACleanup();
#else
    close(sockfd);
#endif
    return 0;
}
