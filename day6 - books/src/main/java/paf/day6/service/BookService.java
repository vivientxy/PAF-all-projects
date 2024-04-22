package paf.day6.service;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import paf.day6.model.Book;
import paf.day6.repo.BookRepo;

@Service
public class BookService {

    @Autowired
    BookRepo repo;

    public List<Book> getBooksByTitle(String title) {
        List<Book> books = new LinkedList<>();
        for (Document doc : repo.findBooksByTitle(title)) {
            books.add(new Book(doc));
        }
        return books;
    }

    public Book findBookByBookId(int bookId) {
        return new Book(repo.findBookByBookId(bookId));
    }
    
}
