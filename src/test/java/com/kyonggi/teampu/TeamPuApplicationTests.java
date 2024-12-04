package com.kyonggi.teampu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//@SpringBootTest
@ActiveProfiles("test") // application-test.yml 파일의 설정을 로드
class TeamPuApplicationTests {

    @Test
    void contextLoads() {
    }

}
