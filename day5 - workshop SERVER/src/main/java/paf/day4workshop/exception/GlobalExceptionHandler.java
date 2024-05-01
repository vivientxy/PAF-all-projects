package paf.day4workshop.exception;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ModelAndView handleException(Exception ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error.html");
        log.error(ex.getMessage());
        mav.addObject("error_date", new Date());
        mav.addObject("error_message", ex.getMessage());
        mav.addObject("error_statusCode", HttpStatus.NOT_FOUND);
        mav.addObject("url", request.getRequestURL().toString());
        return mav;
    }
}
