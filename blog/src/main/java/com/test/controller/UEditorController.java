package com.test.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import com.baidu.ueditor.ActionEnter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ued")
public class UEditorController {
    //ueditor的服务函数
	@RequestMapping(value="/config")
    public void config(HttpServletRequest request, HttpServletResponse response) {
 
        response.setContentType("application/json");
        String rootPath = request.getSession()
                .getServletContext().getRealPath("/");
 
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
}
