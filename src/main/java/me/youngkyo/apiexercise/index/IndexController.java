package me.youngkyo.apiexercise.index;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @ResponseBody
    @GetMapping("/docs/index")
    public String index() {
        return "index";
    }
}
