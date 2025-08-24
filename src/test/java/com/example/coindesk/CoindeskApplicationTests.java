package com.example.coindesk;

import com.example.coindesk.service.CoindeskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CoindeskApplicationTests {

    @Autowired
    private CoindeskService coindeskService;

    @Test
    void testRawCoindeskApi() {
        String data = coindeskService.getRawCoindeskData();
        assertThat(data).contains("bpi");
    }

    @Test
    void testTransformedData() throws Exception {
        Map<String, Object> transformed = coindeskService.getTransformedData();
        assertThat(transformed).containsKeys("updatedTime", "currencies");
    }
}
