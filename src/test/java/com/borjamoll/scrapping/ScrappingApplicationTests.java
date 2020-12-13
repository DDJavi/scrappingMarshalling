package com.borjamoll.scrapping;

import com.borjamoll.scrappingMarshalling.data.List;
import com.borjamoll.scrappingMarshalling.data.Product;
import com.borjamoll.scrappingMarshalling.data.Search;
import com.borjamoll.scrappingMarshalling.services.ProductService;
import com.borjamoll.scrappingMarshalling.utils.JAXBxml;
import com.borjamoll.scrappingMarshalling.utils.ParserXML;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class ScrappingApplicationTests {



	@Test
	void contextLoads() {
	}
	@Test
	void stringCut(){
		String hola="2.000'02 €";
		String[] num = hola.split(" ");
		//float sub = Float.parseFloat(hola.substring(0,5).replace("'","."));
		System.out.println(Double.parseDouble(num[0].replace(".", "").replace("'",".")));

	}
	@Test
	void principal(){
		Document doc = null;
		Double price;
		String title = null;
		String stars = null;
		String pricehtml = "span#price_inside_buybox";
		String pricehtmlnew = "span#newBuyBoxPrice";
		try{
			doc = Jsoup.connect("https://www.amazon.es/s?k=tostadora&__mk_es_ES=%C3%85M%C3%85%C5%BD%C3%95%C3%91&ref=nb_sb_noss_2").get();
		}catch(Exception e){
			System.out.println("Page not found");
		}

		/**
		 * div.a-section esta todo
		 * span.a-size-base-plus esta el titulo
		 */
		Elements producto = doc.select("h2.a-size-mini");


		ArrayList<Product> products = new ArrayList<>();

		Document link = null;
		for(Element product : producto){

			String url = product.select("a.a-link-normal").attr("abs:href");
			try {
				link = Jsoup.connect(url).get();
			}catch(Exception e){
				System.out.println("pagina perdida");
			}
			try {
				//prize = findPrize(link.select("span#price_inside_buybox"),link.select(""));

				price = Double.parseDouble(link.select(pricehtml).text().substring(0,5));
				if(link.select(pricehtml).text().length() < link.select(pricehtmlnew).text().length()){
					price = Double.parseDouble(link.select(pricehtmlnew).text().substring(0,5));
				}
				if(price>1) {
					products.add(new Product(link.select("span#productTitle").text(), price, "stars"));
				}
			}catch(Exception e){
				System.out.println("Un producto perdido");
			}

		}
		for(Product prod : products){
			System.out.println("Product: " + prod.getProductName());
			System.out.println("Prize: " + prod.getPrice() + "€");
		}

	}
	@Test
	void xmlparser() throws TransformerException, ParserConfigurationException {
		ArrayList<Product> products = new ArrayList<>();
		products.add(new Product("hola", 0.54,"5 stars"));
		products.add(new Product("adios", 0.55, "0 stars"));;
		ParserXML parser = new ParserXML();
		parser.createXML(products, "tostadora");
	}
	@Test
	void xmlconverter() throws IOException, JAXBException {
		//Product products = new Product("hola", 0.54,"5 stars");
		ArrayList<Product> products = new ArrayList<>();
		products.add(new Product("hola", 0.54,"5 stars"));
		products.add(new Product("adios", 0.55, "0 stars"));
		List list = new List();
		list.setListName("Catalogo");
		list.setProduct(products);

		JAXBxml jaxb = new JAXBxml();
		jaxb.convertObjecttoXML(list,"hola.xml");
	}
	@Test
	void runtest() throws JAXBException, JsonProcessingException {
		ProductService _productService = new ProductService();
		Search search = new Search();
		search.setKey("azucar");
		search.setRead(true);
		search.setPrime(true);
		System.out.println(_productService.run(search));
	}

	@Test
	void fileExist(){
		File path = new File("tostadoraPrime.xml");
		if(!path.exists()){
			System.out.println("no existe");
		}else{
			System.out.println("existe");
		}
	}

	@Test
	void unmarshall() throws JAXBException, JsonProcessingException {
		JAXBxml jaxb = new JAXBxml();
		List list = jaxb.convertXMLtoObject("tostadoraPrime.xml");
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(objectMapper.writeValueAsString(list));

	}
}
