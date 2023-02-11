package com.roche.forte.simulator;

import java.util.TimerTask;

public class ForteTask extends TimerTask {
    
    private String taskType;
    ForteAnalyzerPanel forteAnalyzerPanel = new ForteAnalyzerPanel();
    
    @Override public void run() {
        if (this.taskType.equals(UrlConstants.PING.getText())) {
            forteAnalyzerPanel.ping();
        } else if (this.taskType.equals(UrlConstants.GETJOB.getText())) {
            forteAnalyzerPanel.getJobQueue();
        }
    }
    
    /**
     * @return the taskType
     */
    public String getTaskType() {
        return taskType;
    }
    
    /**
     * @param taskType the taskType to set
     */
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    
}
