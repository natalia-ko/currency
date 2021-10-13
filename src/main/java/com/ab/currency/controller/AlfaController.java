package com.ab.currency.controller;

import com.ab.currency.dto.GiphyDto;
import com.ab.currency.dto.OXRDto;
import com.ab.currency.service.FileDownloadService;
import com.ab.currency.service.GiphyService;
import com.ab.currency.service.OXRService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequiredArgsConstructor
public class AlfaController {
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

    private final OXRService oxrService;
    private final GiphyService giphyService;
    private final FileDownloadService fileDownloadService;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/{input_currency}")
    public Object amIRich(@PathVariable("input_currency") String inputCurrency) throws IOException {
        OXRDto todayRate = oxrService.getTodayRate(APP_ID);

        String yesterday = getYesterdayString();
        OXRDto yesterdayRate = oxrService.getYesterdayRate(yesterday, APP_ID);

        boolean soAmIRich = isMoreThanYesterday(todayRate, yesterdayRate, inputCurrency);
        String tag = soAmIRich ? tagPositive : tagNegative;

        GiphyDto image = giphyService.getGif(API_KEY, tag);
        String originalImageId = image.getData().get("id").toString();
        //todo комментарий
        String imageUrl = imageHost + originalImageId + imageFormat;
        System.out.println(imageUrl);

        byte[] imageBytes = fileDownloadService.download(imageUrl);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_GIF).body(imageBytes);
    }


    private String getYesterdayString() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();
        return DATE_FORMAT.format(yesterday);
    }

    //todo комментарий
    private boolean isMoreThanYesterday(OXRDto todayRates, OXRDto yesterdayRates, String inputCurrency) {
        // USD is default currency in OXR API
        if (inputCurrency.equals("USD")) {
            return getRateFromResponce(todayRates, currency) - getRateFromResponce(yesterdayRates, currency) > 0;
        }
        double today = getRateFromResponce(todayRates, inputCurrency) / getRateFromResponce(todayRates, currency);
        double yesterday = getRateFromResponce(yesterdayRates, inputCurrency) / getRateFromResponce(yesterdayRates, currency);
        return today - yesterday > 0;
    }

    private double getRateFromResponce(OXRDto rate, String currency) {
        return Double.parseDouble(rate.getRates().get(currency));
    }
}
