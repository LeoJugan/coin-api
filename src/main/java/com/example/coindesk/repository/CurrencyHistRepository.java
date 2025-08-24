package com.example.coindesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.coindesk.entity.CurrencyHist;

public interface CurrencyHistRepository extends JpaRepository<CurrencyHist, String> {

}
