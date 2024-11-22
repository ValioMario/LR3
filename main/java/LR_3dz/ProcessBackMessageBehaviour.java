package LR_3dz;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ProcessBackMessageBehaviour extends Behaviour {
    private NodeAgent nodeAgent;
    private boolean isDone = false;
    private Function<List<String>, String> formatPath;

    public ProcessBackMessageBehaviour(NodeAgent nodeAgent, Function<List<String>, String> formatPath) {
        this.nodeAgent = nodeAgent;
        this.formatPath = formatPath;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            String content = msg.getContent();
            String[] parts = content.split(",");
            List<String> path = new ArrayList<>(Arrays.asList(parts).subList(0, parts.length - 1));
            int totalWeight = Integer.parseInt(parts[parts.length - 1]);

            if (nodeAgent.isInitiator()) {
                System.out.println("Агент " + nodeAgent.getId() + " получает обратное сообщение. Путь: " + formatPath.apply(path) + ", Вес: " + totalWeight);
                nodeAgent.getSearchResults().add(new AbstractMap.SimpleEntry<>(path, totalWeight));
                nodeAgent.analyzeSearchResults();
            } else {
                path.add(nodeAgent.getId());
                totalWeight += getWeightToNeighbor(path.get(path.size() - 2));
                System.out.println("Агент " + nodeAgent.getId() + " пересылает обратное сообщение. Путь: " + formatPath.apply(path) + ", Вес: " + totalWeight);
                nodeAgent.sendBackMessageToInitiator(path, totalWeight);
            }
        } else {
            block();
        }
    }

    private int getWeightToNeighbor(String neighborId) {
        for (Neighbor neighbor : nodeAgent.getNeighbors()) {
            if (neighbor.getId().equals(neighborId)) {
                return neighbor.getWeight();
            }
        }
        return 0; // Если сосед не найден, возвращаем 0 (не должно произойти)
    }

    @Override
    public boolean done() {
        return isDone;
    }
}