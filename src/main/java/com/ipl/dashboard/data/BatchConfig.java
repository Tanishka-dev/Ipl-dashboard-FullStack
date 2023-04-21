package com.ipl.dashboard.data;

import com.ipl.dashboard.model.Match;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
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
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

// Take the csv from reader, write it in the table and process it accrording to MatchDataProcessor and call listener to check status and log details. Print some data with it
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    private final String[] FIELD_NAMES = new String[]{
            "match_id","season","date","city","venue","team1","team2","toss_winner",
            "toss_decision","player_of_match","winner","winner_wickets","winner_runs",
            "outcome","result_type","results","gender","event","match_number","umpire1",
            "umpire2","reserve_umpire","tv_umpire","match_referee","eliminator","method","date_1"
    };


    @Bean
    public FlatFileItemReader<MatchInput> reader() {
        return new FlatFileItemReaderBuilder<MatchInput>()
                .name("MatchItemReader")
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
                .sql("INSERT INTO match (match_id,season,date,city,venue,team1,team2,toss_winner,toss_decision,player_of_match,match_winner,match_number,umpire1,umpire2,reserve_umpire,winner_wickets,winner_runs) VALUES (:matchId,:season,:date,:city,:venue,:team1,:team2,:tossWinner,:tossDecision,:playerOfMatch,:matchWinner,:matchNumber,:umpire1,:umpire2,:reserveUmpire,:winnerWickets,:winnerRuns)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory
                .get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Match> writer) {
        return stepBuilderFactory
                .get("step1")
                .<MatchInput, Match>
                        chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}