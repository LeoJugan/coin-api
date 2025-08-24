# Spring Boot Coindesk API Demo

## 功能
- 呼叫 Coindesk API 並回傳原始資料
- 將資料轉換，加入幣別中文名稱，回傳新格式
- 幣別資料表 CRUD (使用 H2)
- 單元測試

## API
- GET /api/currenciesInit
- GET /api/currencies
- GET /api/currenciesHist
- GET /api/currenciesFindByCode/{code}
- POST /api/currencies
- PUT /api/currencies/
- DELETE /api/currencies/{id}


## 啟動
```bash
mvn spring-boot:run
```
H2 Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
JDBC URL: `jdbc:h2:mem:coindeskdb`
