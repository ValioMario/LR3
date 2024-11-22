package LR_3dz;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Optional;

public class XmlSerialization {

    // Метод для десериализации объекта из XML-файла
    public static <T> Optional<T> deserialize(String path, Class<T> clazz) {
        // Создаем объект File, представляющий путь к XML-файлу
        File f = new File(path);
        try {
            // Создаем экземпляр JAXBContext для класса, который будет десериализован
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            // Создаем объект Unmarshaller для десериализации
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            // Десериализуем объект из XML-файла
            T cfg = (T) unmarshaller.unmarshal(f);
            // Возвращаем Optional, содержащий десериализованный объект или пустой Optional, если объект равен null
            return Optional.ofNullable(cfg);
        } catch (Exception e) {
            // В случае ошибки выводим стек-трейс и возвращаем пустой Optional
            e.printStackTrace();
            return Optional.empty();
        }
    }
}