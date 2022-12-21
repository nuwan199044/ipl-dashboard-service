package io.itgen.ipldashboardservice.controller;

import io.itgen.ipldashboardservice.model.Match;
import io.itgen.ipldashboardservice.model.Team;
import io.itgen.ipldashboardservice.repository.MatchRepository;
import io.itgen.ipldashboardservice.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
public class TeamController {

    @Autowired
    TeamRepository temaRepository;

    @Autowired
    MatchRepository matchRepository;

    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName) {
        Team team = temaRepository.findByTeamName(teamName);
        team.setMatches(matchRepository.findLatestMatchesByTeam(teamName, 4));
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesForTeam(@PathVariable String teamName, @RequestParam int year) {
        LocalDate fromDate = LocalDate.of(year, 1, 1);
        LocalDate toDate = LocalDate.of(year+1, 1, 1);
        return matchRepository.findMatchesForTeamByYear(teamName, fromDate, toDate);
    }
}
