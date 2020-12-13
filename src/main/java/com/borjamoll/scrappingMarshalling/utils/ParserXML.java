package com.borjamoll.scrappingMarshalling.utils;

import com.borjamoll.scrappingMarshalling.data.Product;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class ParserXML {


    public void createXML(ArrayList<Product> products, String key) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.newDocument();

        Element productos = documento.createElement("Products");

        for(Product product : products){
            Element producto = documento.createElement("Product");
            Element title =  documento.createElement("Product");
            Element price = documento.createElement("Price");
            Element stars = documento.createElement("Stars");

            Text textTitle = documento.createTextNode(product.getProductName());
            Text textPrice = documento.createTextNode(String.valueOf(product.getPrice()));
            Text textStars = documento.createTextNode(product.getStars());
            title.appendChild(textTitle);
            price.appendChild(textPrice);
            stars.appendChild(textStars);
            producto.appendChild(title);
            producto.appendChild(price);
            producto.appendChild(stars);
            productos.appendChild(producto);
        }
        documento.appendChild(productos);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(documento);
        StreamResult file = new StreamResult(new File(key +" catalogo.xml"));
        transformer.transform(source,file);
        System.out.println("fichero cargado");
    }
}
