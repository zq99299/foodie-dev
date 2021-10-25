import com.google.common.base.Splitter;

import java.util.List;

/**
 * @author mrcode
 * @date 2021/10/25 23:04
 */
public class Demo {
    public static void main(String[] args) {
        // com.google.common.base.Splitter
        Splitter splitter = Splitter.on("#");
        List<String> strings = splitter.splitToList("12345#789");
        // [12345, 789]
        System.out.println(strings);
    }
}
