package com.mytlx.handcraft.rpc.demo.user.controller;

import com.mytlx.handcraft.rpc.demo.common.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-14 16:06:04
 */
@RequestMapping("/demo/user")
@RestController
public class UserController {

    @Autowired
    private UserDetailService userDetailService;

    @GetMapping("/detail")
    public String getBookingDetails() {
        return userDetailService.checkUser();
    }

}
