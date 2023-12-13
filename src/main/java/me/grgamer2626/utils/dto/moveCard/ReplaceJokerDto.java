package me.grgamer2626.utils.dto.moveCard;

import me.grgamer2626.utils.dto.CardDto;

public record ReplaceJokerDto(int playerSlot, int sourceSlot, int sourceNumber, CardDto cardDto, int jokerId) {


}
