var socket;

// Function to connect to the server
function connectToServer() {
    // Create a WebSocket connection
    socket = new WebSocket("ws://localhost:3360/Newsroom2Server");

    // Function to handle connection opened event
    socket.onopen = function() {
        var connectionStatus = document.getElementById("connectionStatus");
        connectionStatus.innerText = "Connected to the server. You can start receiving and sending messages.";
        
        // Show the message input and send button
        var messageContainer = document.getElementById("messageContainer");
        messageContainer.style.display = "block";
    };

    // Function to handle received messages
    socket.onmessage = function(event) {
        var messageLog = document.getElementById("messageLog");
        var message = event.data;

        // Append the received message to the message log
        messageLog.innerHTML += "<p>" + message + "</p>";
    };

    // Function to handle errors
    socket.onerror = function(error) {
        console.log("WebSocket error: " + error);
    };
}

// Function to send a message to the server
function sendMessage() {
    var messageInput = document.getElementById("messageInput");
    var message = messageInput.value;

    // Send the message to the server
    socket.send(message);

    // Clear the input field
    messageInput.value = "";
}



