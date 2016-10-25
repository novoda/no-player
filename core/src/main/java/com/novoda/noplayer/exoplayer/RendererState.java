package com.novoda.noplayer.exoplayer;

class RendererState {

    private static final int RENDERER_BUILDING_STATE_IDLE = 1;
    private static final int RENDERER_BUILDING_STATE_BUILDING = 2;
    private static final int RENDERER_BUILDING_STATE_BUILT = 3;

    private int rendererBuildingState;

    public RendererState() {
        setIdle();
    }

    public void setIdle() {
        rendererBuildingState = RENDERER_BUILDING_STATE_IDLE;
    }

    public void setBuilding() {
        rendererBuildingState = RENDERER_BUILDING_STATE_BUILDING;
    }

    public void setBuilt() {
        rendererBuildingState = RENDERER_BUILDING_STATE_BUILT;
    }

    public boolean isBuilt() {
        return rendererBuildingState == RENDERER_BUILDING_STATE_BUILT;
    }

    public boolean isBuilding() {
        return rendererBuildingState == RENDERER_BUILDING_STATE_BUILDING;
    }
}
