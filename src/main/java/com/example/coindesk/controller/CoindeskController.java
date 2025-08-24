package com.example.coindesk.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.coindesk.entity.Currency;
import com.example.coindesk.entity.CurrencyHist;
import com.example.coindesk.repository.CurrencyHistRepository;
import com.example.coindesk.repository.CurrencyRepository;
import com.example.coindesk.service.CoindeskService;

@RestController
@RequestMapping("/api")
public class CoindeskController {

	private final CurrencyRepository currencyRepository;
	private final CurrencyHistRepository currencyHistRepository;
	private final CoindeskService coindeskService;

	public CoindeskController(CurrencyRepository currencyRepository, CurrencyHistRepository currencyHistRepository,
			CoindeskService coindeskService) {
		this.currencyRepository = currencyRepository;
		this.currencyHistRepository = currencyHistRepository;
		this.coindeskService = coindeskService;
	}

	@GetMapping("/currenciesInit")
	public void currenciesInit() {
		coindeskService.initCurrency();
	}

	@GetMapping("/currencies")
	public List<Currency> getAllCurrencies() {
		return currencyRepository.findAll();
	}

	@GetMapping("/currenciesFindByCode/{code}")
	public Currency currenciesFindByCode(@PathVariable String code) {
		return currencyRepository.findByCode(code);
	}

	@PostMapping("/currencies")
	public Currency createCurrency(@RequestBody Currency currency) {
		return coindeskService.addCurrency(currency);
	}

	@PutMapping("/currencies")
	public ResponseEntity<Currency> updateCurrency(@RequestBody Currency currency) {
//		return currencyRepository.findById(id).map(existing -> {
//			BeanUtils.copyProperties(currency, existing,"id");
//
//			return ResponseEntity.ok(currencyRepository.save(existing));
//		}).orElse(ResponseEntity.notFound().build());

		return ResponseEntity.ok(coindeskService.updateCurrency(currency.getId(), currency));
	}

	@DeleteMapping("/currencies/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCurrency(@PathVariable String id) {
		coindeskService.deleteCurrency(id);
//		currencyRepository.findById(id).ifPresent(currencyRepository::delete);
	}

	@GetMapping("/coindesk/raw")
	public String getRawCoindesk() {
		return coindeskService.getRawCoindeskData();
	}

	@GetMapping("/coindesk/transform")
	public Map<String, Object> getTransformed() throws Exception {
		return coindeskService.getTransformedData();
	}

	@GetMapping("/currenciesHist")
	public List<CurrencyHist> getAllCurrenciesHist() {
		return currencyHistRepository.findAll();
	}
}
