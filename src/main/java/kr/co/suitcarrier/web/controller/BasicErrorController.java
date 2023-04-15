package kr.co.suitcarrier.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BasicErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        return "Error Page";
    }
}
