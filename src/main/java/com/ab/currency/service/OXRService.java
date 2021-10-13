package com.ab.currency.service;

import com.ab.currency.dto.OXRDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "oxr", url = "${oxr.host}")
public interface OXRService {

    @RequestMapping(method = RequestMethod.GET, value = "latest.json?app_id={app_id}")
    OXRDto getTodayRate(@PathVariable("app_id") String appId);

    @RequestMapping(method = RequestMethod.GET, value = "historical/{date}.json?app_id={app_id}")
    OXRDto getYesterdayRate(@PathVariable("date") String date, @PathVariable("app_id") String appId);
}
