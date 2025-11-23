package com.github.pareronia.usaco._669;

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
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Problem 1. Moocast
 *
 * @see <a
 *     href="https://usaco.org/index.php?page=viewproblem2&cpid=669">https://usaco.org/index.php?page=viewproblem2&cpid=669</a>
 */
public class MoocastPrim {

    private static final int SEEN = -1;

    private final InputStream in;
    private final PrintStream out;

    public MoocastPrim(final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }

    private void handleTestCase(final int i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int[][] a = new int[n][2];
        for (int j = 0; j < n; j++) {
            a[j] = new int[] {sc.nextInt(), sc.nextInt()};
        }
        final int[][] edges = new int[n][n];
        for (int j = 0; j < n; j++) {
            for (int k = j + 1; k < n; k++) {
                final int d =
                        (a[j][0] - a[k][0]) * (a[j][0] - a[k][0])
                                + (a[j][1] - a[k][1]) * (a[j][1] - a[k][1]);
                edges[j][k] = d;
                edges[k][j] = d;
            }
        }
        this.out.println(longestEdgeInMST(edges));
    }

    private long longestEdgeInMST(final int[][] edges) {
        final Queue<Long> q = new PriorityQueue<>();
        q.add(0L);
        int hi = 0;
        while (!q.isEmpty()) {
            final long n = q.poll();
            final int cow = (int) (n % 1000);
            if (edges[cow][cow] == SEEN) {
                continue;
            }
            edges[cow][cow] = SEEN;
            hi = (int) Math.max(hi, n / 1000);
            for (int c = 0; c < edges[cow].length; c++) {
                if (edges[c][c] != SEEN) {
                    q.add(1000L * edges[cow][c] + c);
                }
            }
        }
        return hi;
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
            is = MoocastPrim.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = MoocastPrim.class.getResourceAsStream("moocast.in");
            out = new PrintStream(new FileOutputStream("moocast.out"), true);
        }

        new MoocastPrim(sample, is, out).solve();

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
            final Path path = Paths.get(MoocastPrim.class.getResource("sample.out").toURI());
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
