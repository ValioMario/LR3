package LR_3pr;

import java.io.File;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        File f = new File("src/main/resources/students.xml");
        Optional<CommonConfiguration> cfg = XmlSerialization.deserialize("src/main/resources/students.xml", CommonConfiguration.class);
        System.out.println(cfg.get());

        boolean serialize = XmlSerialization.serialize("src/main/resources/students2.xml", cfg.get());

    }
}
