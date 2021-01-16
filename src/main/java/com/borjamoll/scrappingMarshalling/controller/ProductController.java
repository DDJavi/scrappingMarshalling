package com.borjamoll.scrappingMarshalling.controller;


import com.borjamoll.scrappingMarshalling.data.Search;
import com.borjamoll.scrappingMarshalling.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;

@RestController
public class ProductController {

    @GetMapping("/")
    String testl() {
        return "hola";
    }

    @PostMapping("/")
    String searchProduct(@RequestBody Search key) throws JsonProcessingException, JAXBException {
        ProductService _productService = new ProductService();
        return _productService.run(key);
    }

}
