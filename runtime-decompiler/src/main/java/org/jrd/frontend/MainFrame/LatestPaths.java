package org.jrd.frontend.MainFrame;

public class LatestPaths {
    public String lastManualUplaod ; //origianlly lastLoad
    public String lastSaveSrc;
    public String lastSaveBin;
    public String filesToCompile;
    public String outputExternalFilesDir;
    public String outputBinaries;

    public LatestPaths() {
        lastManualUplaod = System.getProperty("user.home");
        lastSaveSrc = System.getProperty("user.home");
        lastSaveBin = System.getProperty("user.home");
        filesToCompile = System.getProperty("user.home");
        outputExternalFilesDir = System.getProperty("user.home");
        outputBinaries = System.getProperty("user.home");
    }
}
