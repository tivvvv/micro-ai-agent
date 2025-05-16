package com.tiv.microaiagent.app;

import com.tiv.microaiagent.advisor.LoggingAdvisor;
import com.tiv.microaiagent.advisor.ReReadingAdvisor;
import com.tiv.microaiagent.memory.FileBasedChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Slf4j
@Component
public class ChatApp {

    private final ChatClient chatClient;

    private final static String DEFAULT_SYSTEM_PROMPT = "你是Java专家";

    private final static String DEFAULT_STRUCTURED_OUTPUT_SYSTEM_PROMPT = "你是Java专家,每次对话后都要生成Java学习指南,标题为{用户名}的Java学习指南,内容为学习建议列表";

    /**
     * 初始化对话客户端
     *
     * @param chatModel
     */
    public ChatApp(@Qualifier("dashscopeChatModel") ChatModel chatModel) {
        String fileDir = System.getProperty("user.dir") + "/tmp/memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        chatClient = ChatClient.builder(chatModel)
                .defaultSystem(DEFAULT_SYSTEM_PROMPT)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory),
                        new LoggingAdvisor(),
                        new ReReadingAdvisor())
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

    public record JavaLearningGuide(String title, List<String> suggestions) {

    }

    public JavaLearningGuide doChatWithStructuredOutput(String msg, String chatId) {
        JavaLearningGuide javaLearningGuide = chatClient.prompt()
                .system(DEFAULT_STRUCTURED_OUTPUT_SYSTEM_PROMPT)
                .user(msg)
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId).param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(JavaLearningGuide.class);
        log.info("javaLearningGuide: {}", javaLearningGuide);
        return javaLearningGuide;
    }

}
