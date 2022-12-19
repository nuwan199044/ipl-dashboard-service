package io.itgen.ipldashboardservice.data;

import io.itgen.ipldashboardservice.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import java.time.LocalDate;


public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) throws Exception {

        Match match = new Match();
        match.setId(Long.parseLong(matchInput.getId()));
        match.setCity(matchInput.getCity());

        match.setDate(LocalDate.parse(matchInput.getDate()));
        match.setPlayerOfMatch(matchInput.getPlayer_of_match());
        match.setVenue(matchInput.getVenue());

        String firstInnings = null;
        String secondInnings = null;

        if (matchInput.getToss_winner().equals(matchInput.getTeam1())) {
            if(matchInput.getToss_decision().equals("bat")){
                firstInnings = matchInput.getTeam1();
                secondInnings = matchInput.getTeam2();
            } else {
                firstInnings = matchInput.getTeam2();
                secondInnings = matchInput.getTeam1();
            }
        } else {
            if(matchInput.getToss_decision().equals("bat")){
                firstInnings = matchInput.getTeam2();
                secondInnings = matchInput.getTeam1();
            } else {
                firstInnings = matchInput.getTeam1();
                secondInnings = matchInput.getTeam2();
            }
        }

        match.setTeam1(firstInnings);
        match.setTeam2(secondInnings);
        match.setTossWinner(matchInput.getToss_winner());
        match.setTossDecision(matchInput.getToss_decision());
        match.setWinner(matchInput.getWinner());
        match.setResult(matchInput.getResult());
        match.setResultMargin(matchInput.getResult_margin());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());

        return match;
    }

}
