var tableId = null;
var client = null;

var currentSlot = -1;
var moveCardJson = null;


var startGameSlotSub;
var endGameSlotSub;
var yourTurn;

var takeFromDeckSlotSub;
var takeFromStackSlotSub;
var confirmTakenCardSlotSub;
var returnCardSlotSub;

var moveCardSlotSub;
var replaceJokerSlotSub;
var throwCardSlotSub;
var layDownSlotSub;

function connect() {
	tableId = document.getElementById("tableId").innerText;
	client = Stomp.client("ws://localhost:8080/rummy");
	const headers = {}

	client.connect(headers, (frame) => {
		//Update leave slot state for everyone
		client.subscribe(`/topic/table/${tableId}/takeSlot`, onTakeSlotGeneral);

		//Update leave slot state for everyone
		client.subscribe(`/topic/table/${tableId}/leaveSlot`, onLeaveSlot);

		//Reciving count down time
		client.subscribe(`/topic/table/${tableId}/startGameCountDown`, onStartGameCountDown);

		//Displaying information popups
		client.subscribe(`/topic/table/${tableId}/popupVisibility`, onPopupVisibility);

		//Start game data
		client.subscribe(`/topic/table/${tableId}/startGame`, onStartGame);

		//End game
		client.subscribe(`/topic/table/${tableId}/endGame`, onEndGame);

		//Take from deck
		client.subscribe(`/topic/table/${tableId}/takeFromDeck`, onTakeFromDeck);

		//Take from stack
		client.subscribe(`/topic/table/${tableId}/takeFromStack`, onTakeFromStack);
		client.subscribe(`/topic/table/${tableId}/returnCard`, onReturnCard);


		client.subscribe(`/topic/table/${tableId}/moveCard`, onMoveCard);
		client.subscribe(`/topic/table/${tableId}/replaceJoker`, onReplaceJoker);
		client.subscribe(`/topic/table/${tableId}/throwCard`, onThrowCard);

		client.subscribe(`/topic/table/${tableId}/layDown`, onLayDown);

		//Add to user to certain slot subscription
		client.subscribe(`/user/topic/table/${tableId}/slotSubscription`, onSlotSubscription);
		//Show start button
		client.subscribe(`/user/topic/table/${tableId}/showStartButton`, onShowStartButton);
		//Hide start button
		client.subscribe(`/user/topic/table/${tableId}/hideStartButton`, onHideStartButton);

		client.subscribe(`/user/topic/table/${tableId}/confirmTakenCard`, onConfirmSlotSubscription);

		client.subscribe(`/user/topic/table/${tableId}/tableOwner`, onTableOwner)
	});
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/************ Slot Subscription ************/
//Server response
function onSlotSubscription(serverResponse) {
	let json = JSON.parse(serverResponse.body);
	let slotNumber = json.slot;

	currentSlot = slotNumber;

	let slotId = `player-slot-${slotNumber}`;
	let slotElement = createTakenSlot(slotId, json.playerName);
	addLeaveSlotButton(slotElement, slotNumber);

	for(let i = 1; i <= 6; i++) {
		setPlayerSlots(i, slotNumber);
	}

	startGameSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/startGame`, onStartGameSlotSubscription);
	endGameSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/endGame`, onEndGameSlotSubscription);
	yourTurn = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/yourTurn`, onYourTurn);

	takeFromDeckSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/takeFromDeck`, onTakeFromDeckSlotSubscription);
	takeFromStackSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/takeFromStack`, onTakeFromStackSlotSubscription);
	returnCardSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/returnCard`, onReturnCardSlotSubscription);

	moveCardSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/moveCard`, onMoveCardSlotSubscription);
	replaceJokerSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/replaceJoker`, onReplaceJokerSlotSubscription);
	throwCardSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/throwCard`, onThrowCardSlotSubscription);

	layDownSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/layDown`, onLayDownSlotSubscription);
}


/************ StartButton ************/
//Server Response
function onShowStartButton(serverResponse) {
	let startButton = createButton(start, "Start");

	removeAllControllersButton();
	let controllers = document.getElementById("controllers");
	controllers.appendChild(startButton);

	setWaitPopupContent("Press start");
}

