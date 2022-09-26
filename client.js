"use strict";

// const WelcomeMessage = document.getElementById("UserName");
// WelcomeMessage.innerText = name;


document.getElementById("Login-Button").addEventListener("click", async function(e){
    e.preventDefault();
    const name = document.getElementById("FirstName-Input").value;
    const password = document.getElementById("Password-Input").value;
    console.log("INPUT:" + name);
    console.log("Form Submitted");


    try{
        response = await fetch("/Client-Server-A1/Login", {
            headers: {
                'Content-Type': 'application/json'
            },
            mode: "cors",
            method: "POST",
            body: JSON.stringify({UserFirstName: name, UserPassword: password})
        })
        .then((response)=>{
            // console.error("URL: " + response.url);
            window.location.href = response.url;
        });

    } catch(err) {

        console.log(err);

    }
    
    // Get Request
    // try{
    //     response = await fetch("/Client-Server-A1/Login", {
    //         headers: {
    //             'Content-Type': 'application/json'
    //         },
    //         method: "GET"
    //     });
    // } catch(err) {
    //     console.log(err);
    // }
    


    // const status = await response.toString();
    // console.log(status);
    // console.log("Before: " + window.location.href);
    // switch(status){
    //     case "OK":
    //         window.location.href = "/index2.html";
    //         console.log("After: " + window.location.href);
    //         break;
    // }

});