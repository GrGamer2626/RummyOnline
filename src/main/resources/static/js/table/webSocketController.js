var tableId = null;
var client = null;

var currentSlot = -1;
var moveCardJson = null;


var startGameSlotSub;
var yourTurn;
var takeFromDeckSlotSub;
var takeFromStackSlotSub;
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

        //Take from deck
        client.subscribe(`/topic/table/${tableId}/takeFromDeck`, onTakeFromDeck);

        //Take from stack
        client.subscribe(`/topic/table/${tableId}/takeFromStack`, onTakeFromStack);


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
    yourTurn = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/yourTurn`, onYourTurn);

    takeFromDeckSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/takeFromDeck`, onTakeFromDeckSlotSubscription);
    takeFromStackSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/takeFromStack`, onTakeFromStackSlotSubscription);

    moveCardSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/moveCard`, onMoveCardSlotSubscription);
    replaceJokerSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/replaceJoker`, onReplaceJokerSlotSubscription);
    throwCardSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/throwCard`, onThrowCardSlotSubscription);

    layDownSlotSub = client.subscribe(`/user/topic/table/${tableId}/slot/${slotNumber}/layDown`, onLayDownSlotSubscription);
}



/************ Taske Slot ************/
//Send to server
function takeSlot(slotNumber) {
    const headers = {}
    client.send(`/app/rummy/table/${tableId}/takeSlot`, headers, slotNumber);
}

//Send to server
function leaveSlot(slotNumber) {
    const headers = {}
    client.send(`/app/rummy/table/${tableId}/leaveSlot`, headers, slotNumber);
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

//Send to server when player push start button
function start() {
    const headers = {}
    client.send(`/app/rummy/table/${tableId}/startGameCountDown`, headers, currentSlot);
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
    let action = serverResponse.body;
    document.querySelectorAll('.popup').forEach(element => {
        let elementClassList = element.classList;
        elementClassList.remove("popup-visible");
    });

    if(action === "StartCountDownRun") {
        let classList = document.getElementById("starting-countdown").classList;
        classList.add("popup-visible");

    }else if (action === "StartCountDownStop") {
        let classList = document.getElementById("waiting").classList;
        classList.add("popup-visible");

    }else if(action === "StartGame") {
        const headers = {}
        client.send(`/app/rummy/table/${tableId}/startGame`, headers, currentSlot);
    }
}


/************ Game ************/
//Server Response
function onStartGame(serverResponse) {
    let json = JSON.parse(serverResponse.body);
    for (let key = 1; key <= 6; key++) {
        if(key === currentSlot) continue;

        let cardCount = json[key];

        if(cardCount === 0) continue;

        let handElement = document.querySelector(`.hand[data-slot="${key}"]`);
        handElement.innerHTML = "";

        for (let i = 0; i < cardCount; i++) {
            let card = createBlankCard(handElement.id[11]);

            handElement.appendChild(card);
        }
    }
}

//Server Response
function onStartGameSlotSubscription(serverResponse) {
    let onHand = JSON.parse(serverResponse.body);
    let handElement = document.querySelector(`.hand[data-slot="${currentSlot}"]`);
    handElement.innerHTML = "";

    removeAllControllersButton();

    onHand.forEach(cardData => {
        let cardElement = createCard(cardData, "1");
        handElement.appendChild(cardElement);
    });
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
function takeFromDeck() {
    const headers = {}
    client.send(`/app/rummy/table/${tableId}/takeFromDeck`, headers, currentSlot);
}

function takeFromStack() {
    const headers = {}
    client.send(`/app/rummy/table/${tableId}/takeFromStack`, headers, currentSlot);
}


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

function throwCard(cardId, sourceSlot, sourceNumber) {
    const headers = {}
    const message = JSON.stringify(
        {
            'cardId' : cardId,
            'playerSlot' : currentSlot,
            'sourceSlot' : sourceSlot,
            'sourceNumber' : sourceNumber
        });

    client.send("/app/rummy/table/"+tableId+"/throwCard", headers, message);
}

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

    let card = document.querySelector(`[data-id="${cardId}"]`);
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



function isHand(number) {
    return number === -1;
}

function getFieldId(number, container) {
    return isHand(number) ? container.id[11] : container.id[1];
}

function removeAllOrientation(card) {
    const classList = ["normal", "left-side", "right-side", "upside-down"];
    classList.forEach(className => {
        card.classList.remove(className);
    });
}

function getGameCollection(number, slot) {
    const element = isHand(number) ?
    document.querySelector(`.hand[data-slot="${slot}"]`) :
    document.querySelector(`.sequence[data-slot="${slot}"][data-sequence="${number}"]`);

    return element;
}

//send to server
function layDown() {
    const headers = {}
    client.send(`/app/rummy/table/${tableId}/layDown`, headers, currentSlot);
}

/************ Table Owner ************/
function onTableOwner(serverResponse) {
    let json = JSON.parse(serverResponse.body);
}