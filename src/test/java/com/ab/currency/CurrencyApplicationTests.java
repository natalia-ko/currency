package com.ab.currency;

import com.ab.currency.controller.AlfaController;
import com.ab.currency.dto.GiphyDto;
import com.ab.currency.dto.OXRDto;
import com.ab.currency.service.FileDownloadService;
import com.ab.currency.service.GiphyService;
import com.ab.currency.service.OXRService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyApplicationTests {

    @Autowired
    private AlfaController controller;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private OXRService oxrService;

    @MockBean
    private GiphyService giphyService;

    @MockBean
    private FileDownloadService fileDownloadService;

    @Value("${APP_ID}")
    private String APP_ID;

    @Value("${API_KEY}")
    private String API_KEY;

    @Value("${currency}")
    private String currency;

    @Value("${image.host}")
    private String imageHost;

    @Value("${imageFormat}")
    private String imageFormat;

    @Value("${tagPositive}")
    private String tagPositive;

    @Value("${tagNegative}")
    private String tagNegative;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void shouldBeRich() throws Exception {
        Map<String, String> todayRates = new LinkedHashMap<>();
        todayRates.put("EUR", "70");
        todayRates.put("RUB", "60");
        Map<String, String> yesterdayRates = new LinkedHashMap<>();
        yesterdayRates.put("EUR", "5");
        yesterdayRates.put("RUB", "60");
        OXRDto todayDto = new OXRDto("", "", 100, "EUR", todayRates);
        OXRDto yesterdayDto = new OXRDto("", "", 100, "EUR", yesterdayRates);

        Mockito.when(oxrService.getTodayRate(APP_ID)).thenReturn(todayDto);
        Mockito.when(oxrService.getYesterdayRate(getYesterdayString(), APP_ID)).thenReturn(yesterdayDto);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", "12345");
        Map<String, Object> meta = new LinkedHashMap<>();
        GiphyDto imageData = new GiphyDto(data, meta);
        Mockito.when(giphyService.getGif(API_KEY, "rich")).thenReturn(imageData);

        byte[] imageBytes = Files.readAllBytes(Paths.get(new File("src/test/resources/rich.gif").getAbsolutePath()));

        Mockito.when(fileDownloadService.download(imageHost + imageData.getData().get("id") + imageFormat)).thenReturn(imageBytes);

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/EUR",
                byte[].class)).isEqualTo(imageBytes);
    }

    @Test
    public void shouldBeBroke() throws Exception {
        Map<String, String> todayRates = new LinkedHashMap<>();
        todayRates.put("EUR", "70");
        todayRates.put("RUB", "60");
        Map<String, String> yesterdayRates = new LinkedHashMap<>();
        yesterdayRates.put("EUR", "500");
        yesterdayRates.put("RUB", "60");
        OXRDto todayDto = new OXRDto("", "", 100, "EUR", todayRates);
        OXRDto yesterdayDto = new OXRDto("", "", 100, "EUR", yesterdayRates);

        Mockito.when(oxrService.getTodayRate(APP_ID)).thenReturn(todayDto);
        Mockito.when(oxrService.getYesterdayRate(getYesterdayString(), APP_ID)).thenReturn(yesterdayDto);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", "54321");
        Map<String, Object> meta = new LinkedHashMap<>();
        GiphyDto imageData = new GiphyDto(data, meta);
        Mockito.when(giphyService.getGif(API_KEY, "broke")).thenReturn(imageData);

        byte[] imageBytes = Files.readAllBytes(Paths.get(new File("src/test/resources/broke.gif").getAbsolutePath()));

        Mockito.when(fileDownloadService.download(imageHost + imageData.getData().get("id") + imageFormat)).thenReturn(imageBytes);

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/EUR",
                byte[].class)).isEqualTo(imageBytes);
    }

    private String getYesterdayString() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        return DATE_FORMAT.format(yesterday);
    }

}
