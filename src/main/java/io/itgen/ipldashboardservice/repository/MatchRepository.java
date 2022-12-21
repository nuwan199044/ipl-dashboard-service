package io.itgen.ipldashboardservice.repository;

import io.itgen.ipldashboardservice.model.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> findByTeam1OrTeam2OrderByDateDesc(String teamName1, String teamName2, Pageable pageable);

    @Query("SELECT m from Match m WHERE (m.team1=:teamName OR m.team2=:teamName) AND m.date between :fromDate and :toDate ORDER BY m.date DESC ")
    List<Match> findMatchesForTeamByYear(@Param("teamName") String teamName, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    default List<Match> findLatestMatchesByTeam (String teamName, int count) {
        return findByTeam1OrTeam2OrderByDateDesc(teamName, teamName, PageRequest.of(0, count));
    }
}
