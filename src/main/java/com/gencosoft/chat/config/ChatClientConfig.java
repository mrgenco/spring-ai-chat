package com.gencosoft.chat.config;

import com.gencosoft.chat.tools.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    ChatClient defaultChatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public ChatClient chatClientWithDateTimeTools(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultTools(new DateTimeTools())
                .build();
    }

}
