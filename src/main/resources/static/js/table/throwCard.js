function throwCard(cardId, sourceSlot, sourceNumber) {
	const headers = {}
	const message = JSON.stringify(
		{
			'cardId' : cardId,
			'playerSlot' : currentSlot,
			'sourceSlot' : sourceSlot,
			'sourceNumber' : sourceNumber
		});

	client.send(`/app/rummy/${tableId}/throwCard`, headers, message);
}

function onThrowCard(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {playerSlot, layDown, cardDto, sourceNumber, sourceSlot } = json;

	client.send(`/app/rummy/${tableId}/endGame`, {}, currentSlot);

	if(currentSlot === playerSlot) return;

	const source = getGameCollection(sourceNumber, sourceSlot);
	const thrownCardElement = document.getElementById("thrown-card-container");
		  thrownCardElement.innerHTML = "";

	if(isHand(sourceNumber) || !layDown) {
		const cards = source.children;
		source.removeChild(cards[0]);

		const card = createCard(cardDto, "1");

		thrownCardElement.appendChild(card);

	}else {
		const card = document.querySelector(`[data-id="${cardDto.id}"]`);

		if(source.contains(card)) source.removeChild(card);

		thrownCardElement.appendChild(card);
	}
}

function onThrowCardSlotSubscription(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {cardId, sourceNumber, sourceSlot} = json;

	const source = getGameCollection(sourceNumber, sourceSlot);
	const card = document.querySelector(`[data-id="${cardId}"]`);
	card.classList.remove("draggable");
	card.removeAttribute("draggable");

	if(source.contains(card)) source.removeChild(card);

	const thrownCardElement = document.getElementById("thrown-card-container");
	thrownCardElement.innerHTML = "";
	thrownCardElement.appendChild(card);

	removeAllControllersButton();
}