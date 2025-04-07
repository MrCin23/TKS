package pl.lodz.p.soap;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import pl.lodz.p.soap.model.user.SOAPUser;

public class JaxbDebugTest {
    public static void main(String[] args) {
        try {
            JAXBContext context = JAXBContext.newInstance(SOAPUser.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            SOAPUser user = new SOAPUser(); // Fill with dummy values if needed
            marshaller.marshal(user, System.out);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
