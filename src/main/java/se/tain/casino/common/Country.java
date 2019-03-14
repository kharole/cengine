package se.tain.casino.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.Objects;

@XmlAccessorType( XmlAccessType.PROPERTY )
public class Country implements Serializable, Comparable<Country> {

    private static final long serialVersionUID = -7974052288964129906L;

    public static final Country UNDEFINED = new Country( "un", "und", "Undefined" );

    private String id;
    private String alpha3Code;
    private String description;

    public Country( String id, String alpha3Code, String description ) {
        this.id = id;
        this.description = description;
        this.alpha3Code = alpha3Code;
    }

    public Country() {
    }


    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getAlpha3Code() {
        return alpha3Code;
    }

    public String getAlpha2Code() {
        return id;
    }

    public int compareTo( Country c ) {
        return getId().compareTo( c.getId() );
    }


    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !( o instanceof Country ) ) return false;
        Country country = ( Country ) o;
        return Objects.equals( id, country.id );
    }

    @Override
    public int hashCode() {
        return Objects.hash( id );
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder( "Country{" );
        sb.append( "id='" ).append( id ).append( '\'' );
        sb.append( ", alpha3Code='" ).append( alpha3Code ).append( '\'' );
        sb.append( ", description='" ).append( description ).append( '\'' );
        sb.append( '}' );
        return sb.toString();
    }
}

