package com.github.pareronia.usaco._1063;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Problem 2. Rectangular Pasture
 * @see <a href="https://usaco.org/index.php?page=viewproblem2&cpid=1063">https://usaco.org/index.php?page=viewproblem2&cpid=1063</a>
 */
public class RectangularPasture {

    private final InputStream in;
    private final PrintStream out;

    public RectangularPasture(final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }

    private void handleTestCase(final int i, final FastScanner sc) {
        final int n = sc.nextInt();
        if (n > 1000) {
        	this.out.println(0);
        	return;
        }
        final int[][] arr = new int[n][2];
        for (int j = 0; j < n; j++) {
            arr[j] = sc.nextIntArray(2);
        }
        final Map<Integer, Integer> mapX = new HashMap<>();
        Arrays.sort(arr, Comparator.comparingInt(a -> a[0]));
        for (int j = 0; j < n; j++) {
            mapX.put(arr[j][0], j);
        }
        Arrays.sort(arr, Comparator.comparingInt(a -> a[1]));
        final Map<Integer, Integer> mapY = new HashMap<>();
        for (int j = 0; j < n; j++) {
            mapY.put(arr[j][1], j);
        }
        for (int j = 0; j < n; j++) {
            arr[j][0] = mapX.get(arr[j][0]);
            arr[j][1] = mapY.get(arr[j][1]);
        }
        long ans = 0;
        for (int j = 0; j < n; j++) {
            final int xj = arr[j][0];
            final int yj = arr[j][1];
            for (int k = j + 1; k < n; k++) {
                final int xk = arr[k][0];
                final int x1 = Math.min(xj, xk);
                final int x2 = Math.max(xj, xk);
                final int yk = arr[k][1];
                final int y1 = Math.min(yj, yk);
                final int y2 = Math.max(yj, yk);
                long below = 1;
                long above = 1;
                for (int t = 0; t < n; t++) {
                    final int x = arr[t][0];
                    if (x1 <= x && x < x2) {
                        final int y = arr[t][1];
                        if (0 <= y && y < y1) {
                            below++;
                        }
                        if (y2 < y && y < n) {
                            above++;
                        }
                    }
                }
                ans += below * above;
            }
        }
        ans += n + 1;
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
            is = RectangularPasture.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }

        new RectangularPasture(sample, is, out).solve();

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
            final Path path = Paths.get(RectangularPasture.class.getResource("sample.out").toURI());
            final List<String> expected = Files.readAllLines(path);
            final List<String> actual = asList(baos.toString().split("\\r?\\n"));
            if (!expected.equals(actual)) {
                throw new AssertionError(String.format("Expected %s, got %s", expected, actual));
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

        public int[] nextIntArray(final int n) {
            final int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = nextInt();
            }
            return a;
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
