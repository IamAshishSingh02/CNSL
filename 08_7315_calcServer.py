import socket
import math

# Server configuration
SERVER_IP = '127.0.0.15'
SERVER_PORT = 14000
BUFFER_SIZE = 1024

# Create TCP socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind((SERVER_IP, SERVER_PORT))
server_socket.listen(1)
print(f"Calculator Server listening on {SERVER_IP}:{SERVER_PORT}...")

# Accept client connection
conn, addr = server_socket.accept()
print(f"Connected to: {addr}")

# Ask for operation type
conn.send(b"Choose operation type (arithmetic/trigonometric): ")
op_type = conn.recv(BUFFER_SIZE).decode().strip().lower()

# --- Arithmetic Operations ---
if op_type == 'arithmetic':
    conn.send(b"Enter expression (e.g., 5 + 3): ")
    expr = conn.recv(BUFFER_SIZE).decode()

    try:
        # Safely evaluate basic arithmetic only
        result = eval(expr)
        conn.send(f"Result: {result}".encode())
    except Exception as e:
        conn.send(f"Error: {e}".encode())

# --- Trigonometric Operations ---
elif op_type == 'trigonometric':
    conn.send(b"Enter function and value (e.g., sin 30): ")
    func_val = conn.recv(BUFFER_SIZE).decode().split()

    if len(func_val) != 2:
        conn.send(b"Invalid input")
    else:
        func, val = func_val[0].lower(), float(func_val[1])
        val_rad = math.radians(val)
        try:
            if func == 'sin':
                result = math.sin(val_rad)
            elif func == 'cos':
                result = math.cos(val_rad)
            elif func == 'tan':
                result = math.tan(val_rad)
            else:
                conn.send(b"Unsupported function")
                conn.close()
                server_socket.close()
                exit()
            conn.send(f"Result: {result}".encode())
        except Exception as e:
            conn.send(f"Error: {e}".encode())

# --- Invalid Operation Type ---
else:
    conn.send(b"Invalid operation type.")

# Close connection
conn.close()
server_socket.close()
