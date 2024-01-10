//** Take From Deck **// 

function takeFromDeck() {
	const headers = {}
	client.send(`/app/rummy/${tableId}/takeFromDeck`, headers, currentSlot);	
}

function onTakeFromDeck(serverResponse) {
	const playerSlot = parseInt(serverResponse.body);

	if(playerSlot === currentSlot) return;

	const handElement = document.querySelector(`.hand[data-slot="${playerSlot}"]`);
	const card = createBlankCard(handElement.id[11]);

	handElement.appendChild(card);
}

function onTakeFromDeckSlotSubscription(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {cardDto, position, layDownPlayers} = json;

	takeCardSlotSubscription(cardDto, position);

	if(!layDownPlayers[currentSlot]) {
		const laydownButton = createButton(layDown, "Lay Down")
		const controllers = document.getElementById("controllers");
			  controllers.appendChild(laydownButton);

		enableDropping(currentSlot);	
		return;
	}

	for(let slot = 1; slot <= 6; slot++) {
		if(layDownPlayers[slot]) enableDropping(slot);
	}
}

//** Take From Stack **//

function takeFromStack() {
	const headers = {}
	client.send(`/app/rummy/${tableId}/takeFromStack`, headers, currentSlot);
}


function onTakeFromStack(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {playerSlot, cardDto} = json

	const throwCardContaner = document.getElementById("thrown-card-container");

	if(cardDto === null) {
		throwCardContaner.innerHTML="";

	}else {
		const throwCard = throwCardContaner.children[0];
			  throwCard.src = cardDto.imgPath;
			  throwCard.setAttribute("data-id", cardDto.id);
	}

	if(playerSlot === currentSlot) return;

	const hand = document.querySelector(`.hand[data-slot="${playerSlot}"]`);
		  hand.appendChild(createBlankCard(hand.id[11]));
}

function onTakeFromStackSlotSubscription(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const { cardDto, position, layDownPlayers } = json
	
	takeCardSlotSubscription(cardDto, position);

	const controllers = document.getElementById("controllers");

	const confirmButton = createButton(confirmCard, "Confirm Card");
	controllers.appendChild(confirmButton);
	
	const returnCardButton = createButton(returnCard, "Return&Take From Deck");
	controllers.appendChild(returnCardButton);

	for(let slot = 1; slot <= 6; slot++) {
		if(layDownPlayers[slot]) enableDropping(slot);
	}
}

//** Confirm Card**//

function confirmCard() {
	const header = {};
	client.send(`/app/rummy/${tableId}/confirmTakenCard`, header, currentSlot);
}

function onConfirmSlotSubscription(serverResponse) {
	const controllers = document.getElementById("controllers");
	controllers.innerHTML="";

}

//** Return Card And Take From Deck **//

function returnCard() {
	const header = {};
	client.send(`/app/rummy/${tableId}/returnCard`, header, currentSlot);
}

function onReturnCard(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {playerSlot, cardDto, sourceNumber, sourceSlot} = json;

	let card;
	const source = getGameCollection(sourceNumber, sourceSlot);
	if(isHand(sourceNumber)) {
		if(playerSlot !== currentSlot) {
			source.removeChild(source.children[0]);
			card = createCard(cardDto, "1");

		}else {
			card = source.querySelector(`.card[data-id="${cardDto.id}"]`);
			source.removeChild(card);
		}
	}else {
		card = source.querySelector(`.card[data-id="${cardDto.id}"]`);
		source.removeChild(card);
	}
	
	const thrownCardElement = document.getElementById("thrown-card-container");
		  thrownCardElement.innerHTML = "";
		  thrownCardElement.appendChild(card);

	if(playerSlot === currentSlot) return;

	const handElement = getHand(playerSlot);
		  handElement.appendChild(createBlankCard(handElement.id[11]));

}

function onReturnCardSlotSubscription(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {cardDto, position, layDownPlayers} = json

	const cardElement = createCard(cardDto, "1");

	const handElement = getHand(currentSlot);
	const handCards = handElement.children;

	handElement.insertBefore(cardElement, handCards[position]);

	enableDragging();
	removeAllControllersButton();

	handElement.classList.add("droppable");
	createDragOverListener(handElement);

	for(let slot = 1; slot <= 6; slot++) {
		if(layDownPlayers[slot]) enableDropping(slot);
	}
}

/** Utils **/

function takeCardSlotSubscription(cardDto, position) {
	const handElement = document.querySelector(`.hand[data-slot="${currentSlot}"]`);
	const handCards = handElement.children;

	const cardElement = createCard(cardDto, "1");

	handElement.insertBefore(cardElement, handCards[position]);

	enableDragging();
	removeAllControllersButton();

	handElement.classList.add("droppable");
	createDragOverListener(handElement);
}

