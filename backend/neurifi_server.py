from flask import Flask
from flask_sock import Sock
from model import ChatbotModel

app = Flask(__name__)
sock = Sock(app)

# Load AI Model
chatbot = ChatbotModel()

@sock.route('/ws')  # WebSocket route
def websocket(ws):
    try:
        while True:
            data = ws.receive()
            if not data:
                break  # Exit loop if the client disconnects

            print(f"User: {data}")  # Log user input
            
            response = chatbot.generate_response(data)  # Get AI response
            print(f"Bot: {response}")  # Log chatbot response
            
            ws.send(response)  # Send response back to JavaFX
    except Exception as e:
        print(f"WebSocket Error: {e}")  # Catch exceptions
    finally:
        print("WebSocket connection closed")  # Ensure cleanup

if __name__ == '__main__':
    print("Starting WebSocket server on port 5000...")
    app.run(host='0.0.0.0', port=5000, debug=False, threaded=True)  # Keep for testing
