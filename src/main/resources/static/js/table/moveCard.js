function onMoveCard(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	if(json.playerSlot === currentSlot) return;

	const { sourceNumber, sourceSlot, destinationNumber, destinationSlot } = json;
	const source = getGameCollection(sourceNumber, sourceSlot);
	const destination = getGameCollection(destinationNumber, destinationSlot);

	const  fieldId = getFieldId(destinationNumber, destination);

	if('cardDto' in json) {
		//Player is laydown
		const { cardDto, position } = json;
		const cardId = cardDto.id;

		//Move to Hand
		if(isHand(destinationNumber)) {
			moveToHandLayDown(fieldId, cardId, source, destination);
			return;
		}

		//Move to Sequence
		//From hand
		if(isHand(sourceNumber)) {
			moveHandToSequenceLayDown(fieldId, cardDto, position, source, destination);
			return;
		}
		//From Sequence
		moveSequenceToSequenceLayDown(fieldId, cardId, position, source, destination);
		return;
	}
	//Player isn't laydown
	moveCardStandard(fieldId, source, destination);
}

function onMoveCardSlotSubscription(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {cardId, position, sourceNumber, sourceSlot, destinationNumber, destinationSlot} = json;


	const source = getGameCollection(sourceNumber, sourceSlot);
	const destination = getGameCollection(destinationNumber, destinationSlot);

	const fieldId = getFieldId(destinationNumber, destination);

	moveSequenceToSequenceLayDown(fieldId, cardId, position, source, destination);
}


function moveToHandLayDown(fieldId, cardId, source, destination) {
	const blankCard = createBlankCard(fieldId);
	const card = source.querySelector(`[data-id="${cardId}"]`);

	destination.appendChild(blankCard);
	if(source.contains(card)) source.removeChild(card);
}

function moveHandToSequenceLayDown(fieldId, cardDto, position, source, destination) {
	const sourceCards = source.children;
	const destinationCards = destination.children;

	const card = createCard(cardDto, fieldId);

	destination.insertBefore(card, destinationCards[position]);
	source.removeChild(sourceCards[0]);
}

function moveSequenceToSequenceLayDown(fieldId, cardId, position, source, destination) {
	const destinationCards = destination.children;

	const card = source.querySelector(`[data-id="${cardId}"]`);
	removeAllOrientation(card);
	card.classList.add(getOrientationClass(fieldId));

	destination.insertBefore(card, destinationCards[position]);
	if(source.contains(card)) source.removeChild(card);
}

function moveCardStandard(fieldId, source, destination) {
	const card = source.children[0];
	removeAllOrientation(card);
	card.classList.add(getOrientationClass(fieldId));

	destination.appendChild(card);
	if(source.contains(card)) source.removeChild(card);
}