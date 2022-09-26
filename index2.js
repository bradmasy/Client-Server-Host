"use strict";

const sessionName = fetch("/Client-Server-A1/Login", {
    headers: {
        'Content-Type': 'application/json'
    },
    method: "GET"
})
.then((response) => response.json())
.then((user) => {
    document.getElementById("UserName").innerHTML = user.UserFirstNameValue;
});

document.getElementById("LogOutButton").addEventListener("click", async function(e){
    e.preventDefault();
    fetch("/Client-Server-A1/Logout", {
        method: "POST"
    })
    .then((response)=>{
        window.location.href = response.url;
    })
    
        

    console.log("Log out button clicked")
});

