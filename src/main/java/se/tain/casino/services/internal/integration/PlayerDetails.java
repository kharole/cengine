package se.tain.casino.services.internal.integration;

import se.tain.casino.common.Country;
import se.tain.casino.common.Gender;

import java.io.Serializable;
import java.util.Date;

public class PlayerDetails implements Serializable {

    private static final long serialVersionUID = -4840198696350937345L;

    private int playerId;
    private String playerName;
    private String noConflictPlayerName;
    private String currency;
    private Country country;
    private String sessionKey;
    private String token;
    private Date birthDate;
    private Date creationDate;
    private Gender gender;
    private boolean active;
    private String displayName;
    private String ipAddress;
    private Integer vipLevel;

    public PlayerDetails( int playerId, String playerName, String currency, Boolean active ) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.currency = currency;
        this.active = active;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId( int playerId ) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName( String playerName ) {
        this.playerName = playerName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency( String currency ) {
        this.currency = currency;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry( Country country ) {
        this.country = country;
    }

    public void setSessionKey( String sessionKey ) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setToken( String token ) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate( Date birthDate ) {
        this.birthDate = birthDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate( Date creationDate ) {
        this.creationDate = creationDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender( Gender gender ) {
        this.gender = gender;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive( boolean active ) {
        this.active = active;
    }

    public String getNoConflictPlayerName() {
        return noConflictPlayerName;
    }

    public void setNoConflictPlayerName( String noConflictPlayerName ) {
        this.noConflictPlayerName = noConflictPlayerName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName( String displayName ) {
        this.displayName = displayName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress( String ipAddress ) {
        this.ipAddress = ipAddress;
    }

    public Integer getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel( Integer vipLevel ) {
        this.vipLevel = vipLevel;
    }

    @Override
    public String toString() {
        return "PlayerDetails{" +
                "playerId=" + playerId +
                ", playerName='" + playerName + '\'' +
                ", noConflictPlayerName='" + noConflictPlayerName + '\'' +
                ", currency='" + currency + '\'' +
                ", country=" + country +
                ", sessionKey='" + sessionKey + '\'' +
                ", token='" + token + '\'' +
                ", birthDate=" + birthDate +
                ", creationDate=" + creationDate +
                ", gender=" + gender +
                ", active=" + active +
                ", displayName='" + displayName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", vipLevel=" + vipLevel +
                '}';
    }
}
