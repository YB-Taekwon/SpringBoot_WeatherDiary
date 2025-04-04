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
class JpaMemoRepositoryTest {

    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertAndSelectTest() {
        // given
        Memo memo = Memo.builder().text("jpa test text").build();
        Memo saveMemo = jpaMemoRepository.save(memo);
        // when
        int id = saveMemo.getId();
        String text = jpaMemoRepository.findById(id).get().getText();
        // then
        assertEquals("jpa test text", text);
    }

    @Test
    void selectAllTest() {
        List<Memo> memoList = jpaMemoRepository.findAll();
        assertFalse(memoList.isEmpty());
    }
}