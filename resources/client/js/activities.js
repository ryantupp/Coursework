"use strict";

drawVisualization();

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

        calories = response;
        console.log("information retrieved for graph.")
        console.log(calories[1]);

        var data = google.visualization.arrayToDataTable([
        ['Activity', 'Calories'],
        ['2004/05',  165],
        ['2005/06',  135],
        ['2006/07',  157],
        ['2007/08',  139],
        ['2008/09',  136]
    ]);
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
        console.log(response.json());
        return response;
    }).then(response => {
        if (response.hasOwnProperty("Error")) {//checks for error
            alert(JSON.stringify(response));
        } else {
            console.log(response);
            alert("Activity graph drawn");
        }
    });



}
