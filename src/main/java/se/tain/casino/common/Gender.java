package se.tain.casino.common;

@SuppressWarnings("serial")
public enum Gender {
    MALE, FEMALE;

    public int getId() {
        return ordinal();
    }
    
    public String getName() {
        return name();
    }

    public static Gender fromId( int id) {
        if (id == MALE.ordinal()) {
            return MALE;
        } else if (id == FEMALE.ordinal()) {
            return FEMALE;
        }
        throw new IllegalArgumentException("No gender for given id " + id);
    }
}
