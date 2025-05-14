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

}