function onLayDown(serverResponse) {
    const json = JSON.parse(serverResponse.body);

    const playerSlot = json.playerSlot;

    if(playerSlot == currentSlot) return;

    const sequences = json.sequences;

    for(let seq = 1 ; seq <= 4; seq++) {
        const sequenceElement = document.querySelector(`[data-slot="${playerSlot}"][data-sequence="${seq}"]`);
        sequenceElement.innerHTML = "";

        sequences[seq].forEach(cardDto => {
            const card = createCard(cardDto, sequenceElement.id[1]);

            sequenceElement.appendChild(card);
        });
    }
}

function onLayDownSlotSubscription(serverResponse) {
    const layDownPlayers = JSON.parse(serverResponse.body);

    for(let slot = 1; slot <= 6; slot++) {
        if(slot === currentSlot) continue;

        if(layDownPlayers[slot]) enableDropping(slot);
    }
    removeAllControllersButton();
}