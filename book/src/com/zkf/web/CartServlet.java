package com.zkf.web;

import com.google.gson.Gson;
import com.zkf.pojo.Book;
import com.zkf.pojo.Cart;
import com.zkf.pojo.CartItem;
import com.zkf.service.BookService;
import com.zkf.service.impl.BookServiceImpl;
import com.zkf.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CartServlet extends BaseServlet {
    private BookService bookService = new BookServiceImpl();

    /*
     * 修改商品数量
     * */
    protected void updateCount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求的参数 商品编号，商品数量
        int id = WebUtils.parseInt(req.getParameter("id"),0);
        int count = WebUtils.parseInt(req.getParameter("count"),1);
        //获取Cart购物车对象
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart != null){
            //修改商品数量
            cart.updateCount(id,count);
            //重定向购物车展示页面
            resp.sendRedirect(req.getHeader("Referer"));
        }
    }

    /*
     * 清空购物车
     * */
    protected void clear(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取购物车对象
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart != null){
            //清空购物车商品项
            cart.clear();
            //重定向购物车展示页面
            resp.sendRedirect(req.getHeader("Referer"));
        }
    }

    /*
    * 删除商品项
    * */
    protected void deleteItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取商品编号
        int id = WebUtils.parseInt(req.getParameter("id"),0);
        //获取购物车对象
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if (cart != null){
            //删除了购物车商品项
            cart.deleteItem(id);
            //重定向购物车展示页面
            resp.sendRedirect(req.getHeader("Referer"));

        }
    }

    /*
     * 加入购物车
     * */
    protected void addItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取请求的参数 商品编号
        int id = WebUtils.parseInt(req.getParameter("id"),0);
        //调用bookService.queryBookId(id):Book得到图书的信息
        Book book = bookService.queryBookById(id);
        //把图书信息，转换为CartItem商品项
        CartItem cartItem = new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice());
        //调用Cart.addItem(CartItem);添加商品项
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart == null){
            cart = new Cart();
            req.getSession().setAttribute("cart",cart);
        }
        cart.addItem(cartItem);

        System.out.println(cart);
        System.out.println("请求头Referer的值：" + req.getHeader("Referer"));
        //最后一个添加的商品名称
        req.getSession().setAttribute("lastName",cartItem.getName());

        //重定向回原来商品所有的地址页面
        resp.sendRedirect(req.getHeader("Referer"));
    }



    protected void ajaxAddItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取请求的参数 商品编号
        int id = WebUtils.parseInt(req.getParameter("id"),0);
        //调用bookService.queryBookId(id):Book得到图书的信息
        Book book = bookService.queryBookById(id);
        //把图书信息，转换为CartItem商品项
        CartItem cartItem = new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice());
        //调用Cart.addItem(CartItem);添加商品项
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart == null){
            cart = new Cart();
            req.getSession().setAttribute("cart",cart);
        }
        cart.addItem(cartItem);

        System.out.println(cart);
        //最后一个添加的商品名称
        req.getSession().setAttribute("lastName",cartItem.getName());
        //6.返回购物车总的商品数量和最后一个添加的商品名称
        Map<String,Object> resultMap = new HashMap<String,Object>();

        resultMap.put("totalCount",cart.getTotalCount());
        resultMap.put("lastName",cartItem.getName());

        Gson gson = new Gson();
        String resultMapJsonString = gson.toJson(resultMap);

        resp.getWriter().write(resultMapJsonString);

    }



}
