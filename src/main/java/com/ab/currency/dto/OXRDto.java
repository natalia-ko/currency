package com.ab.currency.dto;
import lombok.Value;
import java.util.Map;

@Value
public class OXRDto {
    String disclaimer;
    String license;
    Integer timestamp;
    String base;
    Map<String, String> rates;

}
