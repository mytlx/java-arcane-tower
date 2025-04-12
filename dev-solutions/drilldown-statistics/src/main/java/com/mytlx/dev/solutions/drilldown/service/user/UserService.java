package com.mytlx.dev.solutions.drilldown.service.user;

import com.mytlx.dev.solutions.drilldown.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 15:20
 */
@Slf4j
@Service
public class UserService {

    public User getPlatformUser(Long platformUserId) {
        return new User()
                .setId(platformUserId)
                .setName("平台用户");
    }

    public User getLoginUser(Long loginUserId) {
        return new User()
                .setId(loginUserId)
                .setName("登录用户");
    }

    public List<User> getListByIds(List<Object> ids) {
        return null;
    }
}
