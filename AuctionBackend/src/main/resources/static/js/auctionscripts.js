let i = 1;
const bidsWrapper = document.getElementById("bids");
let bidstepVal = parseFloat(document.getElementById("bid_step").innerText);

function updateBidBox(){
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    i=1;
    document.getElementById('inc').value = bidstepVal+parseFloat(currentBid);
}
function subscribe(){

    let url = new URL(window.location.href);

    console.log(url);

    let lotId = url.pathname.split("/")[2];

    console.log(lotId)

    let urlEndpoint = `http://127.0.0.1:9090/open/api/bids/${lotId}`
    let eventSource = new EventSource(urlEndpoint)

    eventSource.addEventListener("bid", function(event){
        let json = JSON.parse(event.data);
        let text = json.lotCurrentBidPrice;
        let bidtext = json.username.concat(" ").concat(json.bid);
        const nodeDiv = document.createElement("div");
        nodeDiv.innerHTML = bidtext
        document.getElementById("currentBid").textContent=text;
        bidsWrapper.appendChild(nodeDiv)
        console.log(event.data)
        updateBidBox();
    })
}
subscribe();



function UserAction() {

    console.log("Bid placed")
        let url = new URL(window.location.href);
        let lotId = url.pathname.split("/")[2];

        let input = "/open/api/bids/makeBid";
        let bidout = parseFloat(document.getElementById("inc").value);
        let currentBid = parseFloat(document.getElementById("currentBid").innerText);
        if (bidout > currentBid){

        fetch(input, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },

            body: JSON.stringify('{ "lotId": '.concat(lotId).concat(' , "bid": ').concat(bidout).concat(' }'))


        })
            .then(response => {
            console.log(response);

            updateBidBox();

            });
        } else {
            console.log(bidout); console.log(currentBid)
            console.log("Bid error")}

}


function buttonClickP() {
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    document.getElementById('inc').value = ++i*bidstepVal+currentBid;
    console.log(document.getElementById("inc").value);
}
function buttonClickM() {
    i--
    if (i<= 0){ i = 1}
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    document.getElementById('inc').value = i*bidstepVal+currentBid;
}


function updateBid(){
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    document.getElementById('inc').value = bidstepVal+currentBid;
}

updateBid();