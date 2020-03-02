package com.volvo.project.components.openshift;

import com.volvo.project.components.TestBase;

import java.io.File;
import java.io.IOException;

public class ConnectionToOpenshift extends TestBase {

  private String batFileName;
  private String batFilePath;
  private Process openshiftProcess;
  private final String taskkillOpenshiftCommand = "taskkill /F /IM oc.exe";

  public ConnectionToOpenshift(String batFileName, String batFilePath){
    this.batFileName = batFileName;
    String pathDir = new File("").getAbsolutePath();
    this.batFilePath = pathDir+batFilePath;
  }

  public void connect(){
    try {
      ProcessBuilder pb = new ProcessBuilder("cmd", "/c", batFileName);
      File dir = new File(batFilePath);
      pb.directory(dir);
      openshiftProcess = pb.start();
      Thread.sleep(4000);                                   // time needed to connect with Openshift
      logger.info("Successfully connected with Openshift");
    } catch (IOException | InterruptedException e) {
      logger.info("Unable to connect with Openshift");
    }
  }

  public void disconnect(){
    try {
      Runtime.getRuntime().exec(taskkillOpenshiftCommand);
      logger.info(("Successfully disconnected to Openshift"));
    } catch (IOException e) {
      logger.info("Unable to disconnect with Openshift");
    }
    if(openshiftProcess != null) {
      openshiftProcess.destroy();
    }
  }
}
