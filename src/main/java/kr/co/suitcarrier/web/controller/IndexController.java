package kr.co.suitcarrier.web.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    // react에서 localhost:3000/api/hello 호출 시에 사용
    @GetMapping("/api/hello")
    // CORS 허용
    @CrossOrigin(origins = "http://localhost:3000")
    public String test() {
        return "Hello, world!";
    }

}
