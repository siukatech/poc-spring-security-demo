package com.siukatech.poc.spring.security.demo.controller;

import com.siukatech.poc.spring.security.demo.model.SimpleReq;
import com.siukatech.poc.spring.security.demo.model.SimpleRet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @GetMapping(value = "/info")
    public ResponseEntity<SimpleRet> getUserInfo(Authentication authentication) {
        SimpleRet simpleRet = new SimpleRet();
        simpleRet.setUserId(authentication.getName());
        simpleRet.setReqId(simpleRet.getReqId());
        return ResponseEntity.ok(simpleRet);
    }

    @PostMapping(value = "/info")
    public ResponseEntity<SimpleRet> postUserInfo(Authentication authentication
            , @RequestBody SimpleReq simpleReq) {
        SimpleRet simpleRet = new SimpleRet(authentication.getName(), simpleReq.getId());
        return ResponseEntity.ok(simpleRet);
    }

}

