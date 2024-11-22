package LR_3dz;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class ProcessSearchRequestBehaviour extends Behaviour {
    private NodeAgent nodeAgent;
    private boolean isDone = false;
    private Function<List<String>, String> formatPath;

    public ProcessSearchRequestBehaviour(NodeAgent nodeAgent, Function<List<String>, String> formatPath) {
        this.nodeAgent = nodeAgent;
        this.formatPath = formatPath;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            String senderId = msg.getSender().getLocalName();
            String[] contentParts = msg.getContent().split(",");
            String targetId = contentParts[0];
            int currentWeight = Integer.parseInt(contentParts[1]);

            String messageId = UUID.randomUUID().toString();

            if (nodeAgent.getProcessedMessages().contains(messageId)) {
                return;
            }

            nodeAgent.getProcessedMessages().add(messageId);

            List<String> path = new ArrayList<>();
            path.add(nodeAgent.getId());

            if (nodeAgent.isTargetAgent()) {
                System.out.println("Целевой агент найден агентом " + nodeAgent.getId() + ". Путь: " + formatPath.apply(path));
                nodeAgent.sendBackMessageToInitiator(path, currentWeight);
            } else {
                myAgent.addBehaviour(new WakerBehaviour(myAgent, 1000) {
                    @Override
                    protected void onWake() {
                        for (Neighbor neighbor : nodeAgent.getNeighbors()) {
                            if (!path.contains(neighbor.getId()) && !neighbor.getId().equals(senderId)) {
                                System.out.println("Агент " + nodeAgent.getId() + " пересылает запрос поиска соседу " + neighbor.getId());
                                ACLMessage forwardMsg = new ACLMessage(ACLMessage.INFORM);
                                forwardMsg.addReceiver(new jade.core.AID(neighbor.getId(), jade.core.AID.ISLOCALNAME));
                                forwardMsg.setContent(targetId + "," + (currentWeight + neighbor.getWeight()));
                                myAgent.send(forwardMsg);
                            }
                        }
                    }
                });
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}