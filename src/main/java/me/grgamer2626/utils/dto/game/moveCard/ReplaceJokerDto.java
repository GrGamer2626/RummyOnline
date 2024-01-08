package me.grgamer2626.utils.dto.game.moveCard;

import me.grgamer2626.utils.dto.game.CardDto;

public record ReplaceJokerDto(int playerSlot, int sourceSlot, int sourceNumber, CardDto cardDto, int jokerId) {


}
