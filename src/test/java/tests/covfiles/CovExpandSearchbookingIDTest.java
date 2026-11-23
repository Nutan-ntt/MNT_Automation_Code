package tests.CovFiles;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CovFiles.COV_ExpandBookingPage;

public class CovExpandSearchbookingIDTest extends BaseTest {

    @Test
    public void searchUsingFirstExpandedBookingId() {
        String bookingId = new COV_ExpandBookingPage().expandCopyAndSearchBookingId();
        Assert.assertFalse(bookingId.isBlank(), "The first expanded booking ID is empty.");
    }
}
