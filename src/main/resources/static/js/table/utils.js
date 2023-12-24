/************ Cards ************/
function createCard(card, fieldId) {
	let cardElement = document.createElement("img");
		cardElement.classList.add("card", getOrientationClass(fieldId));
		cardElement.alt = "";
		cardElement.src = card.imgPath;
		cardElement.setAttribute("data-id", card.id);

	return cardElement;
}

function createBlankCard(fieldId) {
	let cardElement = document.createElement("img");
		cardElement.classList.add("card", getOrientationClass(fieldId));
		cardElement.alt = "";
		cardElement.src = "/img/deck/back.png";

	return cardElement;
}

function getOrientationClass(fieldId) {
	switch (fieldId) {
		case "1": case "2":
			return "normal";

		case "3":
			return "left-side";

		case "4": case "5":
			return "upside-down";

		case "6":
			return "right-side";

		default:
			return "";
	}
}

/************ Buttons ************/
function createButton(onclickAction, innerHTML) {
	let button = document.createElement("button");
		button.className = "btn";
		button.innerHTML = innerHTML;
		button.onclick = () => onclickAction();

	return button;
}

function createLeaveSlotButton(slotNumber) {
	let leaveButton = document.createElement("button");
		leaveButton.id = `leave-slot-${slotNumber}-btn`;
		leaveButton.className = "btn square-btn";
		leaveButton.innerHTML = "X";
		leaveButton.onclick = () => leaveSlot(slotNumber);

	return leaveButton;
}

/************ Player Slots ************/
function createTakenSlot(slotId, playerName) {
	let slotElement = document.getElementById(slotId);
	//Remove button
	let buttonElement = slotElement.querySelector(".slot-btn");
	slotElement.removeChild(buttonElement);

	//Create div taken slot
	let takenSlot = document.createElement("div");
	takenSlot.className = "taken-slot";

	//Create display player name
	let playerNameElement = document.createElement("h5");
	playerNameElement.textContent = playerName;

	//Add elements to div
	takenSlot.appendChild(playerNameElement);
	slotElement.appendChild(takenSlot);

	return slotElement;
}

function addLeaveSlotButton(slotElement, slotNumber) {
	let takenSlot = slotElement.querySelector(".taken-slot");
	let leaveButton = createLeaveSlotButton(slotNumber);

	takenSlot.appendChild(leaveButton);
}

function createEmptySlot(slotId, action) {
	let slotElement = document.getElementById(slotId);

	//Remove div taken slot
	let takenSlot = slotElement.querySelector(".taken-slot");
	slotElement.removeChild(takenSlot);

	//Add div button
	let buttonElement = document.createElement("button");
		buttonElement.className = "slot-btn";
		buttonElement.textContent = "Empty";
		buttonElement.onclick = action;

	slotElement.appendChild(buttonElement);
}

/************ Drag and Drop ************/
function enableDragging() {
	let handElement = document.querySelector(`.hand[data-slot="${currentSlot}"]`);

	handElement.querySelectorAll("*").forEach(card => {
		card.classList.add("draggable");
		card.draggable = true;

		createDragStartListener(card);
		createDragEndListener(card);
	});
}

function enableDropping(slot) {
	let sequenceElement = document.querySelectorAll(`.sequence[data-slot="${slot}"]`);

	sequenceElement.forEach(sequence => {
		sequence.classList.add("droppable");
		createDragOverListener(sequence);
	});
}

function disableDragging() {
	let draggable = document.querySelectorAll(".draggable");

	draggable.forEach(card => {
		card.classList.remove("draggable");
		card.removeAttribute("draggable");

		removeDragStartListener(card);
		removeDragEndListener(card);
	});

}

function disableDropping() {
	let sequenceElements = document.querySelector(".sequence");

	sequenceElements.forEach(sequence => {
		sequence.classList.remove("droppable");
		removeDragOverListener(sequence);
	});
}

/************ Displayng ************/
function setPlayerSlots(playerField, slotNumber) {
	let dataSlotElements = document.querySelectorAll(`#player-${playerField} [data-slot]`);
	let dataSlotValue = ((playerField - 1 + slotNumber - 1) % 6) + 1;

	dataSlotElements.forEach(function(element) {
		element.setAttribute('data-slot', dataSlotValue);
	});
}

function setWaitPopupContent(content) {
	let contentElement = document.querySelector("#waiting h5");
	contentElement.textContent = content;
}

function removeAllControllersButton() {
	let controllers = document.getElementById("controllers");
	controllers.innerHTML = "";
}

/************ Data Type ************/
function parseBoolean(input) {
	let boolean = input.trim().toLowerCase() === "true";
	return boolean;
}

function getHand(slot) {
	return document.querySelector(`.hand[data-slot="${slot}"]`);
}

function getSequenceBySlotAndNumber(slot, number) {
	return document.querySelectorAll(`.sequence[data-slot="${slot}"][data-sequence="${number}"]`)
}

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