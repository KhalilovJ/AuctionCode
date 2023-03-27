function approval(lotId, status){
    let statusOut;
    if (status == true){
        statusOut = 1
    } else {
        statusOut = 0
    }
    let url = '/open/api/lots/approve-lot'
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