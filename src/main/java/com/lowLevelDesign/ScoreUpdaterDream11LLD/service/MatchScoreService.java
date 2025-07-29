package main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.service;

import main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.model.EventType;
import main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.model.MatchEvent;

public class MatchScoreService {
    private final UserTeamService userTeamService;
    private final LeaderboardService leaderboardService;

    public void handleMatchEvent(MatchEvent event) {
        int fantasyPoints = calculateFantasyPoints(event);
        userTeamService.updatePlayerPoints(event.getMatchId(), event.getPlayerId(), fantasyPoints);
        leaderboardService.updateLeaderboard(event.getMatchId());
    }

    private int calculateFantasyPoints(MatchEvent event) {
        // Custom rules
        if (event.getEventType() == EventType.RUN) return event.getValue();
        if (event.getEventType() == EventType.WICKET) return event.getValue() * 25;
        return 0;
    }
}
