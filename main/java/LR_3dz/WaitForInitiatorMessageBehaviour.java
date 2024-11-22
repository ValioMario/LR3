package LR_3dz;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WaitForInitiatorMessageBehaviour extends Behaviour {
    private NodeAgent nodeAgent;
    private boolean isDone = false;

    public WaitForInitiatorMessageBehaviour(NodeAgent nodeAgent) {
        this.nodeAgent = nodeAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null) {
            System.out.println("Агент " + nodeAgent.getId() + " получил сообщение от инициатора.");
            isDone = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}