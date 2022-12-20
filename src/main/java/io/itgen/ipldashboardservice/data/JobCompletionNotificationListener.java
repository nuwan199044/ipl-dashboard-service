package io.itgen.ipldashboardservice.data;

import io.itgen.ipldashboardservice.model.Match;
import io.itgen.ipldashboardservice.model.Team;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager entityManager;

    @Autowired
    public JobCompletionNotificationListener(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teamData = new HashMap<>();

            entityManager.createQuery("SELECT m.team1, count(*) from Match m group by m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> new Team((String)e[0], (long)e[1]))
                    .forEach(team -> teamData.put(team.getTeamName(), team));

            entityManager.createQuery("SELECT m.team2, count(*) from Match m group by m.team2", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> {
                        Team t = teamData.get((String) e[0]);
                        if (t != null) {
                            t.setTotalMatches(t.getTotalMatches() + (long) e[1]);
                        }
                        return t;
                    })
                    .forEach(t -> teamData.put(t.getTeamName(), t));

            entityManager.createQuery("SELECT m.winner, count(*) from Match m group by m.winner", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team t = teamData.get((String) e[0]);
                        if (t != null) {
                            t.setTotalWins((long) e[1]);
                        }
                    });

            teamData.values().forEach(team -> entityManager.persist(team));
            teamData.values().forEach(team -> System.out.println(team));
        }
    }
}
