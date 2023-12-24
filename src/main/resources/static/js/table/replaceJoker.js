function onReplaceJoker(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {playerSlot, sourceSlot, sourceNumber, cardDto, jokerId} = json;

	if(currentSlot === playerSlot) return;

	const cardId = cardDto.id;

	const joker = document.querySelector(`[data-id="${jokerId}"]`);

	if(!isHand(sourceNumber)) {
		const source = document.querySelector(`.sequence[data-slot="${sourceSlot}"][data-sequence="${sourceNumber}"]`);
		const card = source.querySelector(`[data-id="${cardId}"]`);
		if(source.contains(card)) source.removeChild(card);

		const handElement = document.querySelector(`.hand[data-slot="${playerSlot}"]`);
		handElement.appendChild(createBlankCard(handElement.id[11]));
	}
	joker.src = cardDto.imgPath;
	joker.setAttribute("data-id", cardId);
}

function onReplaceJokerSlotSubscription(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {playerSlot, sourceSlot, sourceNumber, cardDto, jokerId} = json;
	const cardId = cardDto.id;

	const joker = document.querySelector(`[data-id="${jokerId}"]`);
	const jokerDto =
	{
		"id" : jokerId,
		"imgPath" : joker.src
	};

	if(!isHand(sourceNumber)) {
		const source = document.querySelector(`.sequence[data-slot="${sourceSlot}"][data-sequence="${sourceNumber}"]`);
		const card = source.querySelector(`[data-id="${cardId}"]`);
		if(source.contains(card)) source.removeChild(card);

		const handElement = document.querySelector(`.hand[data-slot="${playerSlot}"]`);
		handElement.appendChild(createCard(jokerDto, "1"));
	}
	const source = document.querySelector(`.hand[data-slot="${sourceSlot}"]`);
	const card = source.querySelector(`[data-id="${cardId}"]`);

	card.src= jokerDto.imgPath;
	card.setAttribute("data-id", jokerId);

	joker.src = cardDto.imgPath;
	joker.setAttribute("data-id", cardId);
}