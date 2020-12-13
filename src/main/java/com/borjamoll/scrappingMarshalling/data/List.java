package com.borjamoll.scrappingMarshalling.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@XmlRootElement(name="list")
public class List {
    private String listName;
    private ArrayList<Product> product = new ArrayList<>();

    @XmlElementWrapper(name = "products")
    public ArrayList<Product> getProduct() {
        return product;
    }
}
