package commons;

import java.util.concurrent.TimeUnit;

/**
 * Created by localadmin on 3/28/2017.
 */
public class Utils {
    public static String time(long millis) {
        return String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
