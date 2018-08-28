package login.action;

import admin.servlet.AdminServlet;
import admin.vo.Admin;
import login.servlet.LoginServlet;
import user.servlet.UserServlet;
import user.vo.User;
import user.vo.UserOrderGoods;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class LoginAction extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String userPass = request.getParameter("userPass");

        LoginServlet servlet = new LoginServlet();
        User user = servlet.loginUser(userName, userPass);
        request.getSession().setAttribute("user", user);

        AdminServlet servlet1 = new AdminServlet();
        Admin admin = servlet1.loginAdmin(userName, userPass);
        request.getSession().setAttribute("admin", admin);


        if (user == null) {
            if (admin == null) {
                request.setAttribute("errorInfo", "用户名或者密码错误");
                request.getRequestDispatcher("/WEB-INF/login/index.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/AdminInitAction").forward(request, response);
            }
        } else {
            UserServlet userServlet = new UserServlet();
            List<UserOrderGoods> orderList = userServlet.queryAllOrders(userName);
            List<UserOrderGoods> orderAllList = userServlet.queryOrders(userName);

            request.getSession().setAttribute("orderList", orderList);
            request.getSession().setAttribute("orderAllList", orderAllList);

            // 跳转到登录界面前的url
            String reUrl = String.valueOf(request.getSession().getAttribute("reUrl"));

            reUrl = reUrl.split("//")[1];

            if (reUrl.split("/").length==2){
                reUrl = reUrl.split("/")[1];
            } else {
                reUrl = "";
            }

            request.getRequestDispatcher(reUrl).forward(request, response);
        }


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
