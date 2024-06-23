package tw.lazycat.lazycat_linebot.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.PatternSyntaxException;

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
	 * @param messageText 如: "[123,456]"
	 * @param event 如: 您抽到: 123
	 */
	public void lottery(@NotNull final String messageText, @NotNull MessageEvent event) throws PatternSyntaxException {
		// 字串轉List<String> 如 "[123,456]" -> List<String>
		List<String> list = this.stringToList(messageText);
		
		// 抽出一筆
		String reply = this.drawOne(list);
		
		// 若抽到空字串
		if (StringUtils.isBlank(reply)) {
			lineReplyService.replyText(event.replyToken(), "您抽到: 空");
			return;
		} else {
			lineReplyService.replyText(event.replyToken(), "您抽到: " + reply);
		}
	}

	/**
	 * 字串分割成list
	 * @param text 如: "[123,456]"
	 * @return List<String>
	 */
	private List<String> stringToList(String text) {
		// 移除頭尾[]
		String arrayString = this.removeBrackets(text);
		// 移除頭尾逗號
		arrayString = this.removeCommas(arrayString);
		// 切字串
		String[] jsonArray = arrayString.split(",");
		return Arrays.asList(jsonArray);
	}
	
	/**
	 * 抽取list中一個字串
	 * 
	 * @param list
	 * @return
	 */
	public String drawOne(@NotNull List<String> list) {
		int randomInt = (int) (Math.random() * list.size());
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
	 * 
	 * @param data
	 * @return
	 */
	public String removeCommas(String data) {
		data = data.replaceAll("^,|,$", "");
		return data;
	}
	
	/**
	 * 去除首尾"["、"]"
	 * 
	 * @param data
	 * @return
	 */
	public String removeBrackets(String data) {
		data = data.replaceAll("^\\[|\\]$", "");
		return data;
	}
}
