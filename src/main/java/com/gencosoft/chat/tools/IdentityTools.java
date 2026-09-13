package com.gencosoft.chat.tools;

import org.springframework.ai.tool.annotation.Tool;

public class IdentityTools {

    @Tool(description = "Get the name of this assistant. Use only when the user asks who or what you are.")
    String sayMyName() {
        return "Gencosoft-Chat";
    }

}
