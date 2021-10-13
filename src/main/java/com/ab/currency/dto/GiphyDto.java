package com.ab.currency.dto;
import lombok.Value;
import java.util.Map;

@Value
public class GiphyDto {

    Map<String, Object> data;
    Map<String, Object> meta;

}
