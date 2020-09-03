package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.impl.SqlBookDao;
import com.itechart.javalab.library.model.Author;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.service.impl.DefaultBookService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.mockito.Mockito.mock;

public class DefaultBookServiceTest {

    @Mock
    private BookDao mockBookDao = mock(SqlBookDao.class);
    private BookService bookService = DefaultBookService.getInstance();


    @Test
    public void testGetAllBooksNegative() {
        Whitebox.setInternalState(bookService, "bookDao", mockBookDao);
        boolean isAvailableOnly = true;
        Paginator paginator = new Paginator("10", "1");
        Mockito.when(bookService.getBooks(paginator, isAvailableOnly)).thenReturn(Optional.empty());
        Assert.assertEquals(bookService.getBooks(paginator, isAvailableOnly), Optional.empty());
    }

    @Test
    public void testGetAllBooksPositive() {
        boolean isAvailableOnly = true;
        Paginator paginator = new Paginator("2", "1");

        Whitebox.setInternalState(bookService, "bookDao", mockBookDao);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<Book> books = new ArrayList<>();
        Set<Author> javaBookAuthors = new HashSet<>();
        Author javaBookFirstAuthor = Author
                .builder()
                .id(6)
                .name("Берт Бейтс")
                .build();
        Author javaBookSecondAuthor = Author
                .builder()
                .id(7)
                .name("Кэти Сьерра")
                .build();
        javaBookAuthors.add(javaBookFirstAuthor);
        javaBookAuthors.add(javaBookSecondAuthor);
        Book javaBook = Book.builder()
                .id(1)
                .title("Изучаем Java")
                .publishDate(LocalDateTime.parse("2015-08-25 00:00", formatter))
                .inStock(1)
                .author(javaBookAuthors)
                .build();
        Set<Author> authors = new HashSet<>();
        Author author = Author
                .builder()
                .id(8)
                .name("Charles Dickens")
                .build();
        authors.add(author);
        Book christmasBook = Book.builder()
                .id(2)
                .title("A Christmas Carol")
                .publishDate(LocalDateTime.parse("2020-08-26 00:00", formatter))
                .inStock(7)
                .author(authors)
                .build();
        books.add(javaBook);
        books.add(christmasBook);

        Mockito.when(bookService.getBooks(paginator, isAvailableOnly)).thenReturn(Optional.of(books));
        Assert.assertEquals(bookService.getBooks(paginator, isAvailableOnly), Optional.of(books));
    }


    @Test
    public void testGetNumberOfBooksRecordsPositive() {
        boolean isAvailableOnly = true;
        Whitebox.setInternalState(bookService, "bookDao", mockBookDao);
        Mockito.when(bookService.getNumberOfBooksRecords(isAvailableOnly)).thenReturn(Optional.of(2));

        Assert.assertEquals(bookService.getNumberOfBooksRecords(isAvailableOnly), Optional.of(2));
    }

    @Test
    public void testGetNumberOfBooksRecordsNegative() {
        boolean isAvailableOnly = true;
        Whitebox.setInternalState(bookService, "bookDao", mockBookDao);
        Mockito.when(bookService.getNumberOfBooksRecords(isAvailableOnly)).thenReturn(Optional.empty());

        Assert.assertEquals(bookService.getNumberOfBooksRecords(isAvailableOnly), Optional.empty());
    }


}
