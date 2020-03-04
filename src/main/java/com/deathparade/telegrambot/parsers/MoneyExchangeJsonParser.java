package com.deathparade.telegrambot.parsers;

import com.deathparade.telegrambot.domain.ExchangeRate;
import com.deathparade.telegrambot.service.ExchangeRateService;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class MoneyExchangeJsonParser implements MoneyExchangeParser {
    private final static String API_CALL_TEMPLATE = "https://api.exchangerate-api.com/v4/latest/";
    private final static String USER_AGENT = "Mozilla/5.0";
    private final static String[] bases = {"EUR", "USD", "RUB"};

    private ExchangeRateService exchangeRateService;

    public MoneyExchangeJsonParser(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Override
    @Scheduled(fixedRate = 600000)
    public void uploadCurrentRate() {
        ExchangeRate rate = new ExchangeRate();
        for (String base : bases) {
            try {
                String rawData = downloadJsonRawData(base);
                convertJsonRawData(rawData, rate);
                exchangeRateService.save(rate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String downloadJsonRawData(String base) throws Exception {
        String urlString = API_CALL_TEMPLATE + base;
        URL urlObject = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        if (responseCode == 404) {
            throw new IllegalArgumentException();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static void convertJsonRawData(String data, ExchangeRate rate) throws Exception {
        JSONObject object = new JSONObject(data);
        rate.setBase(object.getString("base"));
        rate.setDate(object.getString("date"));
        // rate.setLastUpdate(object.getLong("time_last_updated"));

        JSONObject rates = object.getJSONObject("rates");
        rate.setValue(rates.getDouble("KZT"));
    }

}