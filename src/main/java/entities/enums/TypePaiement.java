package entities.enums;

public enum TypePaiement {
    JOUR,
    MOIS;



    public String toString() {
        return switch (this) {
            case JOUR -> "Payment by day";
            case MOIS -> "Payment by month";
            default -> throw new IllegalArgumentException();
        };

    }
    }