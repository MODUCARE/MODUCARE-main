package world.moducare.domain.diary.entity;

public enum HeadType {
    TOP, LINE;

    public static HeadType getHeadType(String type) {
        return switch (type) {
            case "top" -> HeadType.TOP;
            case "line" -> HeadType.LINE;
            default -> null;
        };
    }
}
