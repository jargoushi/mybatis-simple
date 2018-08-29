package com.rwb.mapper;

import com.rwb.entity.Book;

import java.util.List;

public interface IBookMapper {

    Book selectByPrimaryKey(Integer bookId);

    List<Book> selectAll();
}
