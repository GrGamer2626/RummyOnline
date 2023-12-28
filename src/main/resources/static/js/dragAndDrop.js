let draggables = document.querySelectorAll('.draggable');
let sequences = document.querySelectorAll('.droppable');


draggables.forEach(draggable => {
	createDragStartListener(draggable);
	createDragEndListener(draggable);
});

sequences.forEach(sequence => {
	createDragOverListener(sequence);
});

document.addEventListener("playerTakeCard", event => {
	let draggable = event.card;
	createDragStartListener(draggable);
	createDragEndListener(draggable);
});

document.addEventListener("playerThrowCard", event => {
	let thrownCard = event.card;
	removeDragStartListener(thrownCard);
	removeDragEndListener(thrownCard);
});
/** Drag Over **/
function createDragOverListener(sequence) {
	sequences = document.querySelectorAll('.droppable');
	sequence.addEventListener("dragover", handleDragOver);
}

function removeAllDragOverListener() {
	sequences = document.querySelectorAll('.droppable');
	sequences.forEach(sequence=> {
		sequence.removeEventListener("dragover", handleDragOver);
	});
}

function removeDragOverListener(droppable) {
	droppable.removeEventListener("dragover", handleDragOver);
}

function handleDragOver(event) {
	event.preventDefault();
}

/** Drag Start Event **/
function createDragStartListener(draggable) {
	draggable.addEventListener("dragstart", handleDragStart);
}

function removeDragStartListener(draggable) {
	draggable.removeEventListener("dragstart", handleDragStart);
}

function handleDragStart(event) {
	let draggable = event.target;
	draggable.classList.add("dragging");
	draggable.setAttribute("data-source-sequence", draggable.closest('.droppable, .hand').id);
}

/** Drag End Event **/
function createDragEndListener(draggable) {
	draggable.addEventListener("dragend", handleDragEnd);
}

function removeDragEndListener(draggable) {
	draggable.removeEventListener("dragend", handleDragEnd);
}

function handleDragEnd(event) {
	let dragged = event.target;
	sequences.forEach(sequence => {
		let sequenceRect = sequence.getBoundingClientRect();
		if(checkX(sequenceRect, event.clientX) && checkY(sequenceRect, event.clientY)) {
			callCardDropEvent(dragged, sequence);
		}
	});
	dragged.classList.remove("dragging");
}


/** Util **/
function callCardDropEvent(dragged, sequence) {
	const event = new Event("cardDroped");
		  event.cardId = dragged.getAttribute('data-id');
		  event.sourceId = dragged.getAttribute('data-source-sequence');
		  event.destinationId = sequence.id;

	
	document.dispatchEvent(event);
}

function checkX(sequenceRect, x) {
	const boxMinX = sequenceRect.left;
	const boxMaxX = sequenceRect.right;

	return (x >= boxMinX) && (x <= boxMaxX);
}

function checkY(sequenceRect, y) {
	const boxMinY = sequenceRect.top;
	const boxMaxY = sequenceRect.bottom;

	return (y >= boxMinY) && (y <= boxMaxY);
}

function setOrientation(draggingClassList, sequence) {
	const classList = ["normal", "left-side", "right-side", "upside-down"];
	classList.forEach(clazz => {
		if(draggingClassList.contains(clazz)) {
			draggingClassList.remove(clazz);
		}
	});
	sequence.classList.forEach(clazz => {
		switch(clazz) {
			case "sequence-normal":
				draggingClassList.add("normal");
				break;

			case "sequence-left-side":
				draggingClassList.add("left-side");
				break;

			case "sequence-upside-down":
				draggingClassList.add("upside-down");
				break;

			case "sequence-right-side":
				draggingClassList.add("right-side");
				break;
		}
	});
}