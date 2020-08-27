package com.itechart.javalab.library.dao.factory;

import com.itechart.javalab.library.dao.BookDao;

public interface DAOFactory {

    BookDao getBookDAO();
}
