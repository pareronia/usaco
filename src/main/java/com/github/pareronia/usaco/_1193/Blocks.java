package com.github.pareronia.usaco._1193;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Problem 3. Blocks
 * @see <a href="http://www.usaco.org/index.php?page=viewproblem&cpid=1193">http://www.usaco.org/index.php?page=viewproblem&cpid=1193</a>
 */
public class Blocks {

    private final InputStream in;
    private final PrintStream out;
    
    public Blocks(
            final Boolean sample, final InputStream in, final PrintStream out) {
        this.in = in;
        this.out = out;
    }
    
    private boolean dfs(
            final Map<Integer, Set<Character>> dice,
            final Set<Integer> unused,
            final char[] word,
            final int pos
    ) {
        final Set<Integer> matches = dice.entrySet().stream()
            .filter(e -> unused.contains(e.getKey()))
            .filter(e -> e.getValue().contains(word[pos]))
            .map(Entry::getKey)
            .collect(toSet());
        if (pos == word.length - 1) {
            return matches.size() > 0;
        }
        for (final Integer m : matches) {
            unused.remove(m);
            if (dfs(dice, unused, word, pos + 1)) {
                return true;
            }
            unused.add(m);
        }
        return false;
    }
    
    private Set<Character> asSet(final String s) {
        final HashSet<Character> set = new HashSet<>();
        for (int j = 0; j < s.length(); j++) {
            set.add(Character.valueOf(s.charAt(j)));
        }
        return set;
    }
    
    private void handleTestCase(final Integer i, final FastScanner sc) {
        final int n = sc.nextInt();
        final Map<Integer, Set<Character>> dice = new HashMap<>();
        dice.put(1, asSet(sc.next()));
        dice.put(2, asSet(sc.next()));
        dice.put(3, asSet(sc.next()));
        dice.put(4, asSet(sc.next()));
        for (int j = 0; j < n; j++) {
            final char[] word = sc.next().toCharArray();
            final String ans;
            if (dfs(dice, new HashSet<>(Set.of(1, 2, 3, 4)), word, 0)) {
                ans = "YES";
            } else {
                ans = "NO";
            }
            this.out.println(ans);
        }
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
            is = Blocks.class.getResourceAsStream("sample.in");
            out = new PrintStream(baos, true);
            timerStart = System.nanoTime();
        } else {
            is = System.in;
            out = System.out;
        }
        
        new Blocks(sample, is, out).solve();
        
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
                    = Paths.get(Blocks.class.getResource("sample.out").toURI());
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
