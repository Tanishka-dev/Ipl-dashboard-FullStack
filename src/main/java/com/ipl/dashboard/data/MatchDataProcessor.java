package com.ipl.dashboard.data;

import com.ipl.dashboard.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MatchDataProcessor implements ItemProcessor<MatchInput, Match> {

    private static final Logger log = LoggerFactory.getLogger(MatchDataProcessor.class);

    @Override
    public Match process(final MatchInput matchInput) throws Exception {

        Match match= new Match();
        match.setMatchId(matchInput.getMatchId());
        match.setSeason(matchInput.getSeason());
        match.setDate(LocalDateTime.parse(matchInput.getDate()));
        match.setCity(matchInput.getCity());
        match.setVenue(matchInput.getVenue());
        match.setTossWinner(matchInput.getToss_winner());
        match.setTossDecision(matchInput.getToss_decision());
        match.setPlayerOfMatch(matchInput.getPlayer_of_match());
        match.setMatchWinner(matchInput.getWinner());
        match.setMatchNumber(matchInput.getMatch_number());
        match.setUmpire1(matchInput.getUmpire1());
        match.setUmpire2(matchInput.getUmpire2());
        match.setReserveUmpire(matchInput.getReserve_umpire());
        match.setWinnerWickets(matchInput.getWinner_wickets());
        match.setWinnerRuns(matchInput.getWinner_runs());

        //set team1 and team2 depending on innings order
        String firstInningsTeam, secondInningsTeam;
        if("bat".equals(matchInput.getToss_decision())){
            firstInningsTeam = matchInput.getToss_winner();
            secondInningsTeam= matchInput.getToss_winner().equals(matchInput.getTeam1())
                    ? matchInput.getTeam2()
                    : matchInput.getTeam1();
        }else{
            secondInningsTeam= matchInput.getToss_winner();
            firstInningsTeam= matchInput.getToss_winner().equals(matchInput.getTeam1())
                    ? matchInput.getTeam2()
                    : matchInput.getTeam1();
        }
        match.setTeam1(firstInningsTeam);
        match.setTeam2(secondInningsTeam);
        return match;

    }
}