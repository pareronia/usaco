package com.github.pareronia.usaco._831;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

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
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collector;
import java.util.stream.IntStream;

/**
 * Problem 1. Team Tic Tac Toe
 * @see <a href="http://www.usaco.org/index.php?page=viewproblem2&cpid=831">http://www.usaco.org/index.php?page=viewproblem2&cpid=831</a>
 */
public class TeamTicTacToe {

    private final InputStream in;
    private final PrintStream out;
    
    public TeamTicTacToe(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
	private static Collector<Character, StringBuilder, String> toAString() {
		return Collector.of(
				StringBuilder::new,
				StringBuilder::append,
				StringBuilder::append,
				StringBuilder::toString);
	}

    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int[][] a = new int[3][3];
        for (int j = 0; j < 3; j++) {
            final char[] c = sc.next().toCharArray();
            for (int k = 0; k < 3; k++) {
                a[j][k] = c[k];
            }
        }
        final int[][] lines = new int[8][3];
        for (int j = 0; j < 3; j++) {
            lines[2 * j] = a[j];
            lines[2 * j + 1] = new int[] { a[0][j], a[1][j], a[2][j] };
        }
        lines[6] = new int[] { a[0][0], a[1][1], a[2][2] };
        lines[7] = new int[] { a[2][0], a[1][1], a[0][2] };
        final Map<Integer, Set<String>> map = IntStream.range(0, lines.length)
            .mapToObj(c -> Arrays.stream(lines[c]).distinct().sorted().toArray())
            .filter(c -> c.length == 1 || c.length == 2)
            .map(c -> Arrays.stream(c)
                    .mapToObj(ch -> Character.valueOf((char) ch))
                    .collect(toAString()))
            .collect(groupingBy(String::length, toSet()));
        this.out.println(map.getOrDefault(1, Set.of()).size());
        this.out.println(map.getOrDefault(2, Set.of()).size());
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
            is = TeamTicTacToe.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = TeamTicTacToe.class.getResourceAsStream("tttt.in");
            out = new PrintStream(new FileOutputStream("tttt.out"), true);
        }
        
        new TeamTicTacToe(sample, is, out).solve();
        
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
                    = Paths.get(TeamTicTacToe.class.getResource("sample.out").toURI());
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
