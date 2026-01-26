package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte.HybridErythrocyte;
import net.minecraft.world.entity.LivingEntity;

public class ErythrocyteStateManager {
    private final HybridErythrocyte erythrocyte;
    private State currentState = State.IDLE;
    private LivingEntity transportTarget;
    private int pickupTimer = 0;

    public enum State {
        IDLE,
        SEEKING_ALLY,
        PICKING_UP,
        CARRYING,
        DEPLOYING,
        RETREATING
    }

    public ErythrocyteStateManager(HybridErythrocyte erythrocyte) {
        this.erythrocyte = erythrocyte;
    }

    public void tick() {
        switch (currentState) {
            case PICKING_UP:
                pickupTimer++;
                if (pickupTimer >= 20) { // 1 second pickup time
                    completePickup();
                }
                break;
            case DEPLOYING:
                pickupTimer++;
                if (pickupTimer >= 20) { // 1 second deploy time
                    completeDeploy();
                }
                break;
        }
    }

    public void setState(State state) {
        this.currentState = state;
        this.pickupTimer = 0;
    }

    public State getState() {
        return currentState;
    }

    public boolean isSeekingAlly() {
        return currentState == State.SEEKING_ALLY;
    }

    public boolean isCarrying() {
        return currentState == State.CARRYING;
    }

    public boolean isReadyForDeploy() {
        return currentState == State.CARRYING;
    }

    private void completePickup() {
        if (transportTarget != null) {
            transportTarget.startRiding(erythrocyte);
            erythrocyte.triggerAnim("transport_controller", "pickup_end");
            setState(State.CARRYING);
        }
    }

    private void completeDeploy() {
        if (transportTarget != null && transportTarget.isPassenger()) {
            transportTarget.stopRiding();
            erythrocyte.triggerAnim("transport_controller", "deploy_end");
            setState(State.RETREATING);
        }
    }

    public void setTransportTarget(LivingEntity target) {
        this.transportTarget = target;
    }

    public LivingEntity getTransportTarget() {
        return transportTarget;
    }
}
