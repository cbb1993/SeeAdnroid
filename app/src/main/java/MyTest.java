/**
 * @author chenbinbin
 * 创建日期：2020/11/2 20:01
 * 描述：
 */
public class MyTest {

    public static void main(String[] args) {
        char[] c = {'1', '2', '3', '4'};
        r(c, 0);
    }


    public static void r(char[] cs, int index) {
        if (index == cs.length) {
            return;
        }
        r(cs, index + 1);
        System.out.println(cs[index]);
    }
}
