package main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.service;

public class LeaderboardService {
    private final UserTeamService userTeamService;
    private final LeaderboardRepository leaderboardRepository;

    public void updateLeaderboard(String matchId) {
        Map<String, Integer> userScores = userTeamService.getScoresForMatch(matchId);

        List<LeaderboardEntry> leaderboard = userScores.entrySet().stream()
                .map(e -> new LeaderboardEntry(e.getKey(), e.getValue()))
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .collect(Collectors.toList());

        leaderboardRepository.saveLeaderboard(matchId, leaderboard);
    }

    public List<LeaderboardEntry> getLeaderboard(String matchId) {
        return leaderboardRepository.getLeaderboard(matchId);
    }
}
