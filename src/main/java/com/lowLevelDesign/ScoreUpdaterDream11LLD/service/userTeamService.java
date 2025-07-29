package main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.service;

import main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.model.UserTeam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserTeamService {

    private final UserTeamRepository userTeamRepository;
    private final Map<String, Map<String, Integer>> userTeamScores = new ConcurrentHashMap<>();

    public void updatePlayerPoints(String matchId, String playerId, int points) {
        List<UserTeam> teams = userTeamRepository.findByMatchAndPlayer(matchId, playerId);

        for (UserTeam team : teams) {
            int multiplier = getMultiplier(playerId, team);
            int updatedPoints = points * multiplier;

            userTeamScores
                    .computeIfAbsent(matchId, k -> new ConcurrentHashMap<>())
                    .merge(team.getUserId(), updatedPoints, Integer::sum);
        }
    }

    private int getMultiplier(String playerId, UserTeam team) {
        if (playerId.equals(team.getCaptainId())) return 2;
        if (playerId.equals(team.getViceCaptainId())) return 15 / 10;
        return 1;
    }

    public int getUserScore(String matchId, String userId) {
        return userTeamScores.getOrDefault(matchId, new HashMap<>())
                .getOrDefault(userId, 0);
    }

    public Map<String, Integer> getScoresForMatch(String matchId) {
        return userTeamScores.getOrDefault(matchId, new HashMap<>());
    }
}

