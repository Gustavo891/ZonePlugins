package org.gustaav.zoneSkills.skills;

public enum SkillType {

    mining("Mineração"),
    combat("Combate"),
    acrobatics("Acrobacia"),
    farming("Fazendeiro");

    private final String displayName;

    SkillType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}