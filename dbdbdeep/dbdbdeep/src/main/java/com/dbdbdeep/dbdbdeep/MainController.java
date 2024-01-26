package com.dbdbdeep.dbdbdeep;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MainController {

    @GetMapping("/home")
    public String str() {
        return "home";
    }
}
