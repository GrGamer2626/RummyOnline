var lobbyClient = null;

var createTableSub;
var createTableUserSub;
var removeTableSub;
var joinTableSub;

function connect() {
    lobbyClient = Stomp.client("ws://localhost:8080/lobbySocket");
    const headers = {}

    lobbyClient.connect(headers, (frame) => {
        createTableSub = lobbyClient.subscribe(`/topic/table/create`, onCreateTable);
		createTableUserSub = lobbyClient.subscribe(`/user/topic/table/create`, onCreateTableUser);

        removeTableSub = lobbyClient.subscribe(`/topic/table/remove`, onRemoveTable);

		joinTableSub = lobbyClient.subscribe(`/user/topic/table/join`, onJoinTable);

    });
}

function createTable() {
    const message = "c";
    const headers = {};
    lobbyClient.send(`/app/lobbySocket/table/create`, headers, message);
}

function onCreateTable(serverResponse) {
	const json = JSON.parse(serverResponse.body);
	const {tableId, tableOwner} = json;

	const tableButton = createTableButton(tableId, tableOwner);

	const tableList = document.getElementById("table-list");
		  tableList.appendChild(tableButton);
}

function onCreateTableUser(serverResponse) {
	const json = JSON.parse(serverResponse.body);
	const {tableId} = json;

	joinTable(tableId);
}

function onRemoveTable(serverResponse) {
	const json = JSON.parse(serverResponse.body);
	const {tableId} = json;

	const tables = document.querySelectorAll(".table");

	tables.forEach(table => {
		const currentTableId = table.querySelector("#table-id").innerText;
		if(currentTableId === tableId) {
			table.remove();
		}
	});
}

function joinTable(tableId) {
    const message = JSON.stringify(
        {
            "tableId" : tableId
        });
    const headers = {};
    lobbyClient.send(`/app/lobbySocket/table/join`, headers, message);
}

function onJoinTable(serverResponse) {
	const json = JSON.parse(serverResponse.body);
	const {tableId} = json;

	createTableSub.unsubscribe();
	createTableUserSub.unsubscribe();
	removeTableSub.unsubscribe();
	joinTableSub.unsubscribe();

	const url = '/table/' + tableId;
	window.location.href = url;
}

function createTableButton(tableId, tableOwnerName) {
	const tableButton = document.createElement("button");
		  tableButton.className = "table";
		  tableButton.onclick = () => joinTable(tableId);

	const tableIdDiv = createDiv("Table ", "table-owner", tableId)
	const tableOwnerDiv = createDiv("Owner: ", "table-owner", tableOwnerName)

	tableButton.appendChild(tableIdDiv);
	tableButton.appendChild(tableOwnerDiv);

	return tableButton;
}

function createSpan(spanId, spanText) {
	const spanElement = document.createElement("span");
		  spanElement.id = spanId;
		  spanElement.innerText = spanText;

	return spanElement;
}

function createDiv(divText, spanId, spanText) {
	const divElement = document.createElement("div");
		  divElement.innerText = divText;
		  divElement.appendChild(createSpan(spanId, spanText));

	return divElement;
}