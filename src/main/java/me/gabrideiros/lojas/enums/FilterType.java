package me.gabrideiros.lojas.enums;

public enum FilterType {

    VISITS("Mais visitadas"),
    BEST("Mais votadas"),
    RECENT("Criadas recentemente");

    private final String type;

    private FilterType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public FilterType next() {

        if (this == RECENT) return values()[0];

        return values()[ordinal() + 1];
    }

}
