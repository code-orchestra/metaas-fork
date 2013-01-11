package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Anton.I.Neverov
 */
public class CommentUtils {

    public static Comment convertStringToComment(String str) {
        if (stringIsNullOrWhitespace(str)) {
            return null;
        }
        int slIndex = str.indexOf("//");
        int mlIndex = str.indexOf("/*");
        if (slIndex == -1 && mlIndex == -1) {
            str = removeWhiteSpace(str);
            return new Comment(str, CommentType.UNKNOWN);
        }
        if (mlIndex == -1 || (slIndex != -1 && slIndex < mlIndex)) {
            Pattern p = Pattern.compile("^\\s*//", Pattern.DOTALL);
            Matcher m = p.matcher(str);
            m.lookingAt();
            str = m.replaceAll("");
            str = removeWhiteSpace(str);
            return new Comment(str, CommentType.SINGLE);
        } else if (slIndex == -1 || (mlIndex != -1 && mlIndex < slIndex)) {
            if (str.matches("^\\s*/\\*\\*/\\s*$")) {
                return new Comment("", CommentType.MULTILINE);
            }
            if (str.matches("^\\s*/\\*\\*\\*+/\\s*$")) {
                return new Comment("", CommentType.JAVADOC);
            }
            if (!str.contains("/**")) {
                Pattern p = Pattern.compile("/\\*(.*)\\*/", Pattern.DOTALL);
                Matcher m = p.matcher(str);
                if (m.lookingAt()) {
                    str = m.replaceFirst(m.group(1).replace("$", "\\$"));
                }
                str = removeWhiteSpace(str);
                return new Comment(str, CommentType.MULTILINE);
            } else {
                Matcher m = Pattern.compile("/\\*+(.*[^\\*])\\*+/", Pattern.DOTALL).matcher(str);
                if (m.lookingAt()) {
                    str = m.replaceFirst(m.group(1).replace("$", "\\$"));
                }
                m = Pattern.compile("^\\s*\\*", Pattern.MULTILINE).matcher(str);
                str = m.replaceAll("");
                str = removeWhiteSpace(str);
                return new Comment(str, CommentType.JAVADOC);
            }
        } else {
            str = removeWhiteSpace(str);
            return new Comment(str, CommentType.UNKNOWN);
        }
    }

    public static List<Comment> getCommentBefore(LinkedListTree tree) {
        LinkedListToken llt = tree.getStartToken();
        List<String> comments = new LinkedList<String>();
        List<Comment> result = new ArrayList<Comment>();
        boolean foundPrevNonHiddenToken = false;

        while (null != (llt = llt.getPrev())) {
            if (llt.getChannel() != 0 || llt.getText().matches("[\\r\\n]+") || llt.getType() == AS3Parser.SL_COMMENT || llt.getType() == AS3Parser.ML_COMMENT) {
                comments.add(0, llt.getText());
            } else {
                foundPrevNonHiddenToken = true;
                break;
            }
        }

        if (foundPrevNonHiddenToken) {
            while (comments.size() > 0) {
                if (!stringContainsEOL(comments.get(0))) {
                    comments.remove(0);
                } else {
                    comments.remove(0);
                    break;
                }
            }
        }
        Iterator it = comments.iterator();
        while (it.hasNext()) {
            Comment cmt = convertStringToComment((String) it.next());
            if (cmt != null) {
                result.add(cmt);
            }
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    public static List<Comment> getCommentAfter(LinkedListTree tree) {
        return getCommentAfter(tree, true);
    }

    public static List<Comment> getCommentAfter(LinkedListTree tree, boolean singleLine) {
        LinkedListToken start = tree.getStopToken();
        return getCommentsAfterToken(start, singleLine);
    }

    public static List<Comment> getCommentsAfterToken(LinkedListToken start, boolean singleLine) {
        LinkedListToken llt = start;

        List<String> comments = new ArrayList<String>();
        List<Comment> result = new ArrayList<Comment>();

        while (null != (llt = llt.getNext())) {
            if (llt.getChannel() != 0) {
                comments.add(llt.getText());
                if (singleLine && stringContainsEOL(llt.getText())) {
                    break;
                }
            } else {
                break;
            }
        }
        Iterator it = comments.iterator();
        while (it.hasNext()) {
            Comment cmt = convertStringToComment((String) it.next());
            if (cmt != null) {
                result.add(cmt);
            }
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    private static boolean stringContainsEOL(String str) {
        if (str.contains("\n") || str.contains("\r")) {
            return true;
        }
        return false;
    }

    public static boolean stringIsNullOrWhitespace(String str) {
        if (str == null) {
            return true;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!(Character.isSpaceChar(str.charAt(i)) || Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    private static String removeWhiteSpace(String str) {
        Pattern p = Pattern.compile("\\s*$");

        String[] lines = str.split("[\\r\\n]{1,2}");
        int[] spaces = new int[lines.length];

        for (int i = 0; i < lines.length; i++ ) {
            lines[i] = p.matcher(lines[i]).replaceFirst("");

            if (stringIsNullOrWhitespace(lines[i])) {
                spaces[i] = -1;
                continue;
            }

            int j;
            for (j = 0; j < lines[i].length(); j++) {
                if (lines[i].charAt(j) != ' ') {
                    break;
                }
            }
            spaces[i] = j;
        }

        int startIndex = 0;
        for (int i = 0; i < spaces.length; i++ ) {
            if (spaces[i] != -1) {
                startIndex = i;
                break;
            }
        }
        int endIndex = spaces.length - 1;
        for (int i = spaces.length - 1; i >= 0; i-- ) {
            if (spaces[i] != -1) {
                endIndex = i;
                break;
            }
        }

        int minSpace = -1;
        for (int space : spaces) {
            if (space == -1) {
                continue;
            }

            if (minSpace == -1 || space < minSpace) {
                minSpace = space;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i <= endIndex; i++) {
            if (minSpace > 0 && spaces[i] != -1) {
                builder.append(lines[i].substring(minSpace)).append("\n");
            } else {
                builder.append(lines[i]).append("\n");
            }
        }
        if (builder.length() > 0) {
            builder.delete(builder.length() - 1, builder.length());
        }

        return builder.toString();
    }
}
