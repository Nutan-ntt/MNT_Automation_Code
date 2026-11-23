package tests.ImportModule;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ImportModule.Import_ExpandBookingPage;

public class Import_ExpandBookingTest extends BaseTest {

    @Test
    public void copyFirstBookingIdToParameters() {
        String bookingId = new Import_ExpandBookingPage().copyFirstBookingIdToParameters();
        Assert.assertFalse(bookingId.isBlank(), "No booking ID was read from the expanded row.");
    }
}
