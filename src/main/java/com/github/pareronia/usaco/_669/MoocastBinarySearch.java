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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Problem 1. Moocast
 *
 * @see <a
 *     href="https://usaco.org/index.php?page=viewproblem2&cpid=669">https://usaco.org/index.php?page=viewproblem2&cpid=669</a>
 */
public class MoocastBinarySearch {

    private final InputStream in;
    private final PrintStream out;

    public MoocastBinarySearch(final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }

    private void handleTestCase(final int i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int[][] arr = new int[n][2];
        for (int j = 0; j < n; j++) {
            arr[j] = new int[] {sc.nextInt(), sc.nextInt()};
        }
        final int[][] edges = new int[n][n];
        for (int j = 0; j < n; j++) {
            for (int k = j + 1; k < n; k++) {
                final int d =
                        (arr[j][0] - arr[k][0]) * (arr[j][0] - arr[k][0])
                                + (arr[j][1] - arr[k][1]) * (arr[j][1] - arr[k][1]);
                edges[j][k] = d;
                edges[k][j] = d;
            }
        }
        int ans = 0;
        int hi = Integer.MAX_VALUE;
        int lo = 0;
        while (lo <= hi) {
            final int mid = (lo + hi) / 2;
            if (bfs(edges, mid)) {
                ans = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        this.out.println(ans);
    }

    private boolean bfs(final int[][] edges, final int distance) {
        final boolean[] seen = new boolean[edges.length];
        final Deque<Integer> q = new ArrayDeque<>();
        q.add(0);
        seen[0] = true;
        while (!q.isEmpty()) {
            final int n = q.poll();
            for (int i = 0; i < edges[n].length; i++) {
                if (!seen[i] && edges[n][i] <= distance) {
                    seen[i] = true;
                    q.add(i);
                }
            }
        }
        for (final boolean b : seen) {
            if (!b) {
                return false;
            }
        }
        return true;
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
            is = MoocastBinarySearch.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = MoocastBinarySearch.class.getResourceAsStream("moocast.in");
            out = new PrintStream(new FileOutputStream("moocast.out"), true);
        }

        new MoocastBinarySearch(sample, is, out).solve();

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
            final Path path =
                    Paths.get(MoocastBinarySearch.class.getResource("sample.out").toURI());
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
