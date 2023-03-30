let i = 1;
const bidsWrapper = document.getElementById("bids");
let bidstepVal = parseFloat(document.getElementById("bid_step").innerText);

let bidplaced = true
let startDate = new Date(document.getElementById("startDate").getAttribute("data"));

function pad(n) {
    return (n < 10 ? "0" + n : n);
}
function updateBidBox(){
    let currentBid = document.getElementById("currentBid").innerText;
    i=1;
    document.getElementById('inc').value = (bidstepVal+parseFloat(currentBid)).toFixed(2);

}
function subscribe(){

    let url = new URL(window.location.href);

    let lotId = url.pathname.split("/")[2];

    let urlEndpoint = `http://127.0.0.1:9090/open/api/bids/${lotId}`
    let eventSource = new EventSource(urlEndpoint)

    eventSource.addEventListener("bid", function(event){

        let data = JSON.parse(event.data);

        let updatedDatetime = new Date(data.time);
        let json =  JSON.parse(data.bid);

        let text = json.lotCurrentBidPrice;
        let bidtext = '<span class="col-md-3">'.concat(json.username).concat("</span>");
        bidtext = bidtext.concat('<span class="col-md-3">').concat(parseFloat(json.bid).toFixed(2)).concat("</span>");

        let timestamp= new Date(json.bidTime);
        // let datetext = timestamp.toISOString().split('T')[0]
        let datetext = timestamp.getDate() + '.' + pad(timestamp.getMonth())+ '.' + pad(timestamp.getFullYear());

        var seconds = timestamp.getSeconds();
        var minutes = timestamp.getMinutes();
        var hour = timestamp.getHours();
        let timetext = datetext+ " " + pad(hour) + ":" + pad(minutes)+":" + pad(seconds)
        bidtext = bidtext.concat('<span class="col-md-6">').concat(timetext).concat("</span>");

        const nodeDiv = document.createElement("div");
        nodeDiv.setAttribute("class", "shadow-sm border d-flex justify-content-between rounded-3");
        nodeDiv.innerHTML = bidtext

        document.getElementById("currentBid").textContent=text;
        bidsWrapper.prepend(document.createElement("br"));
        bidsWrapper.prepend(nodeDiv)
        updateBidBox();

        if (data.time !=  undefined){ // time must be changed
            document.getElementById("endDate").innerText = updatedDatetime;
            convertInnertextToDateTime("endDate");
            date1 = updatedDatetime
            updateTimeCircle()
        }

    })
}
subscribe();



function UserAction() {

    let url = new URL(window.location.href);
    let lotId = url.pathname.split("/")[2];

    let input = "/open/api/bids/makeBid";
    let bidout = parseFloat(document.getElementById("inc").value).toFixed(2);
    let currentBid = parseFloat(document.getElementById("currentBid").innerText).toFixed(2);

    if (parseFloat(bidout) >= parseFloat(currentBid) + bidstepVal && bidplaced){

        bidplaced = false

        fetch(input, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },

            body: JSON.stringify('{ "lotId": '.concat(lotId).concat(' , "bid": ').concat(bidout).concat(' }'))


        })
            .then(response => {
                bidplaced = true
                updateBidBox();
            });
    } else {
        // bidplaced = true
        console.log("Bid error")}

}


function buttonClickP() {
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    document.getElementById('inc').value = ++i*bidstepVal+currentBid;
}
function buttonClickM() {
    i--
    if (i<= 0){ i = 1}
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    document.getElementById('inc').value = i*bidstepVal+currentBid;
}


function updateBid(){
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    if(document.getElementById('inc') != null){
        document.getElementById('inc').value = bidstepVal+currentBid;}
}

updateBid();

var date1 = new Date(document.getElementById("endDateW").innerText);

var upgradeTime = 172801;

var seconds = upgradeTime;
function timer() {

    if (date1 > Date.now() && startDate<Date.now()) {
        var difference = date1.getTime() - Date.now();

        var daysDifference = Math.floor(difference / 1000 / 60 / 60 / 24);
        difference -= daysDifference * 1000 * 60 * 60 * 24

        var hoursDifference = Math.floor(difference / 1000 / 60 / 60);
        difference -= hoursDifference * 1000 * 60 * 60

        var minutesDifference = Math.floor(difference / 1000 / 60);
        difference -= minutesDifference * 1000 * 60

        var secondsDifference = Math.floor(difference / 1000);



        document.getElementById('countdown').innerHTML = "Auksionun bitməsinə qalan vaxt: " + pad(daysDifference) + " gün " + pad(hoursDifference) + " saat " + pad(minutesDifference) + " dəqiqə " + pad(secondsDifference) + " saniyə";
        if (seconds <= 0) {
            clearInterval(countdownTimer);
            document.getElementById('countdown').innerHTML = "Completed";
            document.getElementById("controlsArea").remove();

        } else {
            seconds--;
        }
    } else if(startDate < Date.now()){

        let bidarea = document.getElementById('bid_area')
        bidarea.innerHTML = "                   <div class=\"justify-content-center d-flex\"><div>\n" +
            "                                <p >Hərrac bağlıdır</p>"
            "</div>";
        clearInterval(countdownTimer);
        bidplaced = false;
    } else {
        let bidarea = document.getElementById('bid_area')
        bidarea.innerHTML = "                   <div class=\"justify-content-center d-flex\"><div>\n" +
            "                                <p >Hərrac başlamaq üzrədir</p>"
        "</div>";

        clearInterval(countdownTimer);
        bidplaced = false;

    }
}
var countdownTimer = setInterval('timer()', 1000);
function convertInnertextToDateTime(elementId){
    let element = document.getElementById(elementId)
    let innertext = element.innerText
    let datestamp = new Date(innertext)

    let datetext = datestamp.getDate() + '.' + pad(datestamp.getMonth())+ '.' + pad(datestamp.getFullYear());

    // let datetext = datestamp.toISOString().split('T')[0]
    let seconds = datestamp.getSeconds();
    let minutes = datestamp.getMinutes();
    let hour = datestamp.getHours();
    let timetext = datetext +" " + pad(hour) + ":"+ pad(minutes)+":"+ pad(seconds)

    element.innerText = timetext;

}
function  init(){
    convertInnertextToDateTime("endDate")
    convertInnertextToDateTime("startDate")

    var elements = document.getElementsByClassName("changeTime");
    for(var i = 0; i < elements.length; i++) {
        convertInnertextToDateTime(elements[i].getAttribute("id"))
    }
}




