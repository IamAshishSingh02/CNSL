import socket

# Server configuration
SERVER_IP = '127.0.0.15'
SERVER_PORT = 13000
BUFFER_SIZE = 1024

# Create TCP socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((SERVER_IP, SERVER_PORT))
server_socket.listen(1)
print(f"File Server listening on {SERVER_IP}:{SERVER_PORT}...")

# Accept client connection
conn, addr = server_socket.accept()
print(f"Connected to: {addr}")

try:
    # Step 1: Ask for filename
    conn.send(b"Send filename: ")
    filename = conn.recv(BUFFER_SIZE).decode().strip()

    # Step 2: Notify client server is ready
    conn.send(b"Ready to receive file...")

    # Step 3: Receive and save file
    with open(f"server_{filename}", 'wb') as f:
        while True:
            data = conn.recv(BUFFER_SIZE)
            if not data:
                break
            f.write(data)

    print(f"✅ Received file: server_{filename}")

    # Step 4: Send acknowledgment
    conn.send(b"File received successfully.")

except ConnectionResetError:
    print("⚠️ Connection closed by client unexpectedly.")
except Exception as e:
    print(f"❌ Error: {e}")

# Step 5: Close connection
conn.close()
server_socket.close()
