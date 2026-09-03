package listeners;

import org.testng.*;

public class TestResultListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n==================================================");
        System.out.println("      USER MANAGEMENT AUTOMATION EXECUTION");
        System.out.println("==================================================\n");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✓ " + result.getMethod().getDescription() + " : PASSED");
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("✗ " + result.getMethod().getDescription() + " : FAILED");
        System.out.println("--------------------------------------------------");
    }

    @Override
    public void onFinish(ITestContext context) {

        System.out.println("\n==================================================");
        System.out.println("TEST EXECUTION SUMMARY");
        System.out.println("==================================================");

        System.out.println("Total   : " + context.getAllTestMethods().length);
        System.out.println("Passed  : " + context.getPassedTests().size());
        System.out.println("Failed  : " + context.getFailedTests().size());
        System.out.println("Skipped : " + context.getSkippedTests().size());

        System.out.println("==================================================");
    }
}