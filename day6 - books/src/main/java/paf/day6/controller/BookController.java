package paf.day6.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import paf.day6.model.Book;
import paf.day6.service.BookService;

@Controller
public class BookController {

    @Autowired
    private BookService svc;

    @GetMapping("/books")
    public ModelAndView getBookList(@RequestParam String title) {
        ModelAndView mav = new ModelAndView("books");
        List<Book> bookList = svc.getBooksByTitle(title);
        mav.addObject("title", title);
        mav.addObject("bookList", bookList);
        return mav;
    }

    @GetMapping("/books/{bookID}")
    public ModelAndView getBookDetails(@PathVariable int bookID) {
        ModelAndView mav = new ModelAndView("bookDetail");
        Book book = svc.findBookByBookId(bookID);
        mav.addObject("book", book);
        return mav;
    }
    
}
