package me.gabrideiros.lojas.enums;

public enum FilterType {

    VISITS("Mais visitadas"),
    BEST("Mais votadas"),
    RECENT("Criadas recentemente");

    private final String type;

    private FilterType(String type) {
        this.type = type;
    }

    public String getType(FilterType filter) {
        return type;
    }

}
