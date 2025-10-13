import socket

# Client configuration
SERVER_IP = '127.0.0.15'
SERVER_PORT = 12000
BUFFER_SIZE = 1024

# Create a TCP socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Connect to the server
client_socket.connect((SERVER_IP, SERVER_PORT))
print(f"Connected to server at {SERVER_IP}:{SERVER_PORT}")

# Receive message from server
server_msg = client_socket.recv(BUFFER_SIZE).decode()
print(f"Server says: {server_msg}")

# Send message to server
client_socket.send(b"Hello from client!")

# Close connection
client_socket.close()
