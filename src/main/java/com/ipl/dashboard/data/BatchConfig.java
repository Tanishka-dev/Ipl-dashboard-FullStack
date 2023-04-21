package com.ipl.dashboard.data;

import com.ipl.dashboard.model.Match;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private final String[] FIELD_NAMES = new String[]{
            "match_id","season","date","city","venue","team1","team2","toss_winner",
            "toss_decision","player_of_match","winner","winner_wickets","winner_runs",
            "outcome","result_type","results","gender","event","match_number","umpire1",
            "umpire2","reserve_umpire","tv_umpire","match_referee","eliminator","method","date_1"
    };

    @Autowired
    public JobBuilder jobBuilder;

    @Autowired
    public StepBuilder stepBuilder;

    @Bean
    public FlatFileItemReader<MatchInput> reader() {
        return new FlatFileItemReaderBuilder<MatchInput>()
                .name("personItemReader")
                .resource(new ClassPathResource("match-data.csv"))
                .delimited()
                .names(FIELD_NAMES)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<MatchInput>() {{
                    setTargetType(MatchInput.class);
                }})
                .build();
    }

    @Bean
    public MatchDataProcessor processor() {
        return new MatchDataProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Match>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO match (match_id,season,date,city,venue,team1,team2,toss_winner,toss_decision,player_of_match,match_winner,match_number,umpire1,umpire2,reserve_umpire,winner_wickets,winner_runs)"+
                        "VALUES(:matchId,:season,:date,:city,:venue,:team1,:team2,:tossWinner,:tossDecision,:playerOfMatch,:matchWinner,:matchNumber,:umpire1,:umpire2,:reserveUmpire,:winnerWickets,:winnerRuns)")
                .dataSource(dataSource)
                .build();
    }
}