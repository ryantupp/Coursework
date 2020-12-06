"use strict";

drawVisualization();

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawVisualization);

function addActivity() {
    //debugger;
    console.log("Invoked addActivity");//prints to console
    let url = "/activitiesCompleted/addActivity"; //sets URL to the correct API call

    let formData = new FormData(document.getElementById('addActivityForm')); //sets formData to the data in the form

    fetch(url, {
        method: "POST",
        body: formData,//calls function in Users.java
    }).then(response => {
        return response.json();
    }).then(response => {
        if (response.hasOwnProperty("Error")) {//checks for error
            alert(JSON.stringify(response));
        } else {
            alert("Activity added");
        }
    });
}




function drawVisualization() {

    //debugger;

    console.log("Invoked drawVisualization");//prints to console
    let url = "/activitiesCompleted/drawGraph"; //sets URL to the correct API call
    var calories;

    fetch(url, {
        method: "GET",
    }).then(response => {

        //console.log(response.json());
        return response;
    }).then(response => {
        if (response.hasOwnProperty("Error")) {//checks for error
            alert(JSON.stringify(response));
        } else {

            //console.log(response);
            //console.log(response.json());
            //console.log(response[1].stringify());

            calories = response;
            console.log("information retrieved for graph.")


            var data = google.visualization.arrayToDataTable(
                [
                    [0, 0],
                    [0,  0],
                    [0,  0],
                    [0,  0],
                    [0,  0],
                    [0,  0],
                    [0,  0],
                    [0,  0],
                    [0,  0],
                    [0,  0]
                ]
            );

            console.log("DATA : " + data);

            for (let i = 0; i < 10; i++){
                data[[i], [0]] = i + 1;
                data[[i], [1]] = calories[i];
            }

            console.log("DATA : " + data);

            var options = {
                title : 'Activity Graph',
                vAxis: {title: 'Calories'},
                hAxis: {title: 'Activity Number'},
                seriesType: 'bars',
                series: {5: {type: 'line'}}
            };

            var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
            chart.draw(data, options);

            alert("Activity graph drawn");
            console.log(response);
        }
    });

}



function generateGoals(){

    var goals = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];
    let same = false;
    let num1 = Math.floor((Math.random() * 10));
    let num2 = Math.floor((Math.random() * 10));
    let num3 = Math.floor((Math.random() * 10));

    if(num1 == num2 || num1 == num3 || num2 == num3){
        same = true;
    }
    while (same){
        num2 = Math.floor((Math.random() * 10));
        num3 = Math.floor((Math.random() * 10));
        if(num1 == num2 || num1 == num3 || num2 == num3){
            same = true;
        } else {
            same = false;
        }
    }

    let goal1 = goals[num1];
    let goal2 = goals[num2];
    let goal3 = goals[num3];

    document.getElementById("goalChoice1").innerHTML = goal1;
    document.getElementById("goalChoice2").innerHTML = goal2;
    document.getElementById("goalChoice3").innerHTML = goal3;
}
