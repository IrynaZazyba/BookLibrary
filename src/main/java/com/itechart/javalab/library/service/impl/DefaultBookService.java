package com.itechart.javalab.library.service.impl;

import com.itechart.javalab.library.dao.AlterBookDao;
import com.itechart.javalab.library.dao.ReceiveBookDao;
import com.itechart.javalab.library.dao.impl.SqlAlterBookDao;
import com.itechart.javalab.library.dao.impl.SqlReceiveBookDao;
import com.itechart.javalab.library.dto.BookDto;
import com.itechart.javalab.library.model.Book;
import com.itechart.javalab.library.model.BookFilter;
import com.itechart.javalab.library.model.Paginator;
import com.itechart.javalab.library.service.BookService;
import com.itechart.javalab.library.service.UploadFileService;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.Part;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class DefaultBookService implements BookService {

    private final AlterBookDao alterBookDao;
    private final ReceiveBookDao receiveBookDao;

    private final UploadFileService uploadFileService;
    private static volatile BookService instance;

    private DefaultBookService(AlterBookDao alterBookDao, ReceiveBookDao defaultBookDao, UploadFileService fileService) {
        this.alterBookDao = alterBookDao;
        this.receiveBookDao = defaultBookDao;
        this.uploadFileService = fileService;
    }

    public static BookService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    instance = new DefaultBookService(SqlAlterBookDao.getInstance(),
                            SqlReceiveBookDao.getInstance(),
                            DefaultUploadFileService.getInstance());
                }
            }
        }
        return instance;
    }

    @Override
    public Optional<List<Book>> getBooks(Paginator paginator, boolean isAvailableOnly) {
        return receiveBookDao.getBooks(paginator, isAvailableOnly);
    }

    @Override
    public Optional<Integer> getNumberBooksRecords(boolean isAvailableOnly) {
        return receiveBookDao.getNumberBooksRecords(isAvailableOnly);
    }

    @Override
    public Optional<List<Book>> findBooksByParameters(Paginator paginator, BookFilter bookFilter) {
        return receiveBookDao.findBooksByParameters(paginator, bookFilter);
    }

    @Override
    public Optional<Integer> getNumberFoundBooksRecords(BookFilter bookFilter) {
        return receiveBookDao.getNumberFoundBooksRecords(bookFilter);
    }

    @Override
    public Optional<Book> getBookById(int bookId) {
        Optional<Book> bookById = receiveBookDao.getBookById(bookId);
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
        String fileName = defineFileName(part, book);
        book.setCoverPath(fileName);
        Optional<Boolean> updateResult = alterBookDao.updateBookInfo(book);
        if (part.getSize() != 0 && updateResult.isPresent()) {
            uploadFileService.uploadFile(savePath, part, fileName);
        }
        return updateResult;
    }

    @Override
    public boolean deleteBooks(int[] booksId) {
        return alterBookDao.deleteBooks(booksId);
    }

    private void calculateBookStatus(Book book) {
        if (book.getInStock() > 0) {
            book.setAvailableStatus();
        } else {
            Optional<LocalDateTime> earliestDueDate = receiveBookDao.getEarliestDueDate(book.getId());
            if (earliestDueDate.isPresent()) {
                book.setUnavailableStatus(earliestDueDate.get());
            } else {
                book.setUnavailableStatus(LocalDateTime.now());
            }
        }
    }

    @Override
    public int createBook(BookDto bookDto, Part part, String savePath) {
        Book book = Book.buildFrom(bookDto);
        int id = alterBookDao.createBook(book);
        book.setId(id);
        if (part.getSize() != 0) {
            String cover = generateUniqueFileName(book.getId(), part.getSubmittedFileName());
            book.setCoverPath(cover);
            uploadFileService.uploadFile(savePath, part, cover);
            alterBookDao.updateBookCover(book);
        }
        return id;
    }

    private String defineFileName(Part part, Book book) {
        String fileName;
        if (part.getSize() != 0) {
            fileName = generateUniqueFileName(book.getId(), part.getSubmittedFileName());
        } else {
            fileName = receiveBookDao.getBookCover(book.getId());
        }
        return fileName;
    }

    private String generateUniqueFileName(int bookId, String realFileName) {
        return bookId + "." + FilenameUtils.getExtension(realFileName);
    }
}
