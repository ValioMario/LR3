package LR_3dz;

import jade.core.behaviours.OneShotBehaviour;

public class InitiateSearchBehaviour extends OneShotBehaviour {
    private NodeAgent nodeAgent;

    public InitiateSearchBehaviour(NodeAgent nodeAgent) {
        this.nodeAgent = nodeAgent;
    }

    @Override
    public void action() {
        if (nodeAgent.isInitiator() && !nodeAgent.isTargetAgent()) {
            nodeAgent.sendSearchMessage();
        }
    }
}