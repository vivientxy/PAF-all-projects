package paf.day8.controller;

import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class IndexController {

    private Logger logger = Logger.getLogger(IndexController.class.getName());
    private org.slf4j.Logger logger2 = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // @Autowired
    // private CommonsRequestLoggingFilter logger;

    @GetMapping({"/","/index.html"})
    public String getIndex(@RequestParam String param) {
        logger.info(">>>>> in index.html");
        logger.warning(">>>>> this is a warning");
        return "index";
    }
    
}
