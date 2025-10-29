package com.ian.weatherdiary.repository;

import com.ian.weatherdiary.domain.Diary;
import com.ian.weatherdiary.domain.QDiary;
import com.ian.weatherdiary.domain.QWeather;
import com.ian.weatherdiary.dto.diary.DiarySearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static com.ian.weatherdiary.dto.diary.DiarySearchDto.SortDirection.ASC;

@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QDiary diary = QDiary.diary;
    private final QWeather weather = QWeather.weather1;

    @Override
    public Page<Diary> searchDiaries(DiarySearchDto dto, Pageable pageable) {
        BooleanBuilder where = baseFilter(dto);

        // 작성일 정렬
        OrderSpecifier<LocalDate> orderBy = (dto.getDirection() == ASC)
                ? weather.date.asc().nullsLast()
                : weather.date.desc().nullsLast();

        List<Diary> content = queryFactory
                .selectFrom(diary)
                .leftJoin(diary.weather, weather).fetchJoin()
                .where(where)
                .orderBy(orderBy)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(diary.count())
                .from(diary)
                .leftJoin(diary.weather, weather)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanBuilder baseFilter(DiarySearchDto dto) {
        BooleanBuilder where = new BooleanBuilder();

        LocalDate start = dto.getStartDate();
        LocalDate end = dto.getEndDate();
        String search = dto.getSearch();

        // 기간 필터링
        if (start != null && end != null)
            where.and(weather.date.between(start, end));
        else if (start != null)
            where.and(weather.date.goe(start));
        else if (end != null)
            where.and(weather.date.loe(end));

        // 내용 검색 필터링
        if (StringUtils.hasText(search)) {
            where.and(diary.content.containsIgnoreCase(search));
        }

        return where;
    }
}
