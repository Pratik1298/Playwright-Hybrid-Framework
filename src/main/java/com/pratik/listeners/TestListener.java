package com.pratik.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[START] " + name(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[PASS]  " + name(result) + " (" + duration(result) + " ms)");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("[FAIL]  " + name(result) + " -> "
                + result.getThrowable().getMessage().lines().findFirst().orElse(""));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[SKIP]  " + name(result));
    }

    private String name(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName()
                + "." + result.getMethod().getMethodName();
    }

    private long duration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }
}
