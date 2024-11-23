package org.gustaav.zoneEnchants.enchantment;

public enum EnchantTypes {

    PICKAXE("Picareta"),
    AXE("Machado"),
    SWORD("Espada"),
    SHOVEL("Pá"),
    HOE("Enxada"),
    HELMET("Capacete"),
    CHESTPLATE("Peitoral"),
    LEGGINGS("Calça"),
    BOOTS("Bota");

    private final String value;

    // Construtor para atribuir valor ao campo
    EnchantTypes(String value) {
        this.value = value;
    }

    // Método para acessar o valor
    public String getValue() {
        return value;
    }
}
