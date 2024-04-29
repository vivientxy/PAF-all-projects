package paf.day8.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

// @ControllerAdvice // handles any exception
@Controller
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({DataAccessException.class})
    public ModelAndView handleDataAccessException(HttpServletRequest req, DataAccessException ex) {
        // req.getParameterMap();
        return null;
    }
    
}
