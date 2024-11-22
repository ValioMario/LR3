package LR_3dz;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@XmlRootElement(name = "graph")
@XmlAccessorType(XmlAccessType.FIELD)
public class Graph {
    @XmlElementWrapper(name = "nodes")
    @XmlElement(name = "node")
    private List<Node> nodes;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Node {
        @XmlAttribute
        private String id;

        @XmlAttribute
        private boolean isInitiator;

        @XmlAttribute
        private boolean isTargetAgent;

        @XmlElementWrapper(name = "neighbors")
        @XmlElement(name = "neighbor")
        private List<GraphNeighbor> neighbors;

        @Data
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class GraphNeighbor implements Neighbor {
            @XmlAttribute
            private String id;

            @XmlAttribute
            private int weight;

            @Override
            public String getId() {
                return id;
            }

            @Override
            public int getWeight() {
                return weight;
            }
        }

        public List<Neighbor> convertNeighbors() {
            return neighbors.stream()
                    .map(neighbor -> (Neighbor) neighbor)
                    .collect(Collectors.toList());
        }
    }
}