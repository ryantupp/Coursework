"use strict";
function checkLogIn() {
    console.log("Invoked checkLogIn()");
    if(Token != null){
        window.open("login.html", "_self");
    }
}