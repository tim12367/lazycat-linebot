package tw.lazycat.lazycat_linebot.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.linecorp.bot.webhook.model.MessageEvent;

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

	/**
	 * 從列表內抽取，並回覆給客戶
	 * 
	 * @param list
	 * @param event
	 */
	public void lottery(@NotNull List<String> list, @NotNull MessageEvent event) {
		String reply = drawOne(list);

		// 若抽到空字串
		if (StringUtils.isBlank(reply)) {
			log.debug("抽取為空");
			lineReplyService.replyText(event.replyToken(), "您抽到: 空");
			return;
		}
		lineReplyService.replyText(event.replyToken(), "您抽到: " + reply);
	}

	/**
	 * 抽取list中一個字串
	 * 
	 * @param list
	 * @return
	 */
	public String drawOne(@NotNull List<String> list) {
		int randomInt = (int) Math.random() * list.size();
		return list.get(randomInt);
	}

	/**
	 * 隨機抽取籤詩，並回覆給客戶
	 * 
	 * @param event
	 */
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

	/**
	 * 去除首尾","
	 * @param data
	 * @return
	 */
	public String removeCommas(String data) {
		data = data.replaceAll("^,|,$", "");
		return data;
	}
}
