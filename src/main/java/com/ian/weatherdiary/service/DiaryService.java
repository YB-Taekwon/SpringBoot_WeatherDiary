package com.ian.weatherdiary.service;

import com.ian.weatherdiary.domain.Diary;
import com.ian.weatherdiary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    @Value("${openweathermap.key}")
    private String apiKey;


    // Open Weather Map 데이터 받아오기
    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
        try {
            // URL 객체로 변환 (자바에서 웹 요청을 할 때 필요)
            URL url = new URL(apiUrl);
            // 해당 URL을 HTTP 방식으로 통신 연결
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // GET 방식으로 요청
            connection.setRequestMethod("GET");
            // 서버 응답 코드를 받아옴
            int responseCode = connection.getResponseCode();

            BufferedReader br;
            if (responseCode == 200)
                // 응답 코드가 200이면 정상 응답을 읽어옴
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            else
                // 그 외의 코드는 에러 메시지를 읽어옴
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

            // 응답 내용(inputLine)을 한 줄씩 읽어서 문자열 response에 저장
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            return response.toString();
        } catch (Exception e) {
            return "failed to get response.";
        }
    }

    // 받아온 날씨 데이터f를 JSON 형식으로 파싱하기
    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser(); // 문자열 -> JSON 변환 기능을 제공하는 객체
        JSONObject jsonObject; // 파싱된 JSON 결과({"lon":126.9778,"lat":37.5683}...)를 담을 변수

        try {
            // 문자열 -> JSON 변환
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> resultMap = new HashMap<>();

        // JSONObject = {중괄호} 안에 담겨있는 데이터
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));
        // JSONArray = [대괄호] 안에 담겨있는 데이터
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }

    // DB에 저장하기
    private void saveDiary(LocalDate date, String text, Map<String, Object> parseWeather) {
        Diary nowDiary = new Diary();
        nowDiary.setWeather(parseWeather.get("main").toString());
        nowDiary.setIcon(parseWeather.get("icon").toString());
        nowDiary.setTemperature((Double) parseWeather.get("temp"));
        nowDiary.setText(text);
        nowDiary.setDate(date);

        diaryRepository.save(nowDiary);
    }

    // 일기 작성
    public void createDiary(LocalDate date, String text) {
        // 1. Open Weather Map 데이터 받아오기
        String weatherString = getWeatherString();
        log.info(weatherString);

        // 2. 받아온 날씨 데이터f를 JSON 형식으로 파싱하기
        Map<String, Object> parseWeather = parseWeather(weatherString);

        // 3. DB에 저장하기
        saveDiary(date, text, parseWeather);
    }


    // 특정 날짜 일기 조회
    public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
    }


    // 범위 내 날짜의 일기 조회
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }
}
