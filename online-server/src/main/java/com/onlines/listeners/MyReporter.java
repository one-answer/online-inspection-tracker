package com.onlines.listeners;

import com.onlines.mapper.PlanResultTestMapper;
import com.onlines.onlineSaleTest.SpringWrapper;
import com.onlines.pojo.PlanResultTest;
import com.onlines.utils.DateUtil;
import org.testng.IReporter;

import com.onlines.entiry.TestPlanDto;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.text.SimpleDateFormat;
import java.util.*;

public class MyReporter implements IReporter {
    private static PlanResultTestMapper testPlanResultMapper = SpringWrapper.getBean(PlanResultTestMapper.class);

    private int testsPass = 0;

    private int testsFail = 0;

    private int testsSkip = 0;

    private String beginTime;

    private long totalTime;

    private String name;

    public MyReporter() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        name = formatter.format(System.currentTimeMillis());
    }

    public MyReporter(String name) {
        this.name = name;
        if (this.name == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            this.name = formatter.format(System.currentTimeMillis());
        }
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        //通过遍历 xmlSuites 和 suites 能够获取所有测试方法的信息以及测试结果，后续可用于自定义测试报告。
        List<ITestResult> list = new ArrayList<ITestResult>();
        // 遍历List<ISuite> suites集合
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> suiteResults = suite.getResults();

            for (ISuiteResult suiteResult : suiteResults.values()) {
                ITestContext testContext = suiteResult.getTestContext();
                IResultMap passedTests = testContext.getPassedTests();
                testsPass = testsPass + passedTests.size();
                IResultMap failedTests = testContext.getFailedTests();
                testsFail = testsFail + failedTests.size();
                IResultMap skippedTests = testContext.getSkippedTests();
                testsSkip = testsSkip + skippedTests.size();
                IResultMap failedConfig = testContext.getFailedConfigurations();
                list.addAll(this.listTestResult(passedTests));
                list.addAll(this.listTestResult(failedTests));
                list.addAll(this.listTestResult(skippedTests));
                list.addAll(this.listTestResult(failedConfig));
            }
        }
        this.sort(list);
        this.outputResult(list);
    }

    private ArrayList<ITestResult> listTestResult(IResultMap resultMap) {
        Set<ITestResult> results = resultMap.getAllResults();
        return new ArrayList<ITestResult>(results);
    }

    private void sort(List<ITestResult> list) {
        Collections.sort(list, new Comparator<ITestResult>() {
            @Override
            public int compare(ITestResult r1, ITestResult r2) {
                if (r1.getStartMillis() > r2.getStartMillis()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    private void outputResult(List<ITestResult> list) {
        TestPlanDto res = new TestPlanDto();
        try {
            int index = 0;
            for (ITestResult result : list) {
                String tn = result.getTestContext().getCurrentXmlTest().getParameter("testCase");
                if (index == 0) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    beginTime = formatter.format(new Date(result.getStartMillis()));
                    index++;
                }
                long spendTime = result.getEndMillis() - result.getStartMillis();
                totalTime += spendTime;
                String status = this.getStatus(result.getStatus());
                List<String> log = Reporter.getOutput(result);
                for (int i = 0; i < log.size(); i++) {
                    log.set(i, this.toHtml(log.get(i)));
                }
                Throwable throwable = result.getThrowable();
                if (throwable != null) {
                    log.add(this.toHtml(throwable.toString()));
                    StackTraceElement[] st = throwable.getStackTrace();
                    for (StackTraceElement stackTraceElement : st) {
                        log.add(this.toHtml("    " + stackTraceElement));
                    }
                }
                if(status == "失败"){
                    System.out.println("case名称："+result.getName());
                    System.out.println("测试类："+result.getInstanceName());
                    System.out.println("耗时:"+spendTime + "ms");
                    System.out.println("用例描述："+result.getMethod().getDescription());
                    System.out.println("失败原因"+log);
                }
            }

            try {
                res.setDuration(totalTime);
                res.setPassedNum(testsPass);
                res.setFailedNum(testsFail);
                res.setSkippedNum(testsSkip);
                res.setTotalNum(testsPass + testsFail + testsSkip);
                saveTestPlan(testsPass,testsFail,testsSkip, beginTime, totalTime);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("测试结果："+ res);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTestPlan(int testsPass,int testsFail,int testsSkip,String beginTime,long totalTime){
        PlanResultTest testPlanResult=new PlanResultTest();
        testPlanResult.setTotalNum(testsPass + testsFail + testsSkip);
        testPlanResult.setPassedNum(testsPass);
        testPlanResult.setFailedNum(testsFail);
        testPlanResult.setSkipedNum(testsSkip);
        testPlanResult.setBeginTime(DateUtil.parseDate(beginTime,"yyyy-MM-dd HH:mm:ss.SSS"));
        testPlanResult.setDuration(totalTime);
        testPlanResult.setCreateTime(new java.sql.Date(new Date().getTime()));
        testPlanResultMapper.insert(testPlanResult);
    }
    private String getStatus(int status) {
        String statusString = null;
        switch (status) {
            case 1:
                statusString = "成功";
                break;
            case 2:
                statusString = "失败";
                break;
            case 3:
                statusString = "跳过";
                break;
            default:
                break;
        }
        return statusString;
    }

    private String toHtml(String str) {
        if (str == null) {
            return "";
        } else {
            str = str.replaceAll("<", "&lt;");
            str = str.replaceAll(">", "&gt;");
            str = str.replaceAll(" ", "&nbsp;");
            str = str.replaceAll("\n", "<br>");
            str = str.replaceAll("\"", "\\\\\"");
        }
        return str;
    }

}