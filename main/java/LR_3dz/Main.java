package LR_3dz;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        // Создаем среду выполнения JADE
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        ContainerController cc = rt.createMainContainer(p);

        // Загружаем граф из XML-файла
        String filePath = "src/main/resources/nodes.xml";
        Optional<Graph> graphCfg = XmlSerialization.deserialize(filePath, Graph.class);
        if (graphCfg.isPresent()) {
            Graph graph = graphCfg.get();
            System.out.println("Граф успешно десериализован.");

            // Создаем агентов и передаем им параметры
            List<AgentController> agents = new ArrayList<>();
            AgentController initiatorAgent = null;

            for (Graph.Node node : graph.getNodes()) {
                String agentId = node.getId();
                boolean isInitiator = node.isInitiator();
                boolean isTargetAgent = node.isTargetAgent();
                List<Neighbor> neighbors = node.convertNeighbors();

                try {
                    AgentController ac = cc.createNewAgent(agentId, "LR_3dz.NodeAgent", new Object[]{agentId, isInitiator, isTargetAgent, neighbors});
                    agents.add(ac);

                    if (isInitiator) {
                        initiatorAgent = ac;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Запускаем всех агентов
            for (AgentController ac : agents) {
                try {
                    ac.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Инициируем поиск целевого агента
            if (initiatorAgent != null) {
                try {
                    initiatorAgent.putO2AObject(new InitiateSearchBehaviour(null), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Не удалось загрузить конфигурацию графа.");
        }
    }
}