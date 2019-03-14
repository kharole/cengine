package se.tain.casino.common.limit;

import static java.lang.String.format;

public enum LimitType {
    WAGERS( 0 ), LOSSES( 1 ), PLAY_TIME( 2 );

    private int id;

    private LimitType( int id ) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static LimitType findById( int id ) {
        for ( LimitType type : values() ) {
            if ( type.id == id ) {
                return type;
            }
        }
        throw new IllegalArgumentException( format( "Cannot find limit type by id=%d", id ) );
    }

    public String getName() {
        return name();
    }
}
