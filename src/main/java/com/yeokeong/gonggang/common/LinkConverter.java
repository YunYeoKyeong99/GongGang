package com.yeokeong.gonggang.common;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeokeong.gonggang.model.Link;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.List;

// Mysql post links 타입 JSON
// JPA Post links 타입 List<Link>

// AttributeConverter = Mysql <-> JPA 간 컬럼 데이터 변환 도구
// convertToDatabaseColumn = JPA(links List<Link>) -> Mysql(links JSON) = serialize
// convertToEntityAttribute = Mysql(links JSON) -> JPA(links List<Link>) = deserialize

@Slf4j
@Converter
public class LinkConverter implements AttributeConverter<List<Link>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(final List<Link> linkList) {

        if (linkList == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(linkList);
        } catch (JsonProcessingException e) {
            log.error("PostLinkConverter", e);
            return null;
        }
    }

    @Override
    public List<Link> convertToEntityAttribute(final String linksString) {

        if (!StringUtils.hasLength(linksString)) {
            return null;
        }

        try {
            return objectMapper.readValue(linksString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            log.error("PostLinkConverter", e);
            return null;
        }
    }
}