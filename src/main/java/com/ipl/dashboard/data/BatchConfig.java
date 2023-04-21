package com.ipl.dashboard.data;

import com.ipl.dashboard.model.Match;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names(new String[]{"firstName", "lastName"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                    setTargetType(MatchInput.class);
                }})
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<MatchInput> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<MatchInput>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }
}
