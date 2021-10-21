package com.zkf.test;

import com.zkf.pojo.Book;
import com.zkf.pojo.Page;
import com.zkf.service.BookService;
import com.zkf.service.impl.BookServiceImpl;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BookServiceTest {
    private BookService bookService = new BookServiceImpl();

    @Test
    public void addBook() {
        bookService.addBook(new Book(null,"宝剑在手，天下我有！","大麻屁",new BigDecimal(101),10,0,null));
    }

    @Test
    public void deleteBookById() {
        bookService.deleteBookById(22);
    }

    @Test
    public void updateBook() {
        bookService.updateBook(new Book(22,"狠人！","大麻屁",new BigDecimal(101),10,0,null));
    }

    @Test
    public void queryBookById() {
        System.out.println(bookService.queryBookById(22));
    }

    @Test
    public void queryBooks() {
        for (Book queryBook : bookService.queryBooks()){
            System.out.println(queryBook);
        }
    }

    @Test
    public void page(){

        System.out.println(bookService.page(1, Page.PAGE_SIZE));

    }

    @Test
    public void pageByPrice(){

        System.out.println(bookService.pageByPrice(1, Page.PAGE_SIZE,10,50));

    }
}
