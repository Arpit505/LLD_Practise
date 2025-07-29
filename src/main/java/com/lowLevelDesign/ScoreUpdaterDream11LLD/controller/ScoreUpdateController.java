package main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.controller;

import main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.model.MatchEvent;
import main.java.com.lowLevelDesign.ScoreUpdaterDream11LLD.service.MatchScoreService;

//@RestController
//@RequestMapping("/api/v1/score")
public class ScoreUpdateController {
    private final MatchScoreService matchScoreService;

//    @Autowired
    public ScoreUpdateController(MatchScoreService matchScoreService) {
        this.matchScoreService = matchScoreService;
    }

//    @PostMapping("/event")
    public ResponseEntity<String> updateScore(@RequestBody MatchEvent event) {
        matchScoreService.handleMatchEvent(event);
        return ResponseEntity.ok("Event processed successfully");
    }
}
