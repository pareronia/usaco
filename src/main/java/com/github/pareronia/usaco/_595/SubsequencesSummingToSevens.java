package com.github.pareronia.usaco._595;

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
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Problem 2. Subsequences Summing to Sevens
 * @see <a href="http://www.usaco.org/index.php?page=viewproblem2&cpid=595">http://www.usaco.org/index.php?page=viewproblem2&cpid=595</a>
 */
public class SubsequencesSummingToSevens {

    private final InputStream in;
    private final PrintStream out;
    
    public SubsequencesSummingToSevens(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final long[] p = new long[n + 1];
        final int[] l = new int[7];
        Arrays.fill(l, Integer.MAX_VALUE);
        final int[] r = new int[7];
        for (int j = 0; j < n; j++) {
            p[j + 1] = p[j] + sc.nextInt();
            final int mod = (int) (p[j + 1] % 7);
            l[mod] = Math.min(l[mod], j);
            r[mod] = j;
        }
        int ans = 0;
        for (int j = 0; j < 7; j++) {
            ans = Math.max(ans, r[j] - l[j]);
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
            is = SubsequencesSummingToSevens.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = SubsequencesSummingToSevens.class.getResourceAsStream("div7.in");
            out = new PrintStream(new FileOutputStream("div7.out"), true);
        }
        
        new SubsequencesSummingToSevens(sample, is, out).solve();
        
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
                    = Paths.get(SubsequencesSummingToSevens.class.getResource("sample.out").toURI());
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
