package com.gencosoft.chat;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThinkingProbeTest {

	@Autowired
	OllamaChatModel model;

	@Autowired
	ChatClient.Builder builder;

	@Test
	void probe() {
		System.out.println(">>> MODEL DEFAULT OPTIONS think = " + ((org.springframework.ai.ollama.api.OllamaChatOptions) model.getDefaultOptions()).getThinkOption());

		System.out.println(">>> RAW MODEL STREAM");
		model.stream(new Prompt(new UserMessage("hello")))
				.doOnNext(r -> {
					var g = r.getResult();
					if (g != null) {
						System.out.println("  msgMeta=" + g.getOutput().getMetadata()
								+ " genMeta=" + g.getMetadata()
								+ " text=" + g.getOutput().getText());
					}
				})
				.blockLast();

		System.out.println(">>> CHAT CLIENT STREAM");
		builder.build().prompt("hello").stream().chatResponse()
				.doOnNext(r -> {
					var g = r.getResult();
					if (g != null) {
						System.out.println("  msgMeta=" + g.getOutput().getMetadata()
								+ " text=" + g.getOutput().getText());
					}
				})
				.blockLast();
	}
}
