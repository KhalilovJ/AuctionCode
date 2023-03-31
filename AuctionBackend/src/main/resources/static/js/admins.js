function approval(lotId, status){
    console.log("working")
    let statusOut;
    if (status == true){
        statusOut = 1
    } else {
        statusOut = 0
    }
    console.log("status is " + statusOut)
    let url = '/open/api/lots/approve-lot'
    console.log("Fetched ")
    fetch(url, {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify('{ "id": '+ lotId + ', "status": '+ statusOut +' }')
    })
        .then(response => response.json())
        .then(response => statuschange(response))
}

function statuschange(jsonStr){
    console.log(jsonStr)
    if (jsonStr.result == "success"){
        let element = document.getElementById("lotdiv" + jsonStr.id)
        element.remove();
    }
}