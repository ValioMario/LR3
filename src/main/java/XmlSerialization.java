import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlSerialization {

    public static <T> T deserialize(Class<T> clas, String inPathFile){
        T object=null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clas);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object obj = jaxbUnmarshaller.unmarshal( new File(inPathFile));
            try {
                object = (T) obj;
            }
            catch (Exception e) {
                object = null;
                e.printStackTrace();
            }
        } catch (JAXBException ee ) {
            ee.printStackTrace();
        }
        return  object;
    }
}