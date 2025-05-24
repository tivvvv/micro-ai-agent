package com.tiv.microaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class ChatAppTest {

    @Resource
    private ChatApp chatApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String msg = "你好,我是超哥,想做java开发";
        String answer = chatApp.doChat(msg, chatId);
        Assertions.assertNotNull(answer);

        // 第二轮
        msg = "进入大厂做后端开发,需要掌握哪些内容";
        answer = chatApp.doChat(msg, chatId);
        Assertions.assertNotNull(answer);

        // 第三轮
        msg = "我的名字是什么?刚刚跟你说了,帮我回忆一下";
        answer = chatApp.doChat(msg, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void testChatWithStructuredOutput() {
        String chatId = UUID.randomUUID().toString();
        String msg = "我是超哥,我现在在一家大厂做java开发,我该怎么提升自己的水平";
        ChatApp.JavaLearningGuide javaLearningGuide = chatApp.doChatWithStructuredOutput(msg, chatId);
        Assertions.assertNotNull(javaLearningGuide);
    }

    @Test
    void testChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String msg = "怎样深入掌握JVM原理与调优";
        String answer = chatApp.doChatWithRag(msg, chatId);
        Assertions.assertNotNull(answer);
    }
}