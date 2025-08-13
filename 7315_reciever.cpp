#include <iostream>
#include <bitset>
#include <cstring>
#include <netinet/in.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>

using namespace std;

char decodeHamming(string code) {
    int hammingCode[13];
    for (int i = 1; i <= 12; ++i)
        hammingCode[i] = code[i - 1] - '0';

    int p1 = hammingCode[1] ^ hammingCode[3] ^ hammingCode[5] ^ hammingCode[7] ^ hammingCode[9] ^ hammingCode[11];
    int p2 = hammingCode[2] ^ hammingCode[3] ^ hammingCode[6] ^ hammingCode[7] ^ hammingCode[10] ^ hammingCode[11];
    int p4 = hammingCode[4] ^ hammingCode[5] ^ hammingCode[6] ^ hammingCode[7] ^ hammingCode[12];
    int p8 = hammingCode[8] ^ hammingCode[9] ^ hammingCode[10] ^ hammingCode[11] ^ hammingCode[12];

    int errorPos = p1 * 1 + p2 * 2 + p4 * 4 + p8 * 8;

    if (errorPos != 0) {
        cout << "Error detected at position: " << errorPos << endl;
        hammingCode[errorPos] ^= 1;
    } else {
        cout << "No error detected.\n";
    }

    bitset<8> dataBits;
    int dataPos[] = {3, 5, 6, 7, 9, 10, 11, 12};
    for (int i = 0; i < 8; ++i)
        dataBits[7 - i] = hammingCode[dataPos[i]];

    return static_cast<char>(dataBits.to_ulong());
}

int main() {
    int serverSocket = socket(AF_INET, SOCK_STREAM, 0);
    sockaddr_in serverAddress{};
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(5000);
    inet_pton(AF_INET, "127.0.0.15", &serverAddress.sin_addr);

    if (bind(serverSocket, (struct sockaddr*)&serverAddress, sizeof(serverAddress)) < 0) {
        cerr << "Bind failed\n";
        return 1;
    }

    listen(serverSocket, 5);
    cout << "Receiver is listening on 127.0.0.15:5000\n";

    int clientSocket = accept(serverSocket, nullptr, nullptr);
    if (clientSocket < 0) {
        cerr << "Accept failed\n";
        return 1;
    }

    char buffer[1024] = {0};
    int bytesReceived;

    while ((bytesReceived = recv(clientSocket, buffer, sizeof(buffer) - 1, 0)) > 0) {
        buffer[bytesReceived] = '\0';
        string receivedCode = buffer;
        cout << "\nReceived Hamming code: " << receivedCode << endl;

        char decodedChar = decodeHamming(receivedCode);
        cout << "Decoded character: " << decodedChar << endl;
    }

    close(clientSocket);
    close(serverSocket);
    return 0;
}
