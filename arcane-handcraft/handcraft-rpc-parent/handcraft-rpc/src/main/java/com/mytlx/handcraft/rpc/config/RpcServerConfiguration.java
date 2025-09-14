package com.mytlx.handcraft.rpc.config;

import com.mytlx.handcraft.rpc.server.RpcServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 15:29:16
 */
@Configuration
public class RpcServerConfiguration {

    @Value("${handcraft.rpc.server.port}")
    private int port;

    @Value("${handcraft.rpc.server.worker}")
    private int workerGroupSize;

    @Value("${handcraft.rpc.server.backlog}")
    private int backlogSize;

    @Bean
    public RpcServer rpcServer() {
        RpcServer rpcServer = new RpcServer()
                .setPort(port)
                .setWorkerGroupSize(workerGroupSize)
                .setBacklogSize(backlogSize);
        rpcServer.startServer();

        return rpcServer;
    }

}
