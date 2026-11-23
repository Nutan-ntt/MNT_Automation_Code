package tests.CovFiles;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CovFiles.CovFilesPage;

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

public class CovDownloadFileTest extends BaseTest {

    @Test
    public void testDownloadFile() throws IOException {
        Path downloadDirectory = Paths.get(
                System.getProperty("user.home"),
                "Downloads"
        );
        Assert.assertTrue(
                Files.isDirectory(downloadDirectory),
                "Downloads folder does not exist: " + downloadDirectory
        );

        Map<Path, FileState> filesBeforeDownload = snapshotFiles(downloadDirectory);

        CovFilesPage covFilesPage = new CovFilesPage();
        covFilesPage.openCovFileList();
        covFilesPage.searchFile("test");
        Assert.assertTrue(
                covFilesPage.getSearchResultsCount() > 0,
                "No files matched the search term 'test'."
        );
        covFilesPage.downloadFirstSearchResult();

        Path downloadedFile = waitForDownloadedFile(
                downloadDirectory,
                filesBeforeDownload
        );
        Assert.assertNotNull(downloadedFile, "The file download did not complete.");
        Assert.assertTrue(
                Files.size(downloadedFile) > 0,
                "Downloaded file is empty: " + downloadedFile
        );
    }

    private Map<Path, FileState> snapshotFiles(Path directory) throws IOException {
        Map<Path, FileState> snapshot = new HashMap<>();
        try (Stream<Path> files = Files.list(directory)) {
            for (Path path : files.toList()) {
                if (Files.isRegularFile(path)) {
                    snapshot.put(
                            path.toAbsolutePath(),
                            new FileState(Files.size(path), Files.getLastModifiedTime(path))
                    );
                }
            }
        }
        return snapshot;
    }

    private Path waitForDownloadedFile(
            Path directory,
            Map<Path, FileState> filesBeforeDownload
    ) throws IOException {
        Instant deadline = Instant.now().plus(Duration.ofSeconds(60));
        while (Instant.now().isBefore(deadline)) {
            try (Stream<Path> files = Files.list(directory)) {
                Path completedDownload = files
                        .filter(Files::isRegularFile)
                        .filter(path -> !path.getFileName().toString().endsWith(".crdownload"))
                        .filter(path -> isNewOrUpdated(path, filesBeforeDownload))
                        .findFirst()
                        .orElse(null);
                if (completedDownload != null) {
                    return completedDownload;
                }
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted while waiting for the download.", e);
            }
        }
        return null;
    }

    private boolean isNewOrUpdated(
            Path path,
            Map<Path, FileState> filesBeforeDownload
    ) {
        try {
            Path absolutePath = path.toAbsolutePath();
            FileTime modifiedTime = Files.getLastModifiedTime(path);
            FileState previousState = filesBeforeDownload.get(absolutePath);
            return previousState == null
                    || previousState.size() != Files.size(path)
                    || !previousState.modifiedTime().equals(modifiedTime);
        } catch (IOException e) {
            return false;
        }
    }

    private record FileState(long size, FileTime modifiedTime) {
    }
}
