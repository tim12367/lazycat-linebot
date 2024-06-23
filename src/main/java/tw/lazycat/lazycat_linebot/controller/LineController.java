package tw.lazycat.lazycat_linebot.controller;

import java.util.Arrays;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import com.linecorp.bot.webhook.model.UnknownEvent;

import tw.lazycat.lazycat_linebot.service.FortuneService;
import tw.lazycat.lazycat_linebot.service.YouTubeInfoService;

/**
 * LINE 訊息回覆
 * 
 * @since 2024-06-22
 * @author Tim
 */
@LineMessageHandler
public class LineController {
	private final Logger log = LoggerFactory.getLogger(LineController.class);

	@Autowired
	private YouTubeInfoService youTubeInfoService;

	@Autowired
	private FortuneService fortuneService;

	/**
	 * 文字訊息處理
	 * 
	 * @param event
	 * @param textMessageContent
	 * @throws Exception
	 */
	@EventMapping
	public void handleTextMessageEvent(MessageEvent event, TextMessageContent textMessageContent) throws Exception {
		log.debug("handleTextMessageEvent event: " + event);

		final String originalMessageText = textMessageContent.text();
		log.debug("User originalMessageText: " + originalMessageText);

		// 抓取YouTube影片資料並回傳
		if (originalMessageText.indexOf("youtu.be") != -1 || originalMessageText.indexOf("www.youtube.com") != -1) {
			// 1. parse YouTube info

			// TODO: 回應訊息
//			messagingApiClient.replyMessage(new ReplyMessageRequest(
//					event.replyToken(),
//					List.of(new TextMessage(originalMessageText)),
//					false));
		}

		// 運氣
		if (originalMessageText.indexOf("運") != -1) {
			fortuneService.getFortune(event);
		}

		// 選項中抽一個
		if (originalMessageText.startsWith("[") && originalMessageText.endsWith("]") && !originalMessageText.equals("[,]") && originalMessageText.length() > 2) {
			try {
				String arrayString = originalMessageText.substring(1, originalMessageText.length() - 1);
				String[] jsonArray = arrayString.split(",");
				fortuneService.lottery(Arrays.asList(jsonArray), event);
			} catch (PatternSyntaxException e) {
				log.error("String.split(,) 轉換失敗", e);
			}
		}
	}

	/**
	 * 其他事件處理
	 * 
	 * @param event
	 */
	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		log.debug("handleDefaultMessageEvent... event: " + event);
	}

	@EventMapping
	public void handleUnknownEvent(UnknownEvent event) {
		log.debug("Got an unknown event!!!!! : {}", event);
	}
}