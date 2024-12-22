import java.util.*;

public class Main {

    static final String PATTERN = "RLRFR";
    static final int REPEATS = 1000;
    static final int ROUTELENGTH = 100;

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public synchronized static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < REPEATS; i++) {
            Runnable logic = () -> {
                String route = generateRoute(PATTERN, ROUTELENGTH);
                int count = 0;
                for (int j = 0; j < route.length(); j++) {
                    if (route.charAt(j) == 'R') {
                        count++;
                    }
                }
                if (!sizeToFreq.containsKey(count)) {
                    sizeToFreq.put(count, 1);
                } else {
                    sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                }
                //System.out.println("Количество повторений: " + count);
            };

            Thread thread = new Thread(logic);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int maxFrequency = 0;
        int mostFrequentCount = 0;

        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequentCount = entry.getKey();
            }
        }
        System.out.printf("""
                Самое частое количество повторений %d (встретилось %d раз)
                Другие размеры:
                """, mostFrequentCount, maxFrequency);

        int finalMostFrequentCount = mostFrequentCount;
        sizeToFreq.forEach((size, freq) -> {
            if (size != finalMostFrequentCount) {
                System.out.printf("- %d (%d раз)\n", size, freq);
            }
        });
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}