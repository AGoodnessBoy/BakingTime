package com.moming.jml.bakingtime.data;

/**
 * Created by admin on 2018/1/23.
 */

public  class TestData {
    static StepEntry[] stepEntries;

    public static StepEntry[] getStepEntries() {

        stepEntries = new StepEntry[6];

        for (int i =0;i<6;i++){
            StepEntry stepEntry = new StepEntry();
            stepEntry.setSetp_shortDesc("step"+Integer.toString(i));
            stepEntry.setSetp_id(Integer.toString(i));
            stepEntries[i] =  stepEntry;
        }

        return stepEntries;
    }
}
