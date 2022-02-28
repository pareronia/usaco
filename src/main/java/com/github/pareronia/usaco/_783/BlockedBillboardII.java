package com.github.pareronia.usaco._783;

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
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Problem 1. Blocked Billboard II
 * @see <a href="http://www.usaco.org/index.php?page=viewproblem2&cpid=783">http://www.usaco.org/index.php?page=viewproblem2&cpid=783</a>
 */
public class BlockedBillboardII {

    private final InputStream in;
    private final PrintStream out;
    
    public BlockedBillboardII(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final Rectangle b = Rectangle.of(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
        final Rectangle f = Rectangle.of(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
        final int covered = (int) b.corners().stream()
            .filter(c -> f.contains(c[0], c[1]))
            .count();
        final long ans;
        if (covered == 4) {
            ans = 0;
        } else if (covered == 2) {
            ans = b.getArea() - b.intersection(f).get().getArea();
        } else {
            ans = b.getArea();
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
            is = BlockedBillboardII.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = BlockedBillboardII.class.getResourceAsStream("billboard.in");
            out = new PrintStream(new FileOutputStream("billboard.out"), true);
        }
        
        new BlockedBillboardII(sample, is, out).solve();
        
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
                    = Paths.get(BlockedBillboardII.class.getResource("sample.out").toURI());
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

    private static class Rectangle {

        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        private Rectangle(final int x1, final int y1, final int x2, final int y2) {
            if (x1 > x2 || y1 > y2) {
                throw new IllegalArgumentException();
            }
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public static Rectangle of(final int x1, final int y1, final int x2, final int y2) {
            return new Rectangle(x1, y1, x2, y2);
        }

        public long getArea() {
            return (long) Math.abs(x1 - x2) * (long) Math.abs(y1 - y2);
        }
        
        public Set<int[]> corners() {
            return Set.of(
                new int[] { x1, y1 },
                new int[] { x2, y2 },
                new int[] { x1, y2 },
                new int[] { x2, y1 }
            );
        }
        
        public boolean contains(final int x, final int y ) {
            return x1 <= x && x <= x2 && y1 <= y && y <= y2;
        }
        
        public boolean overlap(final Rectangle other) {
            return this.overlapX(other) && this.overlapY(other);
        }

        private boolean overlapX(final Rectangle other) {
            return this.x1 >= other.x1 && this.x1 <= other.x2
                || other.x1 >= this.x1 && other.x1 <= this.x2;
        }

        private boolean overlapY(final Rectangle other) {
            return this.y1 >= other.y1 && this.y1 <= other.y2
                || other.y1 >= this.y1 && other.y1 <= this.y2;
        }

        public Optional<Rectangle> intersection(final Rectangle other) {
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
    }
}
