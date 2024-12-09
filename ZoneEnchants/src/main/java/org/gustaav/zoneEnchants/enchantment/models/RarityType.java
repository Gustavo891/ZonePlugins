package org.gustaav.zoneEnchants.enchantment.models;

import lombok.Getter;

@Getter
public enum RarityType {

    NORMAL("NORMAL", "<#59AA58>"),
    INCOMUM("INCOMUM", "<#81FF81>"),
    RARO("RARO", "<#555DAA>"),
    MISTICO("MISTICO", "<#950FAA>"),
    LENDARIO("LENDARIO", "<#F6A600>");

    private final String name;
    private final String colorCode;

    RarityType(String name, String colorCode) {
        this.name = name;
        this.colorCode = colorCode;
    }


}

