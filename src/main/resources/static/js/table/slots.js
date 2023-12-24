function onTakeSlotGeneral(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const {slot, playerName} = json

	if(slot === currentSlot) return;

	let slotId = `player-slot-${slot}`;
	createTakenSlot(slotId, playerName);
}

function onLeaveSlot(serverResponse) {
	const json = JSON.parse(serverResponse.body);

	const slotNumber = json.slot;
	const slotId = `player-slot-${slotNumber}`;

	createEmptySlot(slotId, ()=> takeSlot(slotNumber));

	if(currentSlot === slotNumber) {
		unsubscirbeAll();

		for(let i = 1; i <= 6; i++) {
			setPlayerSlots(i, 1);
		}
		currentSlot = -1;
	}
}

function unsubscirbeAll() {
	startGameSlotSub.unsubscribe();
	yourTurn.unsubscribe();

	takeFromDeckSlotSub.unsubscribe();
	takeFromStackSlotSub.unsubscribe();

	moveCardSlotSub.unsubscribe();
	replaceJokerSlotSub.unsubscribe();
	throwCardSlotSub.unsubscribe();

	layDownSlotSub.unsubscribe();
}