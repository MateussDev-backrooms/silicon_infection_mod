package net.Mateuss.Chemosynthesis.stage_system;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class InfectionProgress extends SavedData {
    private int points;

    public InfectionProgress() {
        this.points = 0;
    }

    public static enum InfectionStages {
        None,
        Interphase,
        Prophase,
        Metaphase,
        Anaphase,
        Telophase,
        Cytokinesis
    }

    //accessors


    public int getPoints() {
        return points;
    }

    public int pointsToStage(int p) {
        if(p < 100) return 1; //Interphase
        else if(p >= 100 && p < 500) return 2; //Prophase
        else if(p >= 500 && p < 1000) return 3; //Metaphase
        else if(p >= 1000 && p < 2500) return 4; //Anaphase
        else if(p >= 2500 && p < 5000) return 5; //Telophase
        else if(p > 5000) return 6; //Cytokinesis
        return 0;
    }

    public int stageToPoints(int stage) {
        return switch (stage) {
            case 2 -> 100;
            case 3 -> 500;
            case 4 -> 1000;
            case 5 -> 2500;
            case 6 -> 5000;
            default -> 0;
        };
    }
    public int stageToPoints(InfectionStages stage) {
        return switch (stage.ordinal()) {
            case 2 -> 100;
            case 3 -> 500;
            case 4 -> 1000;
            case 5 -> 2500;
            case 6 -> 5000;
            default -> 0;
        };
    }

    public int getStage() {
        return pointsToStage(points);
    }

    public String getStageString() {
        return InfectionStages.values()[pointsToStage(points)].toString();
    }

    public void setPoints(int new_points) {
        points = new_points;
        setDirty();
    }

    public void addPoints(int delta_points) {
        points += delta_points;
        setDirty();
    }

    public void addPointsIfInInterval(int delta_points, int from, int to) {
        if(points >= from && points <= to) {
            points += delta_points;
            setDirty();
        }
    }

    public void addPointsIfInStage(int delta_points, int stage) {
        if(pointsToStage(points) == stage) {
            points += delta_points;
            setDirty();
        }
    }



    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putInt("ChemosynthesisInfectionProgressPoints", points);
        return compoundTag;
    }

    public static InfectionProgress load(CompoundTag tag) {
        InfectionProgress data = new InfectionProgress();
        data.points = tag.getInt("ChemosynthesisInfectionProgressPoints");
        return data;
    }


}
