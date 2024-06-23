package tw.lazycat.lazycat_linebot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
public class FortuneServiceTest {
	
	@Autowired
	FortuneService fortuneService;
	
	@Test
	public void testLottery() {
		System.out.println("testLottery...");
		System.out.println("[123]".matches("\\[.\\]"));
	}

	@Test
	public void testDrawOne() {
		System.out.println("testDrawOne...");
	}

	@BeforeEach
	public void testStart() {
		System.out.println("Test Start!");
	}

	@AfterEach
	public void testEnd() {
		System.out.println("Test End!");
	}
}
