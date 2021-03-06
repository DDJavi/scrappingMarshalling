package com.borjamoll.scrappingMarshalling.utils;

import com.borjamoll.scrappingMarshalling.data.Catalogue;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;


public class JAXBxml {
    public void convertObjecttoXML(Object object, String path) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Catalogue.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
        m.marshal(object, System.out);
        System.out.println("converted");
        m.marshal(object, new File(path));
        System.out.println("created");
    }

    public Catalogue convertXMLtoObject(String path) throws JAXBException {
        File file = new File(path);
        if(!file.exists()) {
            System.out.println("No existe");
            return null;
        }else{
            JAXBContext context = JAXBContext.newInstance(Catalogue.class);
            Unmarshaller um = context.createUnmarshaller();
            Catalogue catalogue = (Catalogue) um.unmarshal(new File(path));
            return catalogue;
        }
    }

}
