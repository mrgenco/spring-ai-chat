package com.gencosoft.chat;

import com.gencosoft.chat.tools.DateTimeTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.augment.AugmentedToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootApplication
public class ChatApplication {

	private static final Logger log = LoggerFactory.getLogger(ChatApplication.class);

	/** Key under which OllamaChatModel exposes reasoning tokens on each streamed message. */
	private static final String THINKING = "thinking";

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

	@Bean
	CommandLineRunner cli(ChatClient.Builder builder) {
		return args -> {

			AugmentedToolCallbackProvider<AgentThinking> provider = AugmentedToolCallbackProvider
					.<AgentThinking>builder()
					.toolObject(new DateTimeTools())     // wrap the original tools
					.argumentType(AgentThinking.class)  // augmentation schema type
					.argumentConsumer(event -> {        // optional consumer of augmented content
						AgentThinking thinking = event.arguments();
						log.info("Tool: {} | Reasoning: {}", event.toolDefinition().name(), thinking.innerThought());
					})
					.removeExtraArgumentsAfterProcessing(true)
					.build();

			var chat = builder.defaultTools(provider)
					.build();

			var scanner = new Scanner(System.in);
			System.out.println("\nLet's chat!");
			while (true) {
				System.out.print("\nUSER: ");
				var thinking = new AtomicBoolean(false);
				var answering = new AtomicBoolean(false);

				chat.prompt(scanner.nextLine())
						.stream()
						.chatResponse()
						.doOnNext(response -> {
							var generation = response.getResult();
							if (generation == null) {
								return;
							}
							var output = generation.getOutput();

							if (output.getMetadata().get(THINKING) instanceof String reasoning
									&& !reasoning.isEmpty()) {
								if (thinking.compareAndSet(false, true)) {
									System.out.println("Thinking...");
								}
								System.out.print(reasoning);
							}

							var text = output.getText();
							if (text != null && !text.isEmpty()) {
								if (answering.compareAndSet(false, true)) {
									if (thinking.get()) {
										System.out.println("\n...done thinking.");
									}
									System.out.print("\nASSISTANT: ");
								}
								System.out.print(text);
							}
						})
						.blockLast();

				System.out.println();
			}
		};
	}
}
