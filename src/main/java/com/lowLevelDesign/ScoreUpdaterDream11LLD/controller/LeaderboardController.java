package main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.controller;

import main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.service.LeaderboardService;

//@RestController
//@RequestMapping("/api/v1/leaderboard")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

//    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

//    @GetMapping("/{matchId}")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard( String matchId) {
        List<LeaderboardEntry> leaderboard = leaderboardService.getLeaderboard(matchId);
        return ResponseEntity.ok(leaderboard);
    }
}
