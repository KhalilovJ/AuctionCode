function payAuction(auctionId){
    let url = '/open/api/lots/payauction'
    fetch(url, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify('{ "id": '+ auctionId + ' }')
    })
        .then(response => response.json())
        .then(response => statuschange(response))
}

function statuschange(jsonStr){
    if (jsonStr.result == "success"){
        let element = document.getElementById("paybutton" + jsonStr.id)
        element.remove();
    }
}