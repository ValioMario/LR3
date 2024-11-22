package LR_3pr;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class StudentCfg {
    @XmlElement
    private String name;
    @XmlElement
    private String surname;
    @XmlElement
    private int age;
    @XmlElement
    private GroupCfg group;
}
