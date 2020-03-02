package com.volvo.project.components.listeners;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.util.Iterator;

import static com.volvo.project.components.TestBase.logger;

public class TestngContentListener extends TestListenerAdapter {
  @Override
  public void onFinish(ITestContext context){
    deleteAllMethod(context.getPassedConfigurations().getAllResults().iterator());
    deleteAllMethod(context.getSkippedConfigurations().getAllResults().iterator());
    deleteAllMethod(context.getFailedConfigurations().getAllResults().iterator());
  }
  /*
  This is for Jira Xray integration
  This is deleting all configuration methods, which is beforeMethod, beforeTest, after etc.
  Due to the fact that we don't want to have configuration methods in our jira integration.
  */
  public void deleteAllMethod(Iterator<ITestResult> listTestMethods) {
    while (listTestMethods.hasNext()) {
      ITestResult testResult = listTestMethods.next();
      logger.debug("Deleting from result: {}" + testResult.getMethod().getMethodName());
      listTestMethods.remove();
    }
  }
}
