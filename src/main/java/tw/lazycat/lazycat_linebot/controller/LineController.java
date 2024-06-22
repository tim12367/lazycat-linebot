package tw.lazycat.lazycat_linebot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.Event;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import com.linecorp.bot.webhook.model.UnknownEvent;

import tw.lazycat.lazycat_linebot.service.YouTubeInfoService;

@LineMessageHandler
public class LineController {
	private final Logger log = LoggerFactory.getLogger(LineController.class);
	private final MessagingApiClient messagingApiClient;
	private final MessagingApiBlobClient messagingApiBlobClient;

	@Autowired
	private YouTubeInfoService youTubeInfoService;

	public LineController(MessagingApiClient messagingApiClient, MessagingApiBlobClient messagingApiBlobClient) {
		this.messagingApiClient = messagingApiClient;
		this.messagingApiBlobClient = messagingApiBlobClient;
	}

	@EventMapping
	public void handleTextMessageEvent(MessageEvent event) {
		log.info("event: " + event);
		if (event.message() instanceof TextMessageContent) {
			TextMessageContent message = (TextMessageContent) event.message();
			final String originalMessageText = message.text();
			log.debug("User originalMessageText: " + originalMessageText);

			// 抓取YouTube影片資料並回傳
			if (originalMessageText.indexOf("youtu.be") > 0 || originalMessageText.indexOf("www.youtube.com") > 0) {
				// 1. parse YouTube info

				// TODO: 回應訊息
//			messagingApiClient.replyMessage(new ReplyMessageRequest(
//					event.replyToken(),
//					List.of(new TextMessage(originalMessageText)),
//					false));
			}
		}
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		log.debug("handleDefaultMessageEvent... event: " + event);
	}

	@EventMapping
	public void handleUnknownEvent(UnknownEvent event) {
		log.debug("Got an unknown event!!!!! : {}", event);
	}
}