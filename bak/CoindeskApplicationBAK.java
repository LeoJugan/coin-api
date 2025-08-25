package com.example.coindesk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.coindesk.service.CoindeskService;

@SpringBootApplication
public class CoindeskApplicationBAK {

	public static void main(String[] args) {
		SpringApplication.run(CoindeskApplicationBAK.class, args);
	}

//	@Bean
//	public CommandLineRunner initCurrencyData(CurrencyRepository currencyRepository,
//			CurrencyHistRepository currencyHistRepository) {
//		return args -> {
//			String url = "https://kengp3.github.io/blog/coindesk.json";
//			RestTemplate restTemplate = new RestTemplate();
//			ObjectMapper objectMapper = new ObjectMapper();
//
//			String response = restTemplate.getForObject(url, String.class);
//			JsonNode root = objectMapper.readTree(response);
//			JsonNode bpiNode = root.path("bpi");
//
////            // 中英文名稱對應表
//			Map<String, String> zhNameMap = new HashMap<String, String>();
//			zhNameMap.put("USD", "美元");
//			zhNameMap.put("GBP", "英鎊");
//			zhNameMap.put("EUR", "歐元");
//
//			bpiNode.fieldNames().forEachRemaining(data -> {
//				JsonNode currencyNode = bpiNode.path(data);
//				String code = currencyNode.path("code").asText();
//				String name = zhNameMap.get(code) != null ? zhNameMap.get(code) : "";
//				String symbol = currencyNode.path("symbol").asText();
//				String rate = currencyNode.path("rate").asText();
//				String description = currencyNode.path("description").asText();
//				Double rateFloat = currencyNode.path("rate_float").asDouble();
//
//				// String nameZh = zhNameMap.getOrDefault(code, description);
//
//				if (!currencyRepository.existsByCode(code)) {
//					Currency currency = new Currency(code, name, symbol, rate, description, rateFloat);
//					currencyRepository.save(currency);
//
//					CurrencyHist hist = new CurrencyHist();
//					BeanUtils.copyProperties(currency, hist);
//					hist.setAction("ADD");
//
//					currencyHistRepository.save(hist);
//				}
//			});
//
//			System.out.println("✅ 預設幣別資料初始化完成！");
//		};
//	}

	@Bean
	public CommandLineRunner initCurrencyData(CoindeskService coindeskService) {
		return args -> {
			coindeskService.initCurrency();

			System.out.println("✅ 預設幣別資料初始化完成！");
		};
	}

}
