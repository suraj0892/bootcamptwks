package com.tw.bootcamp.bookshop.book;

import com.tw.bootcamp.bookshop.book.exceptions.InvalidFileFormatException;
import com.tw.bootcamp.bookshop.book.exceptions.InvalidRequestParameterException;
import com.tw.bootcamp.bookshop.book.exceptions.NoBooksFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> fetchAll() {
        return bookRepository.findAllByOrderByNameAsc();
    }

    public List<Book> upload(@RequestParam("file") MultipartFile file) throws IOException, InvalidFileFormatException {

        if(isCsvFile(file))
            throw new InvalidFileFormatException("Only Valid csv files are allowed");

        try{
            List<Book> newBooks = BookCsvReader.readFromFile(file);

            updateBookForMatchingIsbn(newBooks);

            return bookRepository.saveAll(newBooks);
        }
        catch(TransactionSystemException ex){
            //need to implement Custom Exception
            throw new NumberFormatException();
        }
    }

    private boolean isCsvFile(MultipartFile file) {
        return !"text/csv".equalsIgnoreCase(file.getContentType());
    }

    private void updateBookForMatchingIsbn(List<Book> newBooks) {
        List<Long> newBooksIsbn = newBooks.stream().map(Book::getIsbn13).collect(Collectors.toList());
        List<Book> oldBooksMatchingIsbn = bookRepository.findAllByIsbn(newBooksIsbn);
        for(Book newBook: newBooks){
            if(oldBooksMatchingIsbn.contains(newBook)){
                Book oldBook = getMatchingBook(oldBooksMatchingIsbn, newBook);
                newBook.update(oldBook);
            }
        }
    }

    private Book getMatchingBook(List<Book> oldBooksMatchingIsbn, Book newBook) {
        return oldBooksMatchingIsbn.stream()
                .filter(oldBook -> oldBook.equals(newBook))
                .findFirst().get();
    }

    public List<Book> search(String title, String author) throws InvalidRequestParameterException, NoBooksFoundException {
        if (StringUtils.isBlank(title) && StringUtils.isBlank(author)) {
            throw new InvalidRequestParameterException("Book title or author should be provided");
        }

        List<Book> foundBooks = bookRepository.
                findByNameStartingWithIgnoreCaseAndAuthorNameStartingWithIgnoreCase(
                        Optional.ofNullable(title).orElse(""),
                        Optional.ofNullable(author).orElse(""));
        if(foundBooks.size() <=0){
            throw new NoBooksFoundException("No Match Found!");
        }
        return foundBooks;
    }

    public List<Book> fetchAllByOrder(String sortBy) {
        List<Book> books = fetchAll();
        if (sortBy.equalsIgnoreCase("asc")) {
            books.sort(new BookPriceComparator());
        } else if (sortBy.equalsIgnoreCase("desc")) {
            books.sort(new BookPriceComparator().reversed());
        }
        return books;
    }
    public Optional<Book> getById(long bookId) {
        return bookRepository.findById(bookId);
    }

    public void reduceBookInventoryCountBy(int count, Book book) {
        book.setQuantity(book.getQuantity() - count);
        bookRepository.save(book);
    }
}
