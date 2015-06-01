package com.test.application.controller;


import com.test.application.model.Greeting;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by TheDude on 12/6/14.
 * This is an example on how to serve RESTful Web-Services with Spring MVC.
 * A key difference between a traditional MVC controller and the RESTful web service controller above is the way that
 * the HTTP response body is created. Rather than relying on a view technology to perform server-side rendering of the
 * greeting data to HTML, this RESTful web service controller simply populates and returns a Greeting object.
 * The object data will be written directly to the HTTP response as JSON.
 *
 */
@RestController
public class GreetingRestController
{
    private static final Logger LOG = Logger.getLogger(GreetingRestController.class);

    private static final String TEMPLATE = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping(value = "greetingRest", method = RequestMethod.GET)
    public Greeting greetingRest(@RequestParam(value="name", defaultValue = "World") String name)
    {
        LOG.info("handling greetingRest API");

        // spring using jackson to create JSON from an object.
        Greeting greeting =
                new Greeting(
                        counter.incrementAndGet(),
                        String.format(TEMPLATE, name)
                );

        return greeting;
    }
}

