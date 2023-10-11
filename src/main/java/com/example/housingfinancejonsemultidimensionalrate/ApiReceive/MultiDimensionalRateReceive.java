package com.example.housingfinancejonsemultidimensionalrate.ApiReceive;

import com.example.housingfinancejonsemultidimensionalrate.DTO.MultiDimensionalRateRequest;
import com.example.housingfinancejonsemultidimensionalrate.DTO.MultiDimensionalRateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@Slf4j
public class MultiDimensionalRateReceive {
    @Value("${dataPortal.secret}")
    private String dataPortalSecret;
    private static final String ENDPOINTURL = "http://apis.data.go.kr/B551408/rent-loan-rate-multi-dimensional-info/dimensional-list";

    public MultiDimensionalRateResponse apiReceive(MultiDimensionalRateRequest request) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(ENDPOINTURL)
                .queryParam("serviceKey",dataPortalSecret)
                .queryParam("dataType","JSON");
        log.info(request.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> requestParam = objectMapper.convertValue(request,Map.class);
        for(Map.Entry element : requestParam.entrySet()){
            if(element.getValue().toString().isEmpty()) continue;
            uriComponentsBuilder.queryParam(element.getKey().toString(),element.getValue().toString());
        }

        DefaultUriBuilderFactory defaultUriBuilderFactory = new DefaultUriBuilderFactory();
        defaultUriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        UriComponents uriComponents = uriComponentsBuilder.build();

        String finalUrl = uriComponents.toUriString();
        log.info(finalUrl);

        WebClient build = WebClient.builder()
                .uriBuilderFactory(defaultUriBuilderFactory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MultiDimensionalRateResponse block = build
                .get()
                .uri(finalUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MultiDimensionalRateResponse.class)
                .block();
        return block;
    }
}
