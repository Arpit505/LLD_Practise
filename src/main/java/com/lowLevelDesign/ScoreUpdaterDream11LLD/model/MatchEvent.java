package main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.model;

public class MatchEvent {
    private String matchId;
    private String playerId;
    private EventType eventType; // e.g., RUN, WICKET
    private int value;

    MatchEvent(String mid, String pid, int val) {
        this.matchId = mid;
        this.playerId = pid;
        this.value = val;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getMatchId() {
        return matchId;
    }

    public int getValue() {
        return value;
    }
}
