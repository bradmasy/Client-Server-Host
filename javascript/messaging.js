"use strict";

var MessageSocket = {};

// Stores the session user's name
var username = "";

MessageSocket.socket = null;

MessageSocket.connect = (function (host) {
    // Checks the browser
    if ('WebSocket' in window) {
        MessageSocket.socket = new WebSocket(host);

    } else if ('MozWebSocket' in window) {
        MessageSocket.socket = new MozWebSocket(host);

    } else {
        document.getElementById("connectionMessage").textContent = "Cannot connect to chat app because the browser is unsupported.";
        return;
    }

    // When socket connection is opened
    MessageSocket.socket.onopen = function () {
        document.getElementById("connectionMessage").textContent = "Connected to chat app.";

        // When Send button is clicked
        document.getElementById('sendMessageBtn').onclick = function (event) {
            MessageSocket.sendMessage();
        };
    };

    // When socket connection is closed
    MessageSocket.socket.onclose = function () {
        document.getElementById('messageInput').onkeydown = null;

        document.getElementById("connectionMessage").textContent = "Cannot connect to chat app.";
    };

    // When a message is sent using a socket connection
    MessageSocket.socket.onmessage = function (message) {
        Console.log(message.data);
    };
});

MessageSocket.initialize = function () {
    if (window.location.protocol == 'http:') {
        /* Connects to Chat Servlet java file */
        MessageSocket.connect('ws://' + window.location.host + '/Client-Server-A1/hub');

    } else {
        MessageSocket.connect('wss://' + window.location.host + '/Client-Server-A1/hub');
    }
};

// Sends a message
MessageSocket.sendMessage = (function () {
    var messageInput = document.getElementById('messageInput').value;

    // Check if input is valid/not empty
    if (messageInput != '') {
        // Format of message: 'user: message'
        let messageValue = username + ": " + messageInput;

        // Send message using a socket
        MessageSocket.socket.send(messageValue);

        // Clear text field input
        document.getElementById('messageInput').value = '';
    }
});

var Console = {};

// When a message appears on the page
Console.log = (function (message) {
    var messageDisplayDiv = document.getElementById('messageDisplayDiv');

    // Create HTML element for the message and append it to the page
    var textElement = document.createElement('p');
    textElement.innerHTML = message;
    messageDisplayDiv.appendChild(textElement);

    // Creates auto scroll in the message div
    messageDisplayDiv.scrollTop = messageDisplayDiv.scrollHeight;
});

MessageSocket.initialize();



// Retrieves the current session user's name from the Java servlet 
function fetchSessionUsername() {

    // Send a fetch post request to MessageServlet.java
    fetch("/Client-Server-A1/message", {method: 'POST'})
        .then(response => {
            if (response.ok) {
                return response.json();
            }
        })
        .then(object => {

            // Fetch sends back username from the current session
            let returnedUsername = object.sessionUser;

            // Store session username is a global variable to access it when sending messages
            username = returnedUsername;

            // Display user's name on the page
            document.getElementById("username").innerHTML = returnedUsername;
        });
}
fetchSessionUsername();


// Method to disable send button if there's no input
function checkValidMessageInput() {
    // Get the current value in the text field
    let messageInputValue = document.getElementById("messageInput").value;
    let sendMessageBtn = document.getElementById("sendMessageBtn");

    // If there's no input in the text field, disable the send button
    if (messageInputValue.trim() == "") {
        sendMessageBtn.disabled = true;
        sendMessageBtn.style.cursor = "not-allowed";

    } else {
        // If there's input, enable the button so user can send a valid message
        sendMessageBtn.disabled = false;
        sendMessageBtn.style.cursor = "pointer";
    }
};