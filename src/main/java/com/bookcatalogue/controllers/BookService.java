package com.bookcatalogue.controllers;

import com.bookcatalogue.dao.BookRepository;


import com.bookcatalogue.dto.Book;
import com.bookcatalogue.exception.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Service
public final class BookService {

    @Autowired
    private BookRepository bookRepo;

//  get all books with pagination options, and sort option
	@RequestMapping("/books")
	List<Book> getAllBooks() {
		List<Book> bookList = bookRepo.findAll();
		return bookList;
	}

//	get book by id
    @RequestMapping("/books/{id}")
    Optional<Book> getBookById(@PathVariable("id") final Long id) {
		Optional<Book> book = bookRepo.findById(id);
		if (book.isPresent()) {
			return book;
		} else {
			throw new BookNotFoundException("No book with id: "
					+ id);
		}
    }

//	get books by author
	@RequestMapping("/books/authors/{author}")
	List<Book> findBooksByAuthor(@PathVariable("author")
								 final String author) {
		List<Book> bookList = bookRepo.findByAuthor(author);
		if (!bookList.isEmpty()) {
			return bookList;
		} else {
			throw new BookNotFoundException("No book found from"
					+ " the author: " + author);
		}
	}

//	get books by title
	@RequestMapping("/books/titles/{title}")
	List<Book> findBooksByTitle(@PathVariable("title")
								final String title) {
		List<Book> bookList = bookRepo.findByTitle(title);
		if (!bookList.isEmpty()) {
			return bookList;
		} else {
			throw new BookNotFoundException("No book found with"
					+ " the title: " + title);
		}
	}

//	get books by ISBN number
	@RequestMapping("/books/isbn/{isbn}")
	List<Book> findBookByIsbn(@PathVariable("isbn")
							  final String isbn) {
		List<Book> bookList = bookRepo.findByIsbn(isbn);
		if (!bookList.isEmpty()) {
			return bookList;
		} else {
			throw new BookNotFoundException("No book found with"
					+ " ISBN number: " + isbn);
		}
	}

    @PostMapping("/books")
    ResponseEntity<Book> createBook(@Valid @RequestBody final Book book) {
        Book savedBook = bookRepo.save(book);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedBook.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

	@PutMapping("books/{id}")
	public Book updateBookById(@Valid @RequestBody final Book book,
							   @PathVariable("id") final Long id) {
		Optional<Book> savedBook = bookRepo.findById(id);
		if (savedBook.isPresent()) {
			Book existingBook = savedBook.get();
			existingBook.setIsAvailable(book.getIsAvailable());
			bookRepo.save(existingBook);
			return existingBook;
		} else {
			throw new BookNotFoundException("No book with id: "
					+ id);
		}
	}

    @DeleteMapping("/books/{id}")
	void deleteBookById(@PathVariable final Long id) {
		Optional<Book> book = bookRepo.findById(id);
		if (book.isPresent()) {
			Book existingBook = book.get();
			bookRepo.delete(existingBook);
		} else {
			throw new BookNotFoundException("No book with id: "
					+ id);
		}
	}
}
