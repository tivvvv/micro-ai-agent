package com.tiv.microaiagent.app;

import com.tiv.microaiagent.advisor.MyLoggerAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Slf4j
@Component
public class ChatApp {

    private final ChatClient chatClient;

    private final static String DEFAULT_SYSTEM_PROMPT = "你是java专家";

    /**
     * 初始化对话客户端
     *
     * @param chatModel
     */
    public ChatApp(@Qualifier("dashscopeChatModel") ChatModel chatModel) {
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(chatModel)
                .defaultSystem(DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory), new MyLoggerAdvisor())
                .build();
    }

    /**
     * 基础对话
     *
     * @param msg
     * @param chatId
     * @return
     */
    public String doChat(String msg, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(msg)
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String result = chatResponse.getResult().getOutput().getText();
        log.info("result: {}", result);
        return result;
    }

}
