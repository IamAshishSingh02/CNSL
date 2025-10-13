import socket

# Client configuration
SERVER_IP = '127.0.0.15'
SERVER_PORT = 14000
BUFFER_SIZE = 1024

# Create TCP socket
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect((SERVER_IP, SERVER_PORT))

# Step 1: Receive prompt for operation type
prompt = client_socket.recv(BUFFER_SIZE).decode()
op_type = input(prompt)
client_socket.send(op_type.encode())

# Step 2: Receive second prompt
prompt2 = client_socket.recv(BUFFER_SIZE).decode()
expr = input(prompt2)
client_socket.send(expr.encode())

# Step 3: Receive and display result
result = client_socket.recv(BUFFER_SIZE).decode()
print(result)

# Close connection
client_socket.close()
