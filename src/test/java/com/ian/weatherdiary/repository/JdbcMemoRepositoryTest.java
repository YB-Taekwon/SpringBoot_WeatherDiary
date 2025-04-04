package com.ian.weatherdiary.repository;

import com.ian.weatherdiary.domain.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JdbcMemoRepositoryTest {

    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertAndSelectTest() {
        // given
        Memo memo = new Memo(1, "test text");
        // when
        jdbcMemoRepository.save(memo);
        String text = jdbcMemoRepository.findById(1).get().getText();
        // then
        assertEquals("test text", text);
    }

    @Test
    void selectTest() {
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println("memoList = " + memoList);
        assertNotNull(memoList);
    }
}