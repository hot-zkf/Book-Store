package com.zkf.web;

import com.google.gson.Gson;
import com.zkf.pojo.User;
import com.zkf.service.UserService;
import com.zkf.service.impl.UserServiceImpl;
import com.zkf.utils.WebUtils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

public class UserServlet extends BaseServlet {

    private UserService userService = new UserServiceImpl();

    protected void ajaxExistsUsername(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求的参数username
        String username = req.getParameter("username");
        //调用userService.existsUsername();
        boolean existsUsername = userService.existsUsername(username);
        //把返回的结果封装成为map对象
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("existsUsername",existsUsername);

        Gson gson = new Gson();
        String json = gson.toJson(resultMap);

        resp.getWriter().write(json);
    }



        /*
        * 注销*/
        protected void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //1.绑定Session中用户登录信息（或者销毁Session）
            req.getSession().invalidate();
            //2.重定向到首页（或登录页面）
            resp.sendRedirect(req.getContextPath());
        }

        /*
        * 登录*/
        protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            //1.获取请求参数
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            //调用userService.login()登录处理业务
            User loginUser = userService.login(new User(null, username, password, null));
            //如果等于null，说明登录失败
            if (loginUser == null) {
                //把错误信息和回显的表单项信息，保存到Request域中
                req.setAttribute("msg", "用户名或密码错误！");
                req.setAttribute("username", username);
                //跳回登录页面
                req.getRequestDispatcher("/pages/user/login.jsp").forward(req, resp);
            } else {
                //登录成功
                //保存用户登录的信息到Session域中
                req.getSession().setAttribute("user",loginUser);
                //跳到成功页面login_success.html
                req.getRequestDispatcher("/pages/user/login_success.jsp").forward(req, resp);

            }
        }



        protected void regist (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            //获取Session中的验证码
            String token = (String) req.getSession().getAttribute(KAPTCHA_SESSION_KEY);
            //删除Session中的验证码
            req.getSession().removeAttribute(KAPTCHA_SESSION_KEY);


            //1.获取请求的参数
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String email = req.getParameter("email");
            String code = req.getParameter("code");

            User user = WebUtils.copyParamToBean(req.getParameterMap(),new User());

            //2.检查验证码是否正确==写死：abcde
            if (token != null && token.equalsIgnoreCase(code)) {
                //正确
                //3.检查用户名是否可用
                if (userService.existsUsername(username)) {
                    //不可用
                    System.out.println("用户名[" + username + "]已存在！");
                    //把回显信息，保存到Request域中
                    req.setAttribute("msg", "用户名已存在！！");
                    req.setAttribute("username", username);
                    req.setAttribute("email", email);

                    //跳回注册页面
                    req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
                } else {
                    //可用
                    //调用service保存到数据库
                    userService.registUser(new User(null, username, password, email));
                    //跳回注册成功页面
                    req.getRequestDispatcher("/pages/user/regist_success.jsp").forward(req, resp);

                }
            } else {
                //把回显信息，保存到Request域中
                req.setAttribute("msg", "验证码错误！！");
                req.setAttribute("username", username);
                req.setAttribute("email", email);

                System.out.println("验证码[" + code + "]错误");
                req.getRequestDispatcher("/pages/user/regist.jsp").forward(req, resp);
            }

        }

        protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String action = req.getParameter("action");

            try {
                //获取action业务鉴别字符串，获取相应的业务方法反射对象
                Method method = this.getClass().getDeclaredMethod(action,HttpServletRequest.class,HttpServletResponse.class);
//                System.out.println(method);

                //调用目标业务方法
                method.invoke(this,req,resp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
}
