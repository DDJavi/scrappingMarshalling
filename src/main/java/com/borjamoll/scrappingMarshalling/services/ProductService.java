package com.borjamoll.scrappingMarshalling.services;

import com.borjamoll.scrappingMarshalling.data.Catalogue;
import com.borjamoll.scrappingMarshalling.data.Product;
import com.borjamoll.scrappingMarshalling.data.Search;
import com.borjamoll.scrappingMarshalling.utils.JAXBxml;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


import javax.xml.bind.JAXBException;
import java.util.ArrayList;

/**
 * private final String ->atajos de CSS para .select
 */

public class ProductService {

    private final String AMAZON_URL = "https://www.amazon.es/s?k=";
    private final String normalSearch = "&dc&__mk_es_ES=ÅMÅŽÕÑ&qid=1606578057&rnid=831276031&ref=sr_nr_p_85_1";
    private final String primeSearch = "&rh=p_85%3A831314031&dc&__mk_es_ES=ÅMÅŽÕÑ&qid=1606578115&rnid=831276031&ref=sr_nr_p_85_1";
    private final String priceCSS = "span#price_inside_buybox";
    private final String priceNewCSS = "span#newBuyBoxPrice";
    private final String urlSectionCSS = "a.a-link-normal";
    private final String productTitle = "span#productTitle";
    private final String href = "abs:href";
    private final String starsCSS = "span#acrPopover";
    private final String sectionCSS = "h2.a-size-mini";
    private final String primeW = "Prime";
    private final String xmlW = ".xml";

    JAXBxml _jaxb = new JAXBxml();
    Catalogue catalogue = new Catalogue();
    private Document doc;
    private ArrayList<Product> products = new ArrayList<>();

    private Elements sections = null;
    private Document link = null;
    private String urlSection;
    private String stars;
    private Double price;


    /**
     *
     * @param key todos los valores de busqueda
     * @return jsonToString con objectmapper
     * @throws JsonProcessingException
     * @throws JAXBException
     * convertXMLtoObject recibe el path formado por la busqueda y devuelve un objeto que se mapea y termina el servicio
     * convertObjectToXML crea el fichero xml y no devuelve nada
     * productList() rellena la arraylist de productos
     */
    public String run(Search key) throws JsonProcessingException, JAXBException {

        if(key.isRead()) {
            if (key.isPrime()) {
                catalogue = _jaxb.convertXMLtoObject(key.getKey()+primeW+xmlW);
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(catalogue);
            }else{
                catalogue = _jaxb.convertXMLtoObject(key.getKey()+xmlW);
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(catalogue);
            }
        }

        if(key.getKey().equals("") || key.getKey()==null){
            return "Bad request";
        }
        try {
            if(key.isPrime()){
                doc = Jsoup.connect(AMAZON_URL + key.getKey().replace(" ", "+") + primeSearch).get();
            }else{
                doc = Jsoup.connect(AMAZON_URL + key.getKey() + normalSearch).get();
            }

        } catch (Exception e) {
            return "Search not found";
        }
        sections = doc.select(sectionCSS);
        if(key.getTotal()>1) products = productList(sections, key.getTotal());
        else products = productList(sections,6);

        catalogue.setListName(key.getKey() + "Catalogue");
        catalogue.setProduct(products);
        if(key.isSave()) {
            if(key.isPrime()) {
                _jaxb.convertObjecttoXML(catalogue, key.getKey() + "Prime.xml");
            }else{
                _jaxb.convertObjecttoXML(catalogue, key.getKey() + ".xml");
            }
        }
        /**
        ParserXML parser = new ParserXML();
        try {
            parser.createXML(products, key.getKey());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        **/

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(catalogue);
    }

    public ArrayList<Product> productList(Elements list, int total){
        int i=0;
        for (Element section : list) {
            if(i==total) return products;
            i++;
            urlSection = section.select(urlSectionCSS).attr(href);
            System.out.println(urlSection);
            try {
                link = Jsoup.connect(urlSection).get();
                stars = link.select(starsCSS).attr("title");
                if(stars.length()<1){
                    stars="No stars";
                }
                price = Double.parseDouble(link.select(priceCSS).text().split(" ")[0].replace(".","").replace(",","."));
                if (link.select(priceCSS).text().length() < link.select(priceNewCSS).text().length()) {
                    price = Double.parseDouble(link.select(priceNewCSS).text().split(" ")[0].replace(".","").replace(",","."));
                }
                if (price > 1) {
                    products.add(new Product(link.select(productTitle).text(), price, stars));
                }
            } catch (Exception e) {
                System.out.println("Lost product");
                i--;
            }
        }

        return products;
    }
}
