package paf.day2.exception;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({RSVPNotFoundException.class})
    public ModelAndView handleExceptionModel(Exception ex, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error_page");
        LOGGER.error(ex.getMessage());
        mav.addObject("error_date", new Date());
        mav.addObject("error_message", ex.getMessage());
        mav.addObject("error_statusCode", HttpStatus.NOT_FOUND);
        mav.addObject("url", request.getRequestURL().toString());
        return mav;
    }

    @ExceptionHandler(RSVPNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleExceptionRest(Exception ex, HttpServletRequest request) {
        ErrorMessage errMsg = new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getRequestURL().toString());
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<ErrorMessage>(errMsg, HttpStatus.NOT_FOUND);
    }
}
