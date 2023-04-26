package com.example.vndl.model;

import java.io.Serializable;

public class Test implements Serializable {
    private int id;
    private String testName;
    private TestResult testResult;

    public Test(int id, String testName, TestResult testResult) {
        this.id = id;
        this.testName = testName;
        this.testResult = testResult;
    }

    public int getId() {
        return id;
    }

    public String getTestName() {
        return testName;
    }

    public TestResult getTestResult() {
        return testResult;
    }

    public void setTestResult(TestResult testResult) {
        this.testResult = testResult;
    }
}
