package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.BookDao;
import com.itechart.javalab.library.dao.impl.SqlBookDao;
import com.itechart.javalab.library.dto.BookDto;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BookFilter;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.UploadFileService;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.Part;
import java.util.List;
import java.util.Optional;

public class DefaultBookService implements BookService {

    private final BookDao bookDao;
    private final UploadFileService uploadFileService;
    private static volatile BookService instance;

    private DefaultBookService(BookDao bookDao, UploadFileService uploadFileService) {
        this.bookDao = bookDao;
        this.uploadFileService = uploadFileService;
    }

    public static BookService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    instance = new DefaultBookService(SqlBookDao.getInstance(), DefaultUploadFileService.getInstance());
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<List<Book>> getBooks(Paginator paginator, boolean isAvailableOnly) {
        return bookDao.getBooks(paginator, isAvailableOnly);
    }

    @Override
    public Optional<Integer> getNumberBooksRecords(boolean isAvailableOnly) {
        return bookDao.getNumberBooksRecords(isAvailableOnly);
    }

    @Override
    public Optional<List<Book>> findBooksByParameters(Paginator paginator, BookFilter bookFilter) {
        return bookDao.findBooksByParameters(paginator, bookFilter);
    }

    @Override
    public Optional<Integer> getNumberFoundBooksRecords(BookFilter bookFilter) {
        return bookDao.getNumberFoundBooksRecords(bookFilter);
    }

    @Override
    public Optional<Book> getBookById(int bookId) {
        Optional<Book> bookById = bookDao.getBookById(bookId);
        if (bookById.isPresent()) {
            Book book = bookById.get();
            calculateBookStatus(book);
            return Optional.of(book);
        }
        return bookById;
    }

    @Override
    public Optional<Boolean> updateBookInfo(BookDto bookDto, Part part, String savePath) {
        Book book = Book.buildFrom(bookDto);
        String nameForDb = defineFileName(part, book);
        Optional<Boolean> updateResult = bookDao.updateBookInfo(book);
        if (part != null && updateResult.isPresent()) {
            if (part.getSize() != 0) {
                uploadFileService.uploadFile(savePath, part, nameForDb);
            }
        }
        return updateResult;
    }

    @Override
    public boolean deleteBooks(int[] booksId) {
        return bookDao.deleteBooks(booksId);
    }

    private void calculateBookStatus(Book book) {
        if (book.getInStock() > 0) {
            book.setAvailableStatus();
        } else {
            bookDao.getEarliestDueDate(book.getId()).ifPresent(book::setUnavailableStatus);
        }
    }

    @Override
    public boolean createBook(BookDto bookDto, Part part, String savePath) {
        Book book = Book.buildFrom(bookDto);
        String nameForDb = defineFileName(part, book);
        boolean createdResult = bookDao.createBook(book);
        if (part!=null&&part.getSize() != 0 && createdResult) {
            if (part.getSize() != 0) {
                uploadFileService.uploadFile(savePath, part, nameForDb);
            }
        }
        return createdResult;
    }

    private String defineFileName(Part part, Book book) {
        String nameForDb = null;
        if (part!=null&&part.getSize() != 0) {
            nameForDb = book.getId() + "." + FilenameUtils.getExtension(part.getSubmittedFileName());
            book.setCoverPath(nameForDb);
        }
        return nameForDb;
    }

}
