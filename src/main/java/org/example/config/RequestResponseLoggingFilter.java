package org.example.config;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Логируем входящий запрос
        logger.info("Incoming request: method = {}, URI = {}", httpRequest.getMethod(), httpRequest.getRequestURI());

        // Продолжаем выполнение цепочки фильтров
        chain.doFilter(request, response);

        // Логируем исходящий ответ
        logger.info("Outgoing response: status = {}", httpResponse.getStatus());
    }
}