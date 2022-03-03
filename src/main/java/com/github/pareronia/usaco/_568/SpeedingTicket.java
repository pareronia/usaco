package com.github.pareronia.usaco._568;

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
import java.util.NavigableMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Problem 2. Speeding Ticket
 * @see <a href="http://www.usaco.org/index.php?page=viewproblem2&cpid=568">http://www.usaco.org/index.php?page=viewproblem2&cpid=568</a>
 */
public class SpeedingTicket {

    private final InputStream in;
    private final PrintStream out;
    
    public SpeedingTicket(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final int m = sc.nextInt();
        final TreeMap<Integer, Integer> map = new TreeMap<>();
        int nn = 0;
        for (int j = 0; j < n; j++) {
            final int tmp = sc.nextInt();
            map.put(nn, sc.nextInt());
            nn += tmp;
        }
        final int[][] b = new int[m][3];
        int mm = 0;
        for (int j = 0; j < m; j++) {
            final int tmp = sc.nextInt();
            b[j] = new int[] { mm, mm + tmp, sc.nextInt() };
            mm += tmp;
        }
        int ans = 0;
        for (int j = 0; j < m; j++) {
            final Integer ceilingKey = map.ceilingKey(b[j][1]);
            final Integer to = ceilingKey != null ? ceilingKey : map.lastKey();
            final boolean toInclusive = ceilingKey == null;
            final NavigableMap<Integer, Integer> subMap
                = map.subMap(map.floorKey(b[j][0]), true, to, toInclusive);
            for (final int v : subMap.values()) {
                if (b[j][2] > v) {
                    ans = Math.max(ans, b[j][2] - v);
                }
            }
        }
        this.out.println(ans);
    }
    
    public void solve() {
        try (final FastScanner sc = new FastScanner(this.in)) {
            final int numberOfTestCases;
            if (isSample()) {
                numberOfTestCases = sc.nextInt();
            } else {
                numberOfTestCases = 1;
            }
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
            is = SpeedingTicket.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = SpeedingTicket.class.getResourceAsStream("speeding.in");
            out = new PrintStream(new FileOutputStream("speeding.out"), true);
        }
        
        new SpeedingTicket(sample, is, out).solve();
        
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
                    = Paths.get(SpeedingTicket.class.getResource("sample.out").toURI());
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
