package com.ipl.dashboard.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Match {
    private long matchId;
    private String season;
    private LocalDateTime date;
    private String city;
    private String venue;
    private String team1;
    private String team2;
    private String tossWinner;
    private String tossDecision;
    private String playerOfMatch;
    private String matchWinner;
    private int matchNumber;
    private String umpire1;
    private String umpire2;
    private String reserveUmpire;
    private int winnerWickets;
    private int winnerRuns;

    public int getWinnerWickets() {
        return winnerWickets;
    }

    public void setWinnerWickets(int winnerWickets) {
        this.winnerWickets = winnerWickets;
    }

    public int getWinnerRuns() {
        return winnerRuns;
    }

    public void setWinnerRuns(int winnerRuns) {
        this.winnerRuns = winnerRuns;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long match_id) {
        this.matchId = match_id;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTossWinner() {
        return tossWinner;
    }

    public void setTossWinner(String tossWinner) {
        this.tossWinner = tossWinner;
    }

    public String getTossDecision() {
        return tossDecision;
    }

    public void setTossDecision(String tossDecision) {
        this.tossDecision = tossDecision;
    }

    public String getPlayerOfMatch() {
        return playerOfMatch;
    }

    public void setPlayerOfMatch(String playerOfMatch) {
        this.playerOfMatch = playerOfMatch;
    }

    public String getMatchWinner() {
        return matchWinner;
    }

    public void setMatchWinner(String matchWinner) {
        this.matchWinner = matchWinner;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(int matchNumber) {
        this.matchNumber = matchNumber;
    }

    public String getUmpire1() {
        return umpire1;
    }

    public void setUmpire1(String umpire1) {
        this.umpire1 = umpire1;
    }

    public String getUmpire2() {
        return umpire2;
    }

    public void setUmpire2(String umpire2) {
        this.umpire2 = umpire2;
    }

    public String getReserveUmpire() {
        return reserveUmpire;
    }

    public void setReserveUmpire(String reserveUmpire) {
        this.reserveUmpire = reserveUmpire;
    }
}
