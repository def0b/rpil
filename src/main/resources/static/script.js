function addAccount() {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/account', false);
    xhr.send();
    if (xhr.status != 200) {
        alert(xhr.status + ': ' + xhr.statusText);
    } else {
        document.getElementById('newAccountResult').innerHTML = xhr.responseText.replace('"', '').replace('"', '');
    }
}

function deleteAccount() {
    var xhr = new XMLHttpRequest();
    var params = 'id=' + encodeURI(document.getElementById('deleteAccUuid').value);
    xhr.open('DELETE', '/api/account' + '?' + params, false);
    xhr.send();
    if (xhr.status != 200) {
        alert(xhr.status + ': ' + xhr.statusText);
    } else {
        document.getElementById('deleteResult').innerHTML = xhr.responseText;
    }
}

function getAllAccount() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/account/all', false);
    xhr.send();
    if (xhr.status != 200) {
        alert(xhr.status + ': ' + xhr.statusText);
    } else {
        var tableName = 'allAccounts';
        var table = document.getElementById(tableName);
        var rowCount = table.rows.length;
        for (var i = 0; i < rowCount; i++) {
            table.deleteRow(0);
        }
        var responseArray = JSON.parse(xhr.response);
        for (var i = 0, len = responseArray.length; i < len; i++) {
            var newRow = table.insertRow();
            var newCell = newRow.insertCell();
            var newText = document.createTextNode(responseArray[i]);
            newCell.appendChild(newText);
        }
    }
}

function getBalance() {
    var xhr = new XMLHttpRequest();
    var params = 'id=' + encodeURI(document.getElementById('getBalanceUuid').value);
    xhr.open('GET', '/api/account' + '?' + params, false);
    xhr.send();
    if (xhr.status != 200) {
        alert(xhr.status + ': ' + xhr.statusText);
    } else {
        document.getElementById('getBalanceResult').innerHTML = xhr.responseText;
    }
}

function decreaseBalance() {
    var xhr = new XMLHttpRequest();
    var params = 'id=' + encodeURI(document.getElementById('decreaseBalanceUuid').value)
        + '&' + 'sum=' + encodeURI(document.getElementById('decreaseBalanceSum').value);
    xhr.open('PUT', '/api/transfers/decrease' + '?' + params, false);
    xhr.send();
    if (xhr.status != 200) {
        alert(xhr.status + ': ' + xhr.statusText);
    } else {
        document.getElementById('decreaseBalanceResult').innerHTML = xhr.responseText;
    }
}

function increaseBalance() {
    var xhr = new XMLHttpRequest();
    var params = 'id=' + encodeURI(document.getElementById('increaseBalanceUuid').value)
        + '&' + 'sum=' + encodeURI(document.getElementById('increaseBalanceSum').value);
    xhr.open('PUT', '/api/transfers/increase' + '?' + params, false);
    xhr.send();
    if (xhr.status != 200) {
        alert(xhr.status + ': ' + xhr.statusText);
    } else {
        document.getElementById('increaseBalanceResult').innerHTML = xhr.responseText;
    }
}

function transferBalance() {
    var xhr = new XMLHttpRequest();
    var params = 'source=' + encodeURI(document.getElementById('transferFrom').value)
        + '&' + 'dest=' + encodeURI(document.getElementById('transferTo').value)
        + '&' + 'sum=' + encodeURI(document.getElementById('transferSum').value);
    xhr.open('PUT', '/api/transfers/transfer' + '?' + params, false);
    xhr.send();
    if (xhr.status != 200) {
        alert(xhr.status + ': ' + xhr.statusText);
    } else {
        document.getElementById('transferResult').innerHTML = xhr.responseText;
    }
}
