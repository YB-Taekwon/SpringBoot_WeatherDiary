package com.ian.weatherdiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 트랜잭션 활성화 -> 보통 스프링 부트의 경우 자동으로 활성화 되어있기 때문에 생략 가능
@EnableScheduling // 스케쥴링 활성화
public class WeatherDiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherDiaryApplication.class, args);
    }

}