// Credit: Mateusz Rybczonec

const FULL_DASH_ARRAY = 283;
const WARNING_THRESHOLD = 10;
const ALERT_THRESHOLD = 5;

const COLOR_CODES = {
    info: {
        color: "green"
    },
    warning: {
        color: "orange",
        threshold: WARNING_THRESHOLD
    },
    alert: {
        color: "red",
        threshold: ALERT_THRESHOLD
    }
};

// let seconds = Math.abs(x.getTime() - y.getTime())/1000;
// let TIME_LIMIT = 400;
let timenow = new Date(Date.now());
let TIME_LIMIT = Math.abs(date1.getTime() - timenow.getTime())/1000;
let timePassed = 0;
let timeLeft = TIME_LIMIT;
let timerInterval = null;
let remainingPathColor = COLOR_CODES.info.color;

function updateTimeCircle(){
    timenow = new Date(Date.now());
    TIME_LIMIT = Math.abs(date1.getTime() - timenow.getTime())/1000;
    timePassed = 0;
}

let app = document.getElementById("app")
    if(app != null) {
    app.innerHTML = `
<div class="base-timer my-auto mx-auto">
  <svg class="base-timer__svg" viewBox="0 0 100 100" xmlns="http://www.w3.org/2000/svg">
    <g class="base-timer__circle">
      <circle class="base-timer__path-elapsed" cx="50" cy="50" r="45"></circle>
      <path
        id="base-timer-path-remaining"
        stroke-dasharray="283"
        class="base-timer__path-remaining ${remainingPathColor}"
        d="
          M 50, 50
          m -45, 0
          a 45,45 0 1,0 90,0
          a 45,45 0 1,0 -90,0
        "
      ></path>
    </g>
  </svg>
  <span id="base-timer-label" class="base-timer__label">${formatTime(
            timeLeft
        )}</span>
</div>
`;
    }

startTimer();

function onTimesUp() {
    clearInterval(timerInterval);
}

function startTimer() {
    timerInterval = setInterval(() => {
        timePassed = timePassed += 1;
        timeLeft = TIME_LIMIT - timePassed;
        document.getElementById("base-timer-label").innerHTML = formatTime(
            timeLeft
        );

        setCircleDasharray();
        setRemainingPathColor(timeLeft);

        if (timeLeft === 0) {
            onTimesUp();
        }
        if (timeLeft > 600){
            document.getElementById("appDiv").style.display = 'none';
            document.getElementById("countdown").style.display = 'block';
        }else if(timeLeft <= 0){
            document.getElementById("appDiv").style.display = 'none';
            document.getElementById("countdown").style.display = 'none';
            document.getElementById("controlsArea").remove();

        }
        else {
            document.getElementById("appDiv").style.display = 'block';
            document.getElementById("countdown").style.display = 'none';
        }
    }, 1000);
}

function formatTime(time) {
    const minutes = Math.floor(time / 60);
    let seconds = Math.trunc(time % 60);

    if (seconds < 10) {
        seconds = `0${seconds}`;
    }

    return `${minutes}:${seconds}`;
}

function setRemainingPathColor(timeLeft) {
    const { alert, warning, info } = COLOR_CODES;
    if (timeLeft <= alert.threshold) {
        document
            .getElementById("base-timer-path-remaining")
            .classList.remove(warning.color);
        document
            .getElementById("base-timer-path-remaining")
            .classList.add(alert.color);
    } else if (timeLeft <= warning.threshold) {
        document
            .getElementById("base-timer-path-remaining")
            .classList.remove(info.color);
        document
            .getElementById("base-timer-path-remaining")
            .classList.add(warning.color);
    }
}

function calculateTimeFraction() {
    const rawTimeFraction = timeLeft / TIME_LIMIT;
    return rawTimeFraction - (1 / TIME_LIMIT) * (1 - rawTimeFraction);
}

function setCircleDasharray() {
    const circleDasharray = `${(
        calculateTimeFraction() * FULL_DASH_ARRAY
    ).toFixed(0)} 283`;
    document
        .getElementById("base-timer-path-remaining")
        .setAttribute("stroke-dasharray", circleDasharray);
}


init()