package com.mytlx.arcane.study.netty.practice.chat.client;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-07 16:03:14
 */
@Slf4j
public class MultiClientLauncher {

    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        try (InputStream is = MultiClientLauncher.class.getClassLoader().getResourceAsStream("clients.yml")) {
            if (is == null) {
                throw new RuntimeException("配置文件 clients.yml 未找到");
            }
            ClientConfig config = yaml.loadAs(is, ClientConfig.class);

            for (ClientConfig.ClientInfo client : config.getClients()) {
                new Thread(() -> {
                    try {
                        ChatClient.start(client);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, "client-" + client.getUsername()).start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
