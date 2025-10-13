import socket

# Server configuration
SERVER_IP = '127.0.0.15'
SERVER_PORT = 12000
BUFFER_SIZE = 1024

# Create a TCP socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind socket to IP and port
server_socket.bind((SERVER_IP, SERVER_PORT))

# Listen for incoming connections
server_socket.listen(1)
print(f"Hello Server listening on {SERVER_IP}:{SERVER_PORT}...")

# Accept a client connection
conn, addr = server_socket.accept()
print(f"Connected to: {addr}")

# Exchange hello messages
conn.send(b"Hello from server!")
client_msg = conn.recv(BUFFER_SIZE).decode()
print(f"Client says: {client_msg}")

# Close connection
conn.close()
server_socket.close()
