package com.github.pareronia.usaco._669;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;

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
import java.util.stream.Stream;

/**
 * Problem 1. Moocast
 *
 * @see <a
 *     href="https://usaco.org/index.php?page=viewproblem2&cpid=669">https://usaco.org/index.php?page=viewproblem2&cpid=669</a>
 */
public class MoocastDSU {

    private final InputStream in;
    private final PrintStream out;

    public MoocastDSU(final Boolean sample, final InputStream in, final PrintStream out) {
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
        final DSU dsu = new DSU(n);
        final int ans =
                Stream.iterate(0, j -> j < n, j -> j + 1)
                        .flatMap(
                                j ->
                                        Stream.iterate(j + 1, k -> k < n, k -> k + 1)
                                                .map(k -> new Pair(j, k)))
                        .sorted(comparing(p -> edges[p.first][p.second]))
                        .filter(p -> dsu.union(p.first, p.second))
                        .dropWhile(x -> dsu.numberOfComponents > 1)
                        .map(p -> edges[p.first][p.second])
                        .findFirst()
                        .orElseThrow();
        this.out.println(ans);
    }

    private static final class Pair {
        final int first;
        final int second;

        public Pair(final int first, final int second) {
            this.first = first;
            this.second = second;
        }
    }

    private static final class DSU {
        private final int[] id;
        private final int[] size;

        private int numberOfComponents;

        public DSU(final int size) {
            this.id = new int[size];
            this.size = new int[size];
            this.numberOfComponents = size;
            for (int i = 0; i < size; i++) {
                id[i] = i;
            }
            Arrays.fill(this.size, 1);
        }

        public boolean union(final int a, final int b) {
            final int rootA = find(a);
            final int rootB = find(b);
            if (rootA == rootB) {
                return false;
            }
            if (size[rootB] > size[rootA]) {
                return union(rootB, rootA);
            }
            id[rootB] = rootA;
            size[rootA] += size[rootB];
            numberOfComponents--;
            return true;
        }

        public int find(final int n) {
            if (id[n] == n) {
                return n;
            }
            id[n] = find(id[n]);
            return id[n];
        }
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
            is = MoocastDSU.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = MoocastDSU.class.getResourceAsStream("moocast.in");
            out = new PrintStream(new FileOutputStream("moocast.out"), true);
        }

        new MoocastDSU(sample, is, out).solve();

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
            final Path path = Paths.get(MoocastDSU.class.getResource("sample.out").toURI());
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
