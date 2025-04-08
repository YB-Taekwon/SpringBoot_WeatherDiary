package com.ian.weatherdiary.service;

import com.ian.weatherdiary.domain.Diary;
import com.ian.weatherdiary.domain.Weather;
import com.ian.weatherdiary.repository.DiaryRepository;
import com.ian.weatherdiary.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    private final WeatherRepository weatherRepository;

    @Value("${openweathermap.key}") // properties에서 API Key 주입 (깃허브 등 외부에 공개되지 않도록)
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

    private Weather getWeatherFromApi() {
        // Open Weather Map 데이터 받아오기
        String weatherString = getWeatherString();
        log.info(weatherString);

        // 받아온 날씨 데이터f를 JSON 형식으로 파싱하기
        Map<String, Object> parseWeather = parseWeather(weatherString);

        // 엔티티 객체에 데이터 담기
        Weather weather = new Weather();

        weather.setDate(LocalDate.now());
        weather.setWeather(parseWeather.get("main").toString());
        weather.setIcon(parseWeather.get("icon").toString());
        weather.setTemperature((Double) parseWeather.get("temp"));

        return weather;
    }


    // 매일 01시 마다 날씨 데이터 저장
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void saveWeatherDate() {
        weatherRepository.save(getWeatherFromApi());
    }


    // DB에서 날씨 데이터 가져오기
    private Weather getWeather(LocalDate date) {
        List<Weather> weatherListFromDb = weatherRepository.findAllByDate(date);

        if (weatherListFromDb.isEmpty())
            // DB에 찾는 날짜의 날씨 데이터가 없을 경우, OpenAPI에서 새로운 날씨 정보를 받아온다.
            return getWeatherFromApi();
        else
            // DB에 찾는 날짜의 낳씨 데이터가 있는 경우, 해당 데이터를 받아온다.
            return weatherListFromDb.get(0);
    }

    // DB에 저장하기
    private void saveDiary(LocalDate date, String text, Weather weather) {
        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(weather);
        nowDiary.setText(text);

        diaryRepository.save(nowDiary);
    }

    // 일기 작성
    @Transactional
    public void createDiary(LocalDate date, String text) {
        // 1. DB에서 날씨 데이터 가져오기
        Weather weather = getWeather(date);

        // 2. DB에 저장하기
        saveDiary(date, text, weather);
    }


    // 특정 날짜 일기 조회
    public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
    }


    // 범위 내 날짜의 일기 조회
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }


    // 일기 수정
    @Transactional
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }


    // 일기 삭제
    @Transactional
    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
