package com.example.webbongden.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebFilter("/*")
public class RoleFilter implements Filter {

    // Map chứa route prefix và các role được phép truy cập
    private final Map<String, List<String>> protectedRoutes = new HashMap<>();

    @Override
    public void init(FilterConfig config) throws ServletException {
        // Gán quyền cho từng route
        protectedRoutes.put("/cart", Arrays.asList("user", "admin"));
        protectedRoutes.put("/user", Arrays.asList("user", "admin"));
        protectedRoutes.put("/admin", Collections.singletonList("admin"));
        // home và product-detail là public, nên không cần map
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();
        String path = requestURI.replace(contextPath, "");

        HttpSession session = req.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        for (Map.Entry<String, List<String>> entry : protectedRoutes.entrySet()) {
            String prefix = entry.getKey();
            List<String> allowedRoles = entry.getValue();

            if (path.startsWith(prefix)) {
                if (role == null || !allowedRoles.contains(role)) {
                    // admin -> sai quyền thì cho về /page-not-found
                    if (prefix.startsWith("/admin")) {
                        res.sendRedirect(contextPath + "/page-not-found");
                    } else {
                        res.sendRedirect(contextPath + "/login");
                    }
                    return;
                }
            }
        }

        // Cho phép truy cập nếu không khớp route bảo vệ
        chain.doFilter(request, response);
    }
}
