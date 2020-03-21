package com.vidviz.back.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    // Required because of 'mode: history' usage in frontend routing
    @RequestMapping(value = "{_:^(?!index\\.html|api|console).$}")
    public String redirectApi() {
        //LOG.info("URL entered directly into the Browser, so we need to redirect...");
        return "forward:/";
    }

    //@CrossOrigin(origins = "http://localhost:8080")
    @RequestMapping(path = "api/hello")
    public @ResponseBody
    String sayHello() {
        System.out.println("caca");
        LOG.info("GET called on /hello resource");
        return "qqqqqqq";
    }
}
