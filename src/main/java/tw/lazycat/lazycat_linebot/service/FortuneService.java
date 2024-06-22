package tw.lazycat.lazycat_linebot.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.linecorp.bot.webhook.model.MessageEvent;

import tw.lazycat.lazycat_linebot.controller.LineController;

/**
 * 抽籤服務
 * 
 * @since 2024-06-22
 * @author Tim
 */
@Service
@Validated
public class FortuneService {
	private final Logger log = LoggerFactory.getLogger(FortuneService.class);

	@Autowired
	private LineReplyService lineReplyService;

	public void getFortune(@NotNull MessageEvent event) {

		String reply = getFortuneString();

		lineReplyService.replyText(event.replyToken(), reply);
	}

	/**
	 * 隨機抽取籤詩
	 * 
	 * @return
	 */
	private String getFortuneString() {
		// 1~100
		int randomInt = (int) (Math.random() * 100) + 1;
		log.debug("抽到的數字是: " + randomInt);
		return getFortuneString(randomInt);
	}

	/**
	 * 輸入數字，拿到對應籤詩
	 * 
	 * @param randomInt
	 * @return
	 */
	private String getFortuneString(int randomInt) {
		if (randomInt <= 30) {
			return "凶";
		} else if (randomInt <= 65) {
			return "吉";
		} else if (randomInt <= 82) {
			return "大吉";
		} else {
			return "性奴隸\r\nこの結果がでるなんて、ある意味運いいかもね？";
		}
	}
}
