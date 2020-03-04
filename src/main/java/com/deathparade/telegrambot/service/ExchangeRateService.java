package com.deathparade.telegrambot.service;

import com.deathparade.telegrambot.domain.ExchangeRate;
import com.deathparade.telegrambot.parsers.MoneyExchangeParser;
import com.deathparade.telegrambot.repository.ExchangeRateRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExchangeRateService {
    private ExchangeRateRepository exchangeRateRepository;
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public ExchangeRate save(ExchangeRate exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }

    public Optional<ExchangeRate> read(String id) {
        return exchangeRateRepository.findById(id);
    }

}
