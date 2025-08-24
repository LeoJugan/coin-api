package com.example.coindesk.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.coindesk.entity.Currency;
import com.example.coindesk.entity.CurrencyHist;
import com.example.coindesk.repository.CurrencyHistRepository;
import com.example.coindesk.repository.CurrencyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CoindeskService {

	private final CurrencyRepository currencyRepository;
	private final CurrencyHistRepository currencyHistRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	public Currency addCurrency(Currency newCurrencyData) {
		newCurrencyData.setModifyDate(new Date());
		// 新增歷史紀錄
		CurrencyHist hist = new CurrencyHist();
		BeanUtils.copyProperties(newCurrencyData, hist, "id");
		hist.setId(null);
		hist.setAction("ADD");
		currencyHistRepository.save(hist);

		return currencyRepository.save(newCurrencyData);
	}

	@Transactional
	public Currency updateCurrency(String id, Currency newCurrencyData) {
		return currencyRepository.findById(id).map(existing -> {

			newCurrencyData.setModifyDate(new Date());
			// 新增歷史紀錄
			CurrencyHist hist = new CurrencyHist();
			BeanUtils.copyProperties(newCurrencyData, hist, "id");
			hist.setId(null);
			hist.setAction("SAVE");
			currencyHistRepository.save(hist);

			BeanUtils.copyProperties(newCurrencyData, existing, "id");
			return currencyRepository.save(existing);
		}).orElseThrow(() -> new RuntimeException("Currency not found"));
	}

	@Transactional
	public void deleteCurrency(String id) {
		currencyRepository.findById(id).map(existing -> {

			// 新增歷史紀錄
			CurrencyHist hist = new CurrencyHist();
			BeanUtils.copyProperties(existing, hist, "id");
			hist.setModifyDate(new Date());
			hist.setId(null);
			hist.setAction("DELETE");
			currencyHistRepository.save(hist);

			return existing;
		}).orElseThrow(() -> new RuntimeException("Currency not found"));
		currencyRepository.findById(id).ifPresent(currencyRepository::delete);

	}

	@Transactional
	public void initCurrency() {
		try {

			List<Currency> currencyList = currencyRepository.findAll();
			for (Currency currency : currencyList) {
				this.deleteCurrency(currency.getId());
			}

			String url = "https://kengp3.github.io/blog/coindesk.json";
			RestTemplate restTemplate = new RestTemplate();
			ObjectMapper objectMapper = new ObjectMapper();

			String response = restTemplate.getForObject(url, String.class);
			JsonNode root;

			root = objectMapper.readTree(response);

			JsonNode bpiNode = root.path("bpi");

//        // 中英文名稱對應表
			Map<String, String> zhNameMap = new HashMap<String, String>();
			zhNameMap.put("USD", "美元");
			zhNameMap.put("GBP", "英鎊");
			zhNameMap.put("EUR", "歐元");

			bpiNode.fieldNames().forEachRemaining(data -> {
				JsonNode currencyNode = bpiNode.path(data);
				String code = currencyNode.path("code").asText();
				String name = zhNameMap.get(code) != null ? zhNameMap.get(code) : "";
				String symbol = currencyNode.path("symbol").asText();
				String rate = currencyNode.path("rate").asText();
				String description = currencyNode.path("description").asText();
				Double rateFloat = currencyNode.path("rate_float").asDouble();

				// String nameZh = zhNameMap.getOrDefault(code, description);

				if (!currencyRepository.existsByCode(code)) {
					Currency currency = new Currency(code, name, symbol, rate, description, rateFloat);
					currencyRepository.save(currency);

					CurrencyHist hist = new CurrencyHist();
					BeanUtils.copyProperties(currency, hist);
					hist.setAction("ADD");

					currencyHistRepository.save(hist);
				}
			});
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CoindeskService(CurrencyRepository currencyRepository, CurrencyHistRepository currencyHistRepository,
			ObjectMapper objectMapper) {
		this.currencyRepository = currencyRepository;
		this.currencyHistRepository = currencyHistRepository;
		this.objectMapper = objectMapper;
	}

	public String getRawCoindeskData() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject("https://kengp3.github.io/blog/coindesk.json", String.class);
	}

	public Map<String, Object> getTransformedData() throws Exception {
		String rawJson = getRawCoindeskData();
		JsonNode root = objectMapper.readTree(rawJson);

		Map<String, Object> result = new HashMap<>();
		String updatedTime = root.path("time").path("updatedISO").asText();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		result.put("updatedTime", sdf.format(javax.xml.bind.DatatypeConverter.parseDateTime(updatedTime).getTime()));

		JsonNode bpiNode = root.path("bpi");
		List<Map<String, Object>> currencies = new ArrayList<>();

		Iterator<String> fieldNames = bpiNode.fieldNames();
		while (fieldNames.hasNext()) {
			String code = fieldNames.next();
			JsonNode currencyNode = bpiNode.path(code);
			Currency currency = currencyRepository.findByCode(code);
			Map<String, Object> currencyInfo = new HashMap<>();
			currencyInfo.put("code", code);
//            currencyInfo.put("name", currency != null ? currency.getName() : "N/A");
			currencyInfo.put("rate", currencyNode.path("rate_float").asDouble());
			currencies.add(currencyInfo);
		}

		result.put("currencies", currencies);
		return result;
	}
}
