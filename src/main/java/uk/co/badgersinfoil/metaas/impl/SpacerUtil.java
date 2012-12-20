package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

/**
 * @author Anton.I.Neverov
 */
public class SpacerUtil {
    
    public static int calculateSpacerSize(LinkedListTree tree) {
        LinkedListToken llt = tree.getStopToken();
        String textAfterStatement = "";
        
        while (null != (llt = llt.getNext())) {
            if (llt.getChannel() == 0) {
                break;
            }
            textAfterStatement += " " + llt.getText() + " ";
        }

        String[] lines = textAfterStatement.split("(\\r\\n|\\r|\\n)");
        if (lines.length <= 2) {
            return 0;
        }

        int result = 0;

        for (int i = 1; i < lines.length - 1; i++) {
            if (!CommentUtils.stringIsNullOrWhitespace(lines[i])) {
                break;
            }
            result++;
        }
        
        return result;
    }
    
}
