package io.itgen.ipldashboardservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Match {

    @Id
    private long id;
    private String city;
    private LocalDate date;
    private String playerOfMatch;
    private String venue;
    private String team1;
    private String team2;
    private String tossWinner;
    private String tossDecision;
    private String winner;
    private String result;
    private String resultMargin;
    private String umpire1;
    private String umpire2;

    public Match(long id, String team1, String team2) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
    }
}
