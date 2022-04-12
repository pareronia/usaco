package com.github.pareronia.usaco._691;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Problem 2. Hoof, Paper, Scissors
 * @see <a href="http://www.usaco.org/index.php?page=viewproblem2&cpid=691">http://www.usaco.org/index.php?page=viewproblem2&cpid=691</a>
 */
public class HoofPaperScissors {

    private final InputStream in;
    private final PrintStream out;
    
    public HoofPaperScissors(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int[] h = new int[n + 1];
        final int[] p = new int[n + 1];
        final int[] s = new int[n + 1];
        for (int j = 0; j < n; j++) {
            final String g = sc.next();
            h[j + 1] = h[j] + ("S".equals(g) ? 1 : 0);
            p[j + 1] = p[j] + ("H".equals(g) ? 1 : 0);
            s[j + 1] = s[j] + ("P".equals(g) ? 1 : 0);
        }
        int ans = 0;
        for (int j = 1; j <= n; j++) {
            ans = Math.max(ans, h[j] + p[n] - p[j]);
            ans = Math.max(ans, h[j] + s[n] - s[j]);
            ans = Math.max(ans, p[j] + h[n] - h[j]);
            ans = Math.max(ans, p[j] + s[n] - s[j]);
            ans = Math.max(ans, s[j] + h[n] - h[j]);
            ans = Math.max(ans, s[j] + p[n] - p[j]);
        }
        this.out.println(ans);
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases = isSample() ? sc.nextInt() : 1;
            for (int i = 0; i < numberOfTestCases; i++) {
                handleTestCase(i, sc);
            }
        }
    }

    public static void main(final String[] args) throws IOException, URISyntaxException {
        final boolean sample = isSample();
        final InputStream is;
        final PrintStream out;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long timerStart = 0;
        if (sample) {
            is = HoofPaperScissors.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = HoofPaperScissors.class.getResourceAsStream("hps.in");
            out = new PrintStream(new FileOutputStream("hps.out"), true);
        }
        
        new HoofPaperScissors(sample, is, out).solve();
        
        out.flush();
        if (sample) {
            final long timeSpent = (System.nanoTime() - timerStart) / 1_000;
            final double time;
            final String unit;
            if (timeSpent < 1_000) {
                time = timeSpent;
                unit = "µs";
            } else if (timeSpent < 1_000_000) {
                time = timeSpent / 1_000.0;
                unit = "ms";
            } else {
                time = timeSpent / 1_000_000.0;
                unit = "s";
            }
            final Path path
                    = Paths.get(HoofPaperScissors.class.getResource("sample.out").toURI());
            final List<String> expected = Files.readAllLines(path);
            final List<String> actual = asList(baos.toString().split("\\r?\\n"));
            if (!expected.equals(actual)) {
                throw new AssertionError(String.format(
                        "Expected %s, got %s", expected, actual));
            }
            actual.forEach(System.out::println);
            System.out.println(String.format("took: %.3f %s", time, unit));
        }
    }
    
    private static boolean isSample() {
        try {
            return "sample".equals(System.getProperty("usaco"));
        } catch (final SecurityException e) {
            return false;
        }
    }
    
    private static final class FastScanner implements Closeable {
        private final BufferedReader br;
        private StringTokenizer st;
        
        public FastScanner(final InputStream in) {
            this.br = new BufferedReader(new InputStreamReader(in));
            st = new StringTokenizer("");
        }
        
        public String next() {
            while (!st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }
    
        public int nextInt() {
            return Integer.parseInt(next());
        }
        
        @Override
        public void close() {
            try {
                this.br.close();
            } catch (final IOException e) {
                // ignore
            }
        }
    }
}
