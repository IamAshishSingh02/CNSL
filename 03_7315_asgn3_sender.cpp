#include <iostream>
#include <bitset>
#include <cstring>
#include <netinet/in.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <cstdlib>
#include <ctime>

using namespace std;

string generateHamming(char ch) {
    bitset<8> dataBits(ch);
    int hammingCode[13] = {0};
    int dataPos[] = {3, 5, 6, 7, 9, 10, 11, 12};

    for (int i = 0; i < 8; ++i)
        hammingCode[dataPos[i]] = dataBits[7 - i];

    // Parity bits
    hammingCode[1] = hammingCode[3] ^ hammingCode[5] ^ hammingCode[7] ^ hammingCode[9] ^ hammingCode[11];
    hammingCode[2] = hammingCode[3] ^ hammingCode[6] ^ hammingCode[7] ^ hammingCode[10] ^ hammingCode[11];
    hammingCode[4] = hammingCode[5] ^ hammingCode[6] ^ hammingCode[7] ^ hammingCode[12];
    hammingCode[8] = hammingCode[9] ^ hammingCode[10] ^ hammingCode[11] ^ hammingCode[12];

    string result = "";
    for (int i = 1; i <= 12; ++i)
        result += (hammingCode[i] + '0');

    return result;
}

string introduceError(string code) {
    srand(time(0));
    int pos = rand() % 12; // 0 to 11
    code[pos] = (code[pos] == '0') ? '1' : '0';
    cout << "Error introduced at position: " << pos + 1 << endl;
    return code;
}

int main() {
    int clientSocket = socket(AF_INET, SOCK_STREAM, 0);
    sockaddr_in serverAddress{};
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(5000);
    inet_pton(AF_INET, "127.0.0.15", &serverAddress.sin_addr);

    if (connect(clientSocket, (struct sockaddr*)&serverAddress, sizeof(serverAddress)) < 0) {
        cerr << "Connection failed\n";
        return 1;
    }

    int choice;
    char ch;

    do {
        cout << "\n--- Sender Menu ---\n";
        cout << "1. Send without error\n";
        cout << "2. Send with 1-bit error\n";
        cout << "3. Exit\n";
        cout << "Enter choice: ";
        cin >> choice;

        if (choice == 1 || choice == 2) {
            cout << "Enter a character: ";
            cin >> ch;

            string hammingCode = generateHamming(ch);

            if (choice == 2)
                hammingCode = introduceError(hammingCode);

            cout << "Sending Hamming code: " << hammingCode << endl;
            send(clientSocket, hammingCode.c_str(), hammingCode.length(), 0);
        }
    } while (choice != 3);

    close(clientSocket);
    return 0;
}
