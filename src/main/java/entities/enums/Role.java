package entities.enums;

public enum Role {
    MEMBRE,
    PROPRIETAIRE,
    ADMIN;

    @Override
    public String toString() {
        return name();
    }
}
