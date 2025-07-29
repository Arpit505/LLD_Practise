package main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.repository;

public interface LeaderboardRepository {
    void saveLeaderboard(String matchId, List<LeaderboardEntry> leaderboard);
    List<LeaderboardEntry> getLeaderboard(String matchId);
}

public class InMemoryLeaderboardRepository implements LeaderboardRepository {
    private final Map<String, List<LeaderboardEntry>> store = new ConcurrentHashMap<>();

    public void saveLeaderboard(String matchId, List<LeaderboardEntry> leaderboard) {
        store.put(matchId, leaderboard);
    }

    public List<LeaderboardEntry> getLeaderboard(String matchId) {
        return store.getOrDefault(matchId, new ArrayList<>());
    }
}

