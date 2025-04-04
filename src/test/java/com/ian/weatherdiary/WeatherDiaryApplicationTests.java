package com.ian.weatherdiary;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherDiaryApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void equalsTest() {
        // given
        assertEquals(1, 1);
    }

    @Test
    void nullTest() {
        assertNull(null);
    }

    @Test
    void trueTest() {
        assertTrue(true);
    }


}
