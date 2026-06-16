package com.mateussdev.chemosyntehsis.Systems.DSPSystem;

public record DSPThreshold(DSPType type, float threshold, Runnable onExceed) {

}
