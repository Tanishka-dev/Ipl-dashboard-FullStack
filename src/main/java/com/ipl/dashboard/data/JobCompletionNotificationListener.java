package com.ipl.dashboard.data;
import com.ipl.dashboard.model.Team;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager em; //jpa

    @Autowired
    public JobCompletionNotificationListener(EntityManager em) {
        this.em = em;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            Map<String, Team> teamNameData = new HashMap();

            //select distinct team1 from Match m and union select distinct team2 from match n
            //in jpa union is not allowed
            //hence do twice

            //distinct teams with there total matches in object array with name and number
             em.createQuery("select m.team1, count(*) FROM Match m group by m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e-> new Team((String) e[0], (long) e[1]))
                     .forEach(team -> teamNameData.put(team.getTeamName(), team));

             em.createQuery("select m.team2, count(*) FROM Match m group by m.team2", Object[].class)
                     .getResultList()
                     .stream()
                     .forEach(e -> {
                         Team team =teamNameData.get((String) e[0]);
                         if(team != null) {
                             team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
                         }
                     });

             //find totalwins by team by name
            em.createQuery("select m.matchWinner, count(*) FROM Match m group by m.matchWinner", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team team =teamNameData.get((String) e[0]);
                       if(team != null){
                           team.setTotalWins((long) e[1]);
                       }
                    });

            //persist the data
            teamNameData.values().forEach(team -> em.persist(team));
            teamNameData.values().forEach(team -> System.out.println(team));
        }
    }
}