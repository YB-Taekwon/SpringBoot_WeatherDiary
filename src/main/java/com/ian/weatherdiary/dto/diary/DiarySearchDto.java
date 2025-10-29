package com.ian.weatherdiary.dto.diary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static com.ian.weatherdiary.dto.diary.DiarySearchDto.SortDirection.DESC;

@Getter
@NoArgsConstructor
public class DiarySearchDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String search;

    private SortDirection direction = DESC;
    private int page = 0;
    private int size = 20;

    public Pageable toPageable() {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        return PageRequest.of(page, size, sort);
    }

    public enum SortDirection {
        ASC, DESC;
    }
}
