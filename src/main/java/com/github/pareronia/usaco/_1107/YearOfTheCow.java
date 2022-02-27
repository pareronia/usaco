package com.github.pareronia.usaco._1107;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

/**
 * Problem 1. Year of the Cow
 * @see <a href="http://www.usaco.org/index.php?page=viewproblem2&cpid=1107">http://www.usaco.org/index.php?page=viewproblem2&cpid=1107</a>
 */
public class YearOfTheCow {

    private final InputStream in;
    private final PrintStream out;
    
    public YearOfTheCow(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private static final List<String> YEARS = List.of(
            "Ox", "Tiger", "Rabbit", "Dragon", "Snake", "Horse",
            "Goat", "Monkey", "Rooster", "Dog", "Pig", "Rat"
    );
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final Map<String, Pair> map = new HashMap<>();
        map.put("Bessie", Pair.of(0, "Ox"));
        for (int j = 0; j < n; j++) {
            final String cow1 = sc.next();
            sc.next();  // born
            sc.next();  // in
            final String dir = sc.next();
            final String animal = sc.next();
            sc.next();  // year
            sc.next();  // from
            final String cow2 = sc.next();
            int y = map.get(cow2).one;
            String animal2 = map.get(cow2).two;
            int idx = YEARS.indexOf(animal2);
            do {
                if ("next".equals(dir)) {
                    idx = (idx + 1) % 12;
                    y++;
                } else {
                    idx = (12 + idx - 1) % 12;
                    y--;
                }
                animal2 = YEARS.get(idx);
            } while (!animal2.equals(animal));
            map.put(cow1, Pair.of(y, animal2));
        }
        final int ans
            = Math.abs(map.get("Bessie").one - map.get("Elsie").one);
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

    public static void main(final String[] args) throws Exception {
        final boolean sample = isSample();
        final InputStream is;
        final PrintStream out;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        long timerStart = 0;
        if (sample) {
            is = YearOfTheCow.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new YearOfTheCow(sample, is, out).solve();
        
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
                    = Paths.get(YearOfTheCow.class.getResource("sample.out").toURI());
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

    private static final class Pair {
        private final Integer one;
        private final String two;

        private Pair(final Integer one, final String two) {
            this.one = one;
            this.two = two;
        }

        public static Pair of(final Integer one, final String two) {
            return new Pair(one, two);
        }

        @Override
        public int hashCode() {
            return Objects.hash(one, two);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Pair other = (Pair) obj;
            return Objects.equals(one, other.one)
                && Objects.equals(two, other.two);
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Pair[one=").append(one)
                   .append(", two=").append(two).append("]");
            return builder.toString();
        }
    }
}
