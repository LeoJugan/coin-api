package com.example.coindesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.coindesk.entity.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String> {
	Currency findByCode(String code);

	boolean existsByCode(String code);
}
