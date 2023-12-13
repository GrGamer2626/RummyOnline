function onTakeFromDeck(serverResponse) {
    let playerSlot = parseInt(serverResponse.body);

    if(playerSlot === currentSlot) return;

    let handElement = document.querySelector(`.hand[data-slot="${playerSlot}"]`);
    let card = createBlankCard(handElement.id[11]);

    handElement.appendChild(card);
}

function onTakeFromDeckSlotSubscription(serverResponse) {
    const json = JSON.parse(serverResponse.body);

    const {cardDto, position, layDownPlayers} = json;


    const handElement = document.querySelector(`.hand[data-slot="${currentSlot}"]`);
    const cards = handElement.children;

    const cardElement = createCard(cardDto, "1");

    handElement.insertBefore(cardElement, cards[position]);

    enableDragging();
    removeAllControllersButton();

    handElement.classList.add("droppable");
    createDragOverListener(handElement);

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

    const hand = document.querySelector(`.hand[data-slot="${currentSlot}"]`);
    const handCards = hand.children;

    const card = createCard(cardDto, "1");
    hand.insertBefore(card, handCards[position]);

    enableDragging();
    removeAllControllersButton();

	const confirmButton = createButton(confirm, "Confirm Card");
	const returnCardButton = createButton(returnCard, "Return Card & Take From Deck")
	
	const controllers = document.getElementById("controllers");
		  controllers.appendChild(confirmButton);
		  controllers.appendChild(returnCardButton);

    handElement.classList.add("droppable");
    createDragOverListener(handElement);

    for(let slot = 1; slot <= 6; slot++) {
        if(layDownPlayers[slot]) enableDropping(slot);
    }
}

function confirm() {

}

function returnCard() {

}