package com.gencosoft.chat;

import com.gencosoft.chat.tools.IdentityTools;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ChatClientController {


	private final ChatClient chatClient;

    public ChatClientController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/simple/chat")
	public String simpleChat(@RequestParam(value = "prompt") String prompt) {
		return chatClient.prompt(prompt).call().content();
	}


	@GetMapping("/stream/chat")
	public Flux<String> streamChat(
			@RequestParam(value = "prompt") String prompt,
			HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		return chatClient.prompt(prompt).tools(new IdentityTools()).stream().content();
	}


}