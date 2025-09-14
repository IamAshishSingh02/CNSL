// RollNo_15_Sender.cpp
#include <iostream>
#include <cstring>
#include <cstdlib>
#include <ctime>

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
    int sockfd, choice, frames;
    char buffer[1024];
    struct sockaddr_in servaddr;
    socklen_t len = sizeof(servaddr);

#ifdef _WIN32
    WSADATA wsa;
    WSAStartup(MAKEWORD(2,2), &wsa);
#endif

    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd < 0) { perror("Socket creation failed"); return 1; }

    servaddr.sin_family = AF_INET;
    servaddr.sin_port = htons(PORT);
    inet_pton(AF_INET, IP, &servaddr.sin_addr);

    srand(time(0));

    while (true) {
        std::cout << "\nSelect Protocol:\n1. Go-Back-N\n2. Selective Repeat\n3. Exit\nChoice: ";
        std::cin >> choice;

        if (choice == 3) {
            sendto(sockfd, "exit", 4, 0, (struct sockaddr*)&servaddr, len);
            std::cout << "Exiting sender...\n";
            break;
        }

        std::cout << "Enter number of frames to send: ";
        std::cin >> frames;

        for (int i = 0; i < frames; i++) {
            std::string frame = "Frame " + std::to_string(i);

            if (rand() % 4 == 0) {
                std::cout << "!! Frame " << i << " lost in channel !!\n";
                if (choice == 1) { // Go-Back-N
                    i--;
                    continue;
                } else { // Selective Repeat
                    sendto(sockfd, frame.c_str(), frame.size(), 0, (struct sockaddr*)&servaddr, len);
                    std::cout << "Resent " << frame << "\n";
                    continue;
                }
            }

            sendto(sockfd, frame.c_str(), frame.size(), 0, (struct sockaddr*)&servaddr, len);
            std::cout << "Sent: " << frame << "\n";

            memset(buffer, 0, sizeof(buffer));
            int n = recvfrom(sockfd, buffer, sizeof(buffer), 0, (struct sockaddr*)&servaddr, &len);
            if (n > 0) {
                buffer[n] = '\0';
                std::cout << "Receiver: " << buffer << "\n";
            }
        }
    }

#ifdef _WIN32
    closesocket(sockfd);
    WSACleanup();
#else
    close(sockfd);
#endif
    return 0;
}
