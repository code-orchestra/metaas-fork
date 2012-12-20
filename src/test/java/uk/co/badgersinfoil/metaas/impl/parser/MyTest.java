package uk.co.badgersinfoil.metaas.impl.parser;

/**
 * Created by IntelliJ IDEA.
 * User: eliseev
 * Date: 08.12.2009
 * Time: 13:52:14
 * To change this template use File | Settings | File Templates.
 */
public class MyTest {

    public static void main(String[] args) {
        String xml ="<a><b></b></a>";
        parse(xml);
    }

    public static void parse(String xml) {
        int index = 1;
        int opening = 1;
        int closing = 0;

        int firstSubOpen = -1;
        int firstSlash = -1;


        String nextChar = null;

        while (opening != closing && index < xml.length() - 1) {
            String theChar = String.valueOf(xml.charAt(index));
            print(theChar);

            if ("/".equals(theChar) && firstSlash == -1) {
                firstSlash = index;
            }

            if ("<".equals(theChar) && firstSubOpen == -1) {
                firstSubOpen = index;
            }

            index++;
        }

        print("Subopen: " + firstSubOpen);
    }

    public static void print(String str) {
        System.out.println(str);
    }

}
