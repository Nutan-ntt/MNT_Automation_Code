package tests.ImportModule;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ImportModule.Import_DownloadExcelPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Import_DownloadExcelTest extends BaseTest {

    @Test
    public void downloadExpandedBookingResultsAsExcel() throws IOException {
        Path downloadDirectory = Paths.get(System.getProperty("user.home"), "Downloads");
        Assert.assertTrue(
                Files.isDirectory(downloadDirectory),
                "Downloads folder does not exist: " + downloadDirectory
        );
        Map<Path, FileState> filesBeforeDownload = snapshotFiles(downloadDirectory);

        new Import_DownloadExcelPage().downloadAsExcel();

        Path downloadedExcel = waitForExcelDownload(downloadDirectory, filesBeforeDownload);
        Assert.assertNotNull(downloadedExcel, "No new Excel file was downloaded.");
        Assert.assertTrue(
                Files.size(downloadedExcel) > 0,
                "Downloaded Excel file is empty: " + downloadedExcel
        );
    }

    private Map<Path, FileState> snapshotFiles(Path directory) throws IOException {
        Map<Path, FileState> snapshot = new HashMap<>();
        try (Stream<Path> files = Files.list(directory)) {
            for (Path path : files.filter(Files::isRegularFile).toList()) {
                snapshot.put(
                        path.toAbsolutePath(),
                        new FileState(Files.size(path), Files.getLastModifiedTime(path))
                );
            }
        }
        return snapshot;
    }

    private Path waitForExcelDownload(
            Path directory,
            Map<Path, FileState> filesBeforeDownload
    ) throws IOException {
        Instant deadline = Instant.now().plus(Duration.ofSeconds(60));
        while (Instant.now().isBefore(deadline)) {
            try (Stream<Path> files = Files.list(directory)) {
                Path excel = files
                        .filter(Files::isRegularFile)
                        .filter(path -> {
                            String name = path.getFileName().toString().toLowerCase();
                            return name.endsWith(".xlsx") || name.endsWith(".xls");
                        })
                        .filter(path -> isNewOrUpdated(path, filesBeforeDownload))
                        .findFirst()
                        .orElse(null);
                if (excel != null) {
                    return excel;
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting for the Excel download.", interrupted);
            }
        }
        return null;
    }

    private boolean isNewOrUpdated(Path path, Map<Path, FileState> filesBeforeDownload) {
        try {
            Path absolutePath = path.toAbsolutePath();
            FileState previous = filesBeforeDownload.get(absolutePath);
            return previous == null
                    || previous.size() != Files.size(path)
                    || !previous.modifiedTime().equals(Files.getLastModifiedTime(path));
        } catch (IOException e) {
            return false;
        }
    }

    private record FileState(long size, FileTime modifiedTime) {
    }
}
