package com.design_pattern.sidecar_pattern.controller.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @GetMapping("/common")
    public String getCommon() {
        return "현재 서비스가 월활하지 않습니다. 잠시 후 다시 시도해 주세요.";
    }
}
