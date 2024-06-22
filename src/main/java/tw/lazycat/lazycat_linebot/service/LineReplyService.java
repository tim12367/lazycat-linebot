package tw.lazycat.lazycat_linebot.service;

import java.util.List;
import java.util.Objects;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.ReplyMessageRequest;
import com.linecorp.bot.messaging.model.ReplyMessageResponse;
import com.linecorp.bot.messaging.model.TextMessage;

import tw.lazycat.lazycat_linebot.controller.LineController;

/**
 * LINE 回應服務，參考LINE官方範例
 * 
 * @since 2024-06-22
 * @author Tim
 */
@Service
public class LineReplyService {
	private final Logger log = LoggerFactory.getLogger(LineReplyService.class);
	private final MessagingApiClient messagingApiClient;
	private final MessagingApiBlobClient messagingApiBlobClient;

	public LineReplyService(MessagingApiClient messagingApiClient, MessagingApiBlobClient messagingApiBlobClient) {
		this.messagingApiClient = messagingApiClient;
		this.messagingApiBlobClient = messagingApiBlobClient;
	}

	/**
	 * 回應訊息(多筆)
	 * 
	 * @param replyToken
	 * @param messages
	 */
	public void reply(String replyToken, List<Message> messages) {
		Objects.requireNonNull(replyToken, "replyToken");
		Objects.requireNonNull(messages, "messages");
		this.reply(replyToken, messages, false);
	}

	/**
	 * 回應文字訊息(單筆)
	 * 
	 * @param replyToken
	 * @param message
	 */
	public void replyText(String replyToken, String message) {
		Objects.requireNonNull(replyToken, "replyToken");
		Objects.requireNonNull(message, "message");
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken 不可為空!");
		}
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "……";
		}
		this.reply(replyToken, new TextMessage(message));
	}

	/**
	 * 回應訊息(單筆)
	 * 
	 * @param replyToken
	 * @param message
	 */
	public void reply(String replyToken, Message message) {
		Objects.requireNonNull(replyToken, "replyToken");
		Objects.requireNonNull(message, "message");
		this.reply(replyToken, Collections.singletonList(message));
	}

	/**
	 * 回應訊息(多筆) + 是否被通知
	 * 
	 * @param replyToken
	 * @param messages
	 * @param notificationDisabled 用戶是否會被通知(true: 不會被通知，false: 會被通知)
	 */
	public void reply(String replyToken,
			List<Message> messages,
			boolean notificationDisabled) {
		try {
			Objects.requireNonNull(replyToken, "replyToken");
			Objects.requireNonNull(messages, "messages");
			Result<ReplyMessageResponse> apiResponse = messagingApiClient
					.replyMessage(new ReplyMessageRequest(replyToken, messages, notificationDisabled))
					.get();
			log.debug("Sent messages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
