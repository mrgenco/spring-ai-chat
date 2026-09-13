package com.gencosoft.chat.tools;

import org.springframework.ai.tool.annotation.Tool;

public class IdentityTools {

    @Tool(description = "Get your name information")
    String sayMyName(String time) {
        return "Gencosoft-Chat";
    }

}