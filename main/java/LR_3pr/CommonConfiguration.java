package LR_3pr;

import lombok.Data;


import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "cfg")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonConfiguration {
    @XmlElementWrapper(name = "students")
    @XmlElement(name = "student")
    private List<StudentCfg> students;
}