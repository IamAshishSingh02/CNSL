import socket
import os

# Client configuration
SERVER_IP = '127.0.0.15'
SERVER_PORT = 13000
BUFFER_SIZE = 1024

# Create TCP socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((SERVER_IP, SERVER_PORT))

# Step 1: Receive prompt for filename
prompt = client_socket.recv(BUFFER_SIZE).decode()
filename = input(prompt).strip()
client_socket.send(filename.encode())

# Step 2: Wait for server ready message
ready_msg = client_socket.recv(BUFFER_SIZE).decode()
print(ready_msg)

# Step 3: Verify file exists
if not os.path.exists(filename):
    print("❌ File not found. Please check the filename.")
    client_socket.close()
    exit()

# Step 4: Send file contents
with open(filename, 'rb') as f:
    while True:
        bytes_read = f.read(BUFFER_SIZE)
        if not bytes_read:
            break
        client_socket.sendall(bytes_read)

# Gracefully close the sending side
client_socket.shutdown(socket.SHUT_WR)

# Step 5: Receive acknowledgment
ack = client_socket.recv(BUFFER_SIZE).decode()
print(ack)

# Step 6: Close connection
client_socket.close()
