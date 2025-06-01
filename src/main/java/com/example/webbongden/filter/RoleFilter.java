package com.example.webbongden.filter;

import com.example.webbongden.dao.model.SecurityUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String path = req.getRequestURI().substring(contextPath.length());

        if (path.startsWith("/admin")) {
            if (SecurityUtils.hasPermission(req, "access_admin_dashboard")) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(contextPath + "/access-denied.jsp");
            }
            return;
        }

        if (path.startsWith("/cart") || path.startsWith("/user-profile")) {
            if (SecurityUtils.hasPermission(req, "access_user_pages")) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(contextPath + "/login");
            }
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}