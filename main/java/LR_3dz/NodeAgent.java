package LR_3dz;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.*;

public class NodeAgent extends Agent {
    private String id;
    private boolean isInitiator;
    private boolean isTargetAgent;
    private List<Neighbor> neighbors;
    private Set<String> processedMessages = new HashSet<>();
    private List<Map.Entry<List<String>, Integer>> searchResults = new ArrayList<>();

    @Override
    protected void setup() {
        // Инициализация агента
        Object[] args = getArguments();
        if (args != null && args.length == 4) {
            id = (String) args[0];
            isInitiator = (boolean) args[1];
            isTargetAgent = (boolean) args[2];
            neighbors = (List<Neighbor>) args[3];
        }

        // Добавление поведений
        addBehaviour(new InitiateSearchBehaviour(this));
        addBehaviour(new ProcessSearchRequestBehaviour(this, this::formatPath));
        addBehaviour(new ProcessBackMessageBehaviour(this, this::formatPath));
    }

    public void sendSearchMessage() {
        System.out.println("Агент " + id + " отправляет сообщения поиска соседям.");
        for (Neighbor neighbor : neighbors) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new jade.core.AID(neighbor.getId(), jade.core.AID.ISLOCALNAME));
            msg.setContent(isTargetAgent ? "true" : "false" + "," + neighbor.getWeight());
            send(msg);
        }
    }

    public void sendBackMessageToInitiator(List<String> path, int totalWeight) {
        System.out.println("Агент " + id + " отправляет обратное сообщение инициатору.");
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new jade.core.AID(path.get(0), jade.core.AID.ISLOCALNAME));
        msg.setContent(String.join(",", path) + "," + totalWeight);
        send(msg);
    }

    public void analyzeSearchResults() {
        if (searchResults.isEmpty()) {
            System.out.println("Поиск не удался. Путь к целевому агенту не найден.");
        } else {
            System.out.println("Найденные пути к целевому агенту:");
            for (Map.Entry<List<String>, Integer> entry : searchResults) {
                List<String> path = entry.getKey();
                int weight = entry.getValue();
                System.out.println("Путь: " + formatPath(path) + ", Вес: " + weight);
            }

            // Выводим общее количество путей
            System.out.println("Общее количество путей: " + searchResults.size());

            // Находим путь с наименьшим весом
            Map.Entry<List<String>, Integer> bestPathEntry = searchResults.stream()
                    .min(Comparator.comparingInt(Map.Entry::getValue))
                    .orElse(null);

            if (bestPathEntry != null) {
                List<String> bestPath = bestPathEntry.getKey();
                int bestWeight = bestPathEntry.getValue();
                System.out.println("Лучший путь: " + formatPath(bestPath) + ", Вес: " + bestWeight);
            } else {
                System.out.println("Не удалось найти лучший путь.");
            }
        }

        // Устанавливаем флаг завершения поиска
        doDelete();
    }

    private String formatPath(List<String> path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public boolean isInitiator() {
        return isInitiator;
    }

    public boolean isTargetAgent() {
        return isTargetAgent;
    }

    public List<Neighbor> getNeighbors() {
        return neighbors;
    }

    public Set<String> getProcessedMessages() {
        return processedMessages;
    }

    public List<Map.Entry<List<String>, Integer>> getSearchResults() {
        return searchResults;
    }
}