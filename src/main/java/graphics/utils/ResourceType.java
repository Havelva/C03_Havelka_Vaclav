package graphics.utils;

public enum ResourceType {

    TrueTypeFont(".ttf"),
    Objects(".obj"),
    Materials(".mtl"),
    Icon(".png"),
    Other("");

    private final String type;

    ResourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
