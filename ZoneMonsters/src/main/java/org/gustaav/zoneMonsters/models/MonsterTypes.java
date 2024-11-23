package org.gustaav.zoneMonsters.models;

import lombok.Getter;

@Getter
public enum MonsterTypes {

    NORMAL("NORMAL", "<#59AA58>"),
    RARO("RARO", "<#555DAA>"),
    MISTICO("MISTICO", "<#950FAA>"),
    LENDARIO("LENDARIO", "<#F6A600>");

    private final String name;
    private final String colorCode;

    MonsterTypes(String name, String colorCode) {
        this.name = name;
        this.colorCode = colorCode;
    }


}
