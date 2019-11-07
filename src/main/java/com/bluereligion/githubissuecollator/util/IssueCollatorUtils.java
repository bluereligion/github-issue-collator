package com.bluereligion.githubissuecollator.util;

import com.bluereligion.githubissuecollator.dto.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class IssueCollatorUtils {

    public static String convertToJson(Response response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(response);
    }

    public static LocalDateTime convertToLocalDateTime(String s) {
        Instant instant = Instant.parse(s);
        return LocalDateTime.ofInstant(instant, ZoneId.of(ZoneOffset.UTC.getId()));
    }
}
