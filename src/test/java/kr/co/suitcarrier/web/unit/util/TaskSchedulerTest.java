package kr.co.suitcarrier.web.unit.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import kr.co.suitcarrier.web.util.TaskScheduler;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TaskSchedulerTest {
    
    @Autowired
    private TaskScheduler taskScheduler;
    
    @BeforeAll
    static void setup() {

    }

    @BeforeEach
    void init() {
        
    }
}

/*
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class})
public class RefreshJwtSchedulerTest {

    @Autowired
    private RefreshJwtScheduler refreshJwtScheduler;

    @Test
    public void testRefreshJwtScheduler() {
        // Create a refresh JWT with an expiration date in the past.
        String refreshJwt = Jwts.builder()
                .setSubject("test")
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();

        // Schedule the refresh JWT scheduler to run.
        refreshJwtScheduler.schedule();

        // Wait for the refresh JWT scheduler to run.
        Thread.sleep(1000);

        // Assert that the outdated refresh JWT has been removed.
        assertThat(refreshJwtScheduler.getRefreshJwts()).doesNotContain(refreshJwt);
    }

}

 */

 /*
    0. h2 DB 설정
        - init할때 in3ight_sql.sql을 실행해서 DB Schema 생성
    1. RefreshToken을 직접 h2 DB에 넣어준다
        - created_at
        - exp를 임의의 값으로
        - Repository에서 구현
    2. TaskScheduler -> deleteExpiredRefreshToken 호출해서 토큰 삭제
    3. Repository -> findByRefreshJwtAndCreatedAt 호출해서 삭제한 토큰 있는지 조회(Null) 
*/
    