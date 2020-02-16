package com.example.demo.web.output;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "jplaceholder", url = "http://dummy.restapiexample.com")
public interface JSONPlaceHolderClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/employees")
    String getDummy();
}
