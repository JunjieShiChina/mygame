package com.mygdx.game.component.stages;

public interface Stage {
    void create();

    void render();

    void dispose();

    boolean isStagePass();

    void setStagePass(boolean stagePass);

    boolean isStageFailed();

    void setStageFailed(boolean stageFailed);

    boolean isStageOver();

    void setStageOver(boolean stageOver);

    void clear();
}
