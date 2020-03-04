package com.deathparade.telegrambot.repository;

import com.deathparade.telegrambot.domain.ExchangeRate;
import org.springframework.data.repository.CrudRepository;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, String> {
}
