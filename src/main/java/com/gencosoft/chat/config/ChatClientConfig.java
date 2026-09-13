package com.gencosoft.chat.config;

import com.gencosoft.chat.tools.DateTimeTools;
import com.gencosoft.chat.tools.IdentityTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
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
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultTools(new DateTimeTools())
                .build();
    }

    @Bean
    public ChatClient chatClientWithIdentityTools(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultTools(new IdentityTools())
                .build();
    }

}
