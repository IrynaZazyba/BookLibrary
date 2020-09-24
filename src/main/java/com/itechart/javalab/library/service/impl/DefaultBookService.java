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
        String nameForDb = defineFileName(part, book);
        Optional<Boolean> updateResult = alterBookDao.updateBookInfo(book);
        if (part != null && updateResult.isPresent()) {
            if (part.getSize() != 0) {
                uploadFileService.uploadFile(savePath, part, nameForDb);
            }
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
            receiveBookDao.getEarliestDueDate(book.getId()).ifPresent(book::setUnavailableStatus);
        }
    }

    @Override
    public int createBook(BookDto bookDto, Part part, String savePath) {
        Book book = Book.buildFrom(bookDto);
        int id = alterBookDao.createBook(book);
        book.setId(id);
        String cover = defineFileName(part, book);
        book.setCoverPath(cover);
        if (part != null && part.getSize() != 0) {
            alterBookDao.updateBookCover(book);
            uploadFileService.uploadFile(savePath, part, cover);
        }
        return id;
    }

    private String defineFileName(Part part, Book book) {
        String nameForDb = null;
        if (part != null && part.getSize() != 0) {
            nameForDb = book.getId() + "." + FilenameUtils.getExtension(part.getSubmittedFileName());
            book.setCoverPath(nameForDb);
        }
        return nameForDb;
    }

}
