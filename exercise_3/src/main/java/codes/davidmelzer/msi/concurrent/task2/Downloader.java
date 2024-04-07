package codes.davidmelzer.msi.concurrent.task2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Downloader {
    private final InputStream in;
    private final OutputStream out;
    private final List<ProgressListener> listeners;

    public Downloader(URL url, String outputFilename) throws IOException {
        in = url.openConnection().getInputStream();
        out = new FileOutputStream(outputFilename);
        listeners = new ArrayList<>();
    }

    public synchronized void addListener(ProgressListener listener) {
        listeners.add(listener);
    }

    private synchronized void updateProgress(int total) {
        for (ProgressListener listener : listeners)
            listener.onProgress(total);
    }

    public void run() throws IOException {
        int n = 0, total = n;
        byte[] buffer = new byte[1024];
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
            total += n;
            updateProgress(total);
        }
        out.flush();
    }

    public interface ProgressListener {
        void onProgress(int current);
    }

    // Deadlock scenario
    public static void main(String[] args) throws Exception {
        final Object lock = new Object();

        ProgressListener listener = current -> {
            synchronized (lock) {
                System.out.println("Progress: " + current);
                // Simulate doing some work that also requires locking on the main thread's lock object
                // This could potentially create a deadlock if the main thread is waiting for this listener to complete its work
                // while holding onto the lock.
            }
        };

        // Example URL and output file
        URL url = new URL("https://example-files.online-convert.com/document/txt/example.txt");
        Downloader downloader = new Downloader(url, "output.txt");
        downloader.addListener(listener);

        Thread downloadThread = new Thread(() -> {
            try {
                downloader.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        synchronized (lock) {
            downloadThread.start();
            // Wait for a condition to be true, which might never happen if a deadlock occurs
            // For example, waiting for the download to complete, but since the download thread is waiting for the lock
            // to call the progress listener, and the main thread holds the lock and waits for the download to complete,
            // we have a deadlock.
            while (!isDownloadComplete()) {
                lock.wait(); // This could cause the main thread to wait indefinitely, leading to a deadlock scenario.
            }
        }
    }

    private static boolean isDownloadComplete() {
        // This method should ideally check if the download is complete.
        // For the purpose of this example, it just returns false.
        return false;
    }
}
