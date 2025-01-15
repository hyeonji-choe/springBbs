package org.study.springbbs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.study.springbbs.model.UserInfo;
import org.study.springbbs.service.PostService;


@Controller
public class WebController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserInfo user, Model model){
        model.addAttribute("loginYn", !ObjectUtils.isEmpty(user));
        return "/index";
    }

}
