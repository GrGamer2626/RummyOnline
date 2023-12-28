function start() {
	const headers = {}
	client.send(`/app/rummy/table/${tableId}/startGameCountDown`, headers, currentSlot);
}

function onStartGame(serverResponse) {
	const json = JSON.parse(serverResponse.body);
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

function onStartGameSlotSubscription(serverResponse) {
	const onHand = JSON.parse(serverResponse.body);
	const handElement = document.querySelector(`.hand[data-slot="${currentSlot}"]`);
	handElement.innerHTML = "";

	removeAllControllersButton();

	onHand.forEach(cardData => {
		const cardElement = createCard(cardData, "1");
		handElement.appendChild(cardElement);
	});

	const sequences = document.querySelectorAll(".sequence");
	sequences.forEach(sequence => {
		sequence.innerHTML = "";
	});

	const thrownCardElement = document.getElementById("thrown-card-container");
		  thrownCardElement.innerHTML = "";
}

function onEndGame(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {winnerName} = json;

	const classList = document.getElementById("waiting").classList;
		  classList.add("popup-visible");
		  setWaitPopupContent(`Playre ${winnerName} won!`)
}

function onEndGameSlotSubscription(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const startButton = createButton(start, "Start")

	const cards = document.querySelectorAll(".card");
	cards.forEach(card => {
		removeDragStartListener(card);
		removeDragEndListener(card);
		removeAllDragOverListener();

		const cardClassList = card.classList;
		cardClassList.remove("draggable");
	});

	const controllers = document.getElementById("controllers");
		  controllers.innerHTML = "";
		  controllers.appendChild(startButton);

}