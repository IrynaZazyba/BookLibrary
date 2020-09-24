package com.itechart.javalab.library.service;

import com.itechart.javalab.library.dao.AlterBookDao;
import com.itechart.javalab.library.dao.ReceiveBookDao;
import com.itechart.javalab.library.dto.BookDto;
import com.itechart.javalab.library.model.Author;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BookFilter;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.service.impl.DefaultBookService;
import com.itechart.javalab.library.service.impl.DefaultUploadFileService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import javax.servlet.http.Part;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.mockito.Mockito.*;

public class DefaultBookServiceTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Mock
    private AlterBookDao alterBookDao = mock(AlterBookDao.class);
    @Mock
    private ReceiveBookDao receiveBookDao = mock(ReceiveBookDao.class);
    private final BookService bookService = DefaultBookService.getInstance();
    @Mock
    private final UploadFileService uploadFileService = mock(DefaultUploadFileService.class);


    @Test
    public void testGetAllBooksNegative() {
        Whitebox.setInternalState(bookService, "receiveBookDao", receiveBookDao);
        boolean isAvailableOnly = true;
        Paginator paginator = new Paginator("10", "1");
        Mockito.when(receiveBookDao.getBooks(paginator, isAvailableOnly)).thenReturn(Optional.empty());
        Assert.assertEquals(bookService.getBooks(paginator, isAvailableOnly), Optional.empty());
    }

    @Test
    public void testGetAllBooksPositive() {
        boolean isAvailableOnly = true;
        Paginator paginator = new Paginator("2", "1");

        Whitebox.setInternalState(bookService, "receiveBookDao", receiveBookDao);

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
                .publishDate(LocalDate.parse("2015-08-25", formatter))
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
                .publishDate(LocalDate.parse("2020-08-26", formatter))
                .inStock(7)
                .author(authors)
                .build();
        books.add(javaBook);
        books.add(christmasBook);

        Mockito.when(receiveBookDao.getBooks(paginator, isAvailableOnly)).thenReturn(Optional.of(books));
        Assert.assertEquals(bookService.getBooks(paginator, isAvailableOnly), Optional.of(books));
    }

    @Test
    public void testGetNumberOfBooksRecordsPositive() {
        boolean isAvailableOnly = true;
        Whitebox.setInternalState(bookService, "receiveBookDao", receiveBookDao);
        Mockito.when(receiveBookDao.getNumberBooksRecords(isAvailableOnly)).thenReturn(Optional.of(2));

        Assert.assertEquals(bookService.getNumberBooksRecords(isAvailableOnly), Optional.of(2));
    }

    @Test
    public void testGetNumberOfBooksRecordsNegative() {
        boolean isAvailableOnly = true;
        Whitebox.setInternalState(bookService, "receiveBookDao", receiveBookDao);
        Mockito.when(receiveBookDao.getNumberBooksRecords(isAvailableOnly)).thenReturn(Optional.empty());

        Assert.assertEquals(bookService.getNumberBooksRecords(isAvailableOnly), Optional.empty());
    }

    @Test
    public void testFindBooksByParametersPositive() {
        Set<Author> bookAuthors = new HashSet<>();
        bookAuthors.add(new Author(3, "Анджей Сапковский"));
        Book firstBook = Book
                .builder()
                .id(1)
                .title("Ведьмак. Меч Предназначения")
                .author(bookAuthors)
                .publishDate(LocalDate.parse("2015-08-25", formatter))
                .inStock(10)
                .build();
        Book secondBook = Book
                .builder()
                .id(1)
                .title("Ведьмак. Сезон гроз")
                .author(bookAuthors)
                .publishDate(LocalDate.parse("2015-08-25", formatter))
                .inStock(3)
                .build();
        List<Book> foundBooks = new ArrayList<>();
        foundBooks.add(firstBook);
        foundBooks.add(secondBook);

        BookFilter bookFilter = BookFilter.builder()
                .isAvailableOnly(true)
                .bookTitle("Ведьмак")
                .bookGenre("")
                .bookDescription("")
                .bookAuthor("")
                .build();

        Paginator paginator = new Paginator("2", "2");
        Whitebox.setInternalState(bookService, "receiveBookDao", receiveBookDao);
        Mockito.when(receiveBookDao.
                findBooksByParameters(paginator, bookFilter))
                .thenReturn(Optional.of(foundBooks));

        Assert.assertEquals(bookService.findBooksByParameters(paginator, bookFilter), Optional.of(foundBooks));
    }

    @Test
    public void testFindBooksByParametersNegative() {

        BookFilter bookFilter = BookFilter.builder()
                .isAvailableOnly(true)
                .bookTitle("Ведьмак")
                .bookGenre("")
                .bookDescription("")
                .bookAuthor("")
                .build();

        Paginator paginator = new Paginator("2", "2");

        Whitebox.setInternalState(bookService, "receiveBookDao", receiveBookDao);
        Mockito.when(receiveBookDao.
                findBooksByParameters(paginator, bookFilter))
                .thenReturn(Optional.empty());

        Assert.assertEquals(bookService.findBooksByParameters(paginator, bookFilter), Optional.empty());
    }

    @Test
    public void getNumberFoundBooksRecordsPositive() {
        BookFilter bookFilter = new BookFilter();
        Whitebox.setInternalState(bookService, "receiveBookDao", receiveBookDao);
        Mockito.when(receiveBookDao.getNumberFoundBooksRecords(bookFilter)).thenReturn(Optional.of(5));
        Assert.assertEquals(bookService.getNumberFoundBooksRecords(bookFilter), Optional.of(5));
    }

    @Test
    public void getBookByIdPositive() {
        int bookId = 5;
        Book book = Book.builder().id(5).build();
        Whitebox.setInternalState(bookService, "receiveBookDao", receiveBookDao);
        Mockito.when(receiveBookDao.getBookById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(receiveBookDao.getEarliestDueDate(bookId)).thenReturn(Optional.of(LocalDateTime.now()));
        Assert.assertEquals(bookService.getBookById(bookId), Optional.of(book));
    }

    @Test
    public void getBookByIdNegative() {
        int bookId = 5;
        Book book = null;
        Whitebox.setInternalState(bookService, "receiveBookDao", receiveBookDao);
        Mockito.when(receiveBookDao.getBookById(bookId)).thenReturn(Optional.ofNullable(book));
        Mockito.when(receiveBookDao.getEarliestDueDate(bookId)).thenReturn(Optional.of(LocalDateTime.now()));
        Assert.assertEquals(bookService.getBookById(bookId), Optional.ofNullable(book));
    }

    @Test
    public void updateBookInfoPositive() {
        BookDto bookDto = BookDto.builder()
                .id(5)
                .title("Алые паруса")
                .publishDate("2019-03-01")
                .author("Александр Грин")
                .genre("Классика, проза")
                .pageCount(255)
                .totalAmount(5)
                .publisher("АСТ")
                .isbn("1547852145")
                .description("")
                .build();
        String anyPath = "/book";
        String fileName = "5.png";
        Whitebox.setInternalState(bookService, "alterBookDao", alterBookDao);
        Mockito.when(alterBookDao.updateBookInfo(Book.buildFrom(bookDto))).thenReturn(Optional.of(true));
        doNothing().when(uploadFileService)
                .uploadFile(eq(anyPath), Mockito.any(Part.class), eq(fileName));
        Assert.assertEquals(bookService.updateBookInfo(bookDto, Mockito.any(Part.class), fileName),
                Optional.of(true));
    }

    @Test
    public void updateBookInfoNegative() {
        BookDto bookDto = BookDto.builder()
                .id(5)
                .title("Алые паруса")
                .publishDate("2019-03-01")
                .author("Александр Грин")
                .genre("Классика, проза")
                .pageCount(255)
                .totalAmount(5)
                .publisher("АСТ")
                .isbn("1547852145")
                .description("")
                .build();
        String anyPath = "/book";
        String fileName = "5.png";
        Whitebox.setInternalState(bookService, "alterBookDao", alterBookDao);
        Mockito.when(alterBookDao.updateBookInfo(Book.buildFrom(bookDto))).thenReturn(Optional.empty());
        doNothing().when(uploadFileService)
                .uploadFile(eq(anyPath), Mockito.any(Part.class), eq(fileName));
        Assert.assertEquals(Optional.empty(), bookService.updateBookInfo(bookDto, Mockito.any(Part.class), fileName));
    }

    @Test
    public void deleteBooksPositive() {
        int[] ids = {1, 5, 7};
        Whitebox.setInternalState(bookService, "alterBookDao", alterBookDao);
        Mockito.when(alterBookDao.deleteBooks(ids)).thenReturn(true);
        Assert.assertTrue(bookService.deleteBooks(ids));
    }

    @Test
    public void deleteBooksNegative() {
        int[] ids = {1, 5, 7};
        Whitebox.setInternalState(bookService, "alterBookDao", alterBookDao);
        Mockito.when(alterBookDao.deleteBooks(ids)).thenReturn(false);
        Assert.assertFalse(bookService.deleteBooks(ids));
    }

    @Test
    public void createBook() {
        BookDto bookDto = BookDto.builder()
                .title("Алые паруса")
                .publishDate("2019-03-01")
                .author("Александр Грин")
                .genre("Классика, проза")
                .pageCount(255)
                .totalAmount(5)
                .publisher("АСТ")
                .isbn("1547852145")
                .description("")
                .build();
        String anyPath = "/book";
        String fileName = "5.png";
        Whitebox.setInternalState(bookService, "alterBookDao", alterBookDao);
        Mockito.when(alterBookDao.createBook(Book.buildFrom(bookDto))).thenReturn(5);
        doNothing().when(uploadFileService)
                .uploadFile(eq(anyPath), Mockito.any(Part.class), eq(fileName));
        Assert.assertEquals(bookService.createBook(bookDto, Mockito.any(Part.class), anyPath), 5);
    }

}
