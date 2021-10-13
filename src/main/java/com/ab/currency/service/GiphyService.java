package com.ab.currency.service;

import com.ab.currency.dto.GiphyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "giphy", url = "${giphy.host}")
public interface GiphyService {

    @RequestMapping(method = RequestMethod.GET, value = "random?api_key={api_key}&tag={tag}&rating=g")
    GiphyDto getGif(@PathVariable("api_key") String apiKey, @PathVariable("tag") String tag);
}
