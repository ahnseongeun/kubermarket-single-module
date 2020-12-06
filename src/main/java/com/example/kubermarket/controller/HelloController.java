package com.example.kubermarket.controller;

import com.example.kubermarket.domain.User;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.GeneratedValue;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@Slf4j
public class HelloController {

    //http 통신은 request 와 response를 한번만 통신하고 통신을 끊어 버리기 때문에
    //response후에 request가 불가능하다.
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public void hello(HttpServletResponse httpServletResponse) throws IOException {
        log.debug("here is hello");
        httpServletResponse.sendRedirect("http://localhost:8080/ex_redirect1");
    }

    @GetMapping("/ex_redirect1")
    public void exRedirect1(HttpServletResponse res) throws IOException {
        try {
            log.debug("here is exe");
            PrintWriter out = res.getWriter();
            out.println("Hello, world2");
            out.close();
        } catch (IOException ex) {

        }
    }

}