//Server Response
function onHideStartButton(serverResponse) {
	removeAllControllersButton();
	setWaitPopupContent("Waiting for players");
}



//Server Response
function onStartGameCountDown(serverResponse) {
	let time = JSON.parse(serverResponse.body);

	const timeElement = document.getElementById("time");
	timeElement.innerHTML = "";
	timeElement.textContent = time;
}

//Server Response
function onPopupVisibility(serverResponse) {
	const action = serverResponse.body;
	document.querySelectorAll('.popup').forEach(element => {
		const elementClassList = element.classList;
			  elementClassList.remove("popup-visible");
	});

	if(action === "StartCountDownRun") {
		const classList = document.getElementById("starting-countdown").classList;
			  classList.add("popup-visible");

	}else if (action === "StartCountDownStop") {
		const classList = document.getElementById("waiting").classList;
			  classList.add("popup-visible");

	}else if(action === "StartGame") {
		const headers = {}
		client.send(`/app/rummy/table/${tableId}/startGame`, headers, currentSlot);
	}
}

//Server Response
function onYourTurn(serverResponse) {
	let isLayDown = parseBoolean(serverResponse.body);

	let takeFromDeckButton = createButton(takeFromDeck, "Take From Deck");
	let controllers = document.getElementById("controllers");
		controllers.innerHTML = "";
		controllers.appendChild(takeFromDeckButton);

	if(isLayDown) {
		let takeFromStackButton = createButton(takeFromStack, "Take From Stack");
		controllers.appendChild(takeFromStackButton);
	}
}

//Send to server

document.addEventListener("cardDroped", event => {
	let cardId = event.cardId

	let source = document.getElementById(event.sourceId);
	let sourceSlot = source.getAttribute('data-slot');
	let sourceNumber = -1;

	if(source.classList.contains("sequence")) {
		sourceNumber = source.getAttribute("data-sequence");
	}

	let destinationId = event.destinationId;
	if(destinationId === "thrown-card-container") {
		throwCard(cardId, sourceSlot, sourceNumber);
		return;
	}
	moveCard(cardId, sourceSlot, sourceNumber, destinationId);
});


function moveCard(cardId, sourceSlot, sourceNumber, destinationId) {
	let destinationNumber = -1;

	let destination = document.getElementById(destinationId);
	if(destination.classList.contains("sequence")) {
		destinationNumber = destination.getAttribute("data-sequence");
	}
	moveCardJson = {
		'cardId' : cardId,
		'figure' : null,
		'playerSlot' : currentSlot,
		'sourceSlot' : sourceSlot,
		'sourceNumber' : sourceNumber,
		'destinationSlot' : destination.getAttribute('data-slot'),
		'destinationNumber' : destinationNumber
	};

	const card = document.querySelector(`[data-id="${cardId}"]`);
	if(card.src.includes("joker") && !isHand(destinationNumber)) {
		let popup = document.getElementById("joker-figure");
		popup.classList.add("popup-visible");
		return;
	}

	const headers = {}
	const message = JSON.stringify(moveCardJson);

	client.send(`/app/rummy/table/${tableId}/moveCard`, headers, message);
	client.send(`/app/rummy/table/${tableId}/replaceJoker`, headers, message);

	moveCardJson = null;
}

function setJokerValue(value) {
	if(moveCardJson === null) return;

	moveCardJson.figure = value;

	const headers = {}
	const message = JSON.stringify(moveCardJson);
	client.send(`/app/rummy/table/${tableId}/moveCard`, headers, message);

	moveCardJson = null;

	let popup = document.getElementById("joker-figure");
	popup.classList.remove("popup-visible");
}

////////////////////////////////////////////////////////////////////////////////


//send to server
function layDown() {
	const headers = {}
	client.send(`/app/rummy/table/${tableId}/layDown`, headers, currentSlot);
}

/************ Table Owner ************/
function onTableOwner(serverResponse) {
	let json = JSON.parse(serverResponse.body);
}