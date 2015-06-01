package com.test.application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by TheDude on 12/6/14.
 * This is an example for serving web content with Spring MVC.
 */

@Controller
public class GreetingController
{
    private static final String GREETING_TEMPLATE_VIEW = "greeting";


    @RequestMapping(value="/greeting", method= RequestMethod.GET)
    public String greeting(
            @RequestParam(value="name", required = false, defaultValue = "World")String name,
            Model model)
    {
        model.addAttribute("name", name);
        return GREETING_TEMPLATE_VIEW;
    }

}