package kr.co.suitcarrier.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "basicError", description = "기본 에러 처리 API")
@Controller
public class BasicErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        return "Error Page";
    }
}
