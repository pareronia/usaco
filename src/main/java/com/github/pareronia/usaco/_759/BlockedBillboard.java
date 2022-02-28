package com.github.pareronia.usaco._759;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Problem 1. Blocked Billboard
 * @see <a href="http://www.usaco.org/index.php?page=viewproblem2&cpid=759">http://www.usaco.org/index.php?page=viewproblem2&cpid=759</a>
 */
public class BlockedBillboard {

    private final InputStream in;
    private final PrintStream out;

    public BlockedBillboard(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }

    private void handleTestCase(final Integer i, final FastScanner sc) {
        final Rectangle b1 = Rectangle.of(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
        final Rectangle b2 = Rectangle.of(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
        final Rectangle t = Rectangle.of(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
        long ans = Rectangle.totalArea(Set.of(b1, b2, t)) - t.getArea();
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
            is = BlockedBillboard.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = BlockedBillboard.class.getResourceAsStream("billboard.in");
            out = new PrintStream(new FileOutputStream("billboard.out"), true);
        }

        new BlockedBillboard(sample, is, out).solve();

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
                    = Paths.get(BlockedBillboard.class.getResource("sample.out").toURI());
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

    static class Rectangle {

        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        private Rectangle(int x1, int y1, int x2, int y2) {
            if (x1 > x2 || y1 > y2) {
                throw new IllegalArgumentException();
            }
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public static Rectangle of(int x1, int y1, int x2, int y2) {
            return new Rectangle(x1, y1, x2, y2);
        }

        public long getArea() {
            return Math.abs(x1 - x2) * Math.abs(y1 - y2);
        }

        public boolean overlap(Rectangle other) {
            return this.overlapX(other) && this.overlapY(other);
        }

        private boolean overlapX(Rectangle other) {
            return this.x1 >= other.x1 && this.x1 <= other.x2
                || other.x1 >= this.x1 && other.x1 <= this.x2; 
        }

        private boolean overlapY(Rectangle other) {
            return this.y1 >= other.y1 && this.y1 <= other.y2
                || other.y1 >= this.y1 && other.y1 <= this.y2; 
        }

        public Optional<Rectangle> intersection(Rectangle other) {
            if (!this.overlap(other)) {
                return Optional.empty();
            }
            return Optional.of(Rectangle.of(
                    Math.max(this.x1, other.x1), 
                    Math.max(this.y1, other.y1), 
                    Math.min(this.x2, other.x2), 
                    Math.min(this.y2, other.y2) 
            ));
        }

        public static long totalArea(Collection<Rectangle> rects) {
            final Map<Rectangle, Integer> map = new HashMap<>();
            for (Rectangle rect : rects) {
                final Map<Rectangle, Integer> update = new HashMap<>();
                for (Entry<Rectangle, Integer> e : map.entrySet()) {
                    rect.intersection(e.getKey()).ifPresent(ix -> update.merge(ix, -1 * e.getValue(), Integer::sum));
                }
                update.merge(rect, 1, Integer::sum);
                for (Entry<Rectangle, Integer> u : update.entrySet()) {
                    map.merge(u.getKey(), u.getValue(), Integer::sum);
                }
            }
            return map.entrySet().stream()
                   .mapToLong(e -> e.getValue() * e.getKey().getArea())
                   .sum();
        }

        @Override
        public int hashCode() {
            return Objects.hash(x1, x2, y1, y2);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Rectangle)) {
                return false;
            }
            final Rectangle other = (Rectangle) obj;
            return x1 == other.x1 && x2 == other.x2 && y1 == other.y1 && y2 == other.y2;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Rectangle [x1=").append(x1).append(", y1=").append(y1).append(", x2=").append(x2)
                   .append(", y2=").append(y2).append("]");
            return builder.toString();
        }
    }
}
