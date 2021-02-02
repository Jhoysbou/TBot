package com.jhoysbou.TBot;

import com.jhoysbou.TBot.models.MenuAttachmentsDto;
import com.jhoysbou.TBot.services.VkApi.GroupApi;
import com.jhoysbou.TBot.utils.AttachmentExtractor;
import com.jhoysbou.TBot.utils.SimpleAttachmentExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class TBotApplicationTests {
	private final AttachmentExtractor extractor;
	private final GroupApi api;

	@Autowired
	TBotApplicationTests(AttachmentExtractor extractor, GroupApi api) {
		this.extractor = extractor;
		this.api = api;
	}

	@Test
	void extractorTest() {
		final String text = "https://vk.com/theormech?w=wall-8812367_2324\n text\n https://stackoverflow.com/questions/10601672/posting-url-with-string-parameter-that-contains-line-breaks-using-java/10601740";
		MenuAttachmentsDto result = extractor.parse(text);
	}

	@Test
	void confirmationCodeTest() {
		try {
			String code = api.getConfirmationCode();
			System.out.println(code);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
