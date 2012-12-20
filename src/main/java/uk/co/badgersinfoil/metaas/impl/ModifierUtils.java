/*
 * ModifierUtils.java
 * 
 * Copyright (c) 2006 David Holroyd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.badgersinfoil.metaas.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListToken;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;


/**
 * Helpers for dealing with the modifiers list.
 */
class ModifierUtils {

    private static class ModInfo {
        public int tokenType;
        public Visibility vis;
        public String keyword;

        public ModInfo(int tokenType, Visibility vis, String keyword) {
            this.tokenType = tokenType;
            this.vis = vis;
            this.keyword = keyword;
        }
    }

    private static Map modinfoByTokenType = new HashMap();
    private static Map modinfoByVisibility = new HashMap();

    private static Set<String> nonVisibilityModifiers = new HashSet<String>();

    static {
        mapMod(AS3Parser.PRIVATE, Visibility.PRIVATE, "private");
        mapMod(AS3Parser.PUBLIC, Visibility.PUBLIC, "public");
        mapMod(AS3Parser.PROTECTED, Visibility.PROTECTED, "protected");
        mapMod(AS3Parser.INTERNAL, Visibility.INTERNAL, "internal");
        mapMod(Integer.MIN_VALUE, Visibility.DEFAULT, null);

        nonVisibilityModifiers.add("native");
        nonVisibilityModifiers.add("static");
        nonVisibilityModifiers.add("final");
        nonVisibilityModifiers.add("enumerable");
        nonVisibilityModifiers.add("explicit");
        nonVisibilityModifiers.add("override");
        nonVisibilityModifiers.add("dynamic");
        nonVisibilityModifiers.add("intrinsic");
        nonVisibilityModifiers.add("virtual");
    }

    private static void mapMod(int tokenType, Visibility vis, String keyword) {
        ModInfo inf = new ModInfo(tokenType, vis, keyword);
        modinfoByTokenType.put(new Integer(tokenType), inf);
        modinfoByVisibility.put(vis, inf);
    }

    private static ModInfo getModInfo(int tokenType) {
        return (ModInfo)modinfoByTokenType.get(new Integer(tokenType));
    }

    private static ModInfo getModInfo(Visibility vis) {
        ModInfo result = (ModInfo)modinfoByVisibility.get(vis);
        if (result == null) {
            throw new IllegalArgumentException("unknown kind of visibility: "+vis);
        }
        return result;
    }

    public static Visibility getVisibility(LinkedListTree modifiers) {
        for (ASTIterator i=new ASTIterator(modifiers); i.hasNext(); ) {
            LinkedListTree mod = i.next();
            ModInfo modInfo = getModInfo(mod.getType());
            if (modInfo != null) {
                return modInfo.vis;
            }
        }
        return Visibility.DEFAULT;
    }

    public static String getNamespace(LinkedListTree modifiers) {
        for (ASTIterator i=new ASTIterator(modifiers); i.hasNext(); ) {
            LinkedListTree mod = i.next();
            ModInfo modInfo = getModInfo(mod.getType());
            if (modInfo == null) {
                String modifier = mod.toString();
                if (!nonVisibilityModifiers.contains(modifier)) {
                    return modifier;
                }
            }
        }
        return null;
    }

    private static boolean hasModifierFlag(LinkedListTree modifiers, int type) {
        for (ASTIterator i=new ASTIterator(modifiers); i.hasNext(); ) {
            LinkedListTree mod = i.next();
            if (mod.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDynamic(LinkedListTree modifiers) {
        return hasModifierFlag(modifiers, AS3Parser.DYNAMIC);
    }

    public static boolean isOverride(LinkedListTree modifiers) {
        return hasModifierFlag(modifiers, AS3Parser.OVERRIDE);
    }

    public static boolean isNative(LinkedListTree modifiers) {
        return hasModifierFlag(modifiers, AS3Parser.NATIVE);
    }

    public static boolean isFinal(LinkedListTree modifiers) {
        return hasModifierFlag(modifiers, AS3Parser.FINAL);
    }

    public static boolean isVirtual(LinkedListTree modifiers) {
        return hasModifierFlag(modifiers, AS3Parser.VIRTUAL);
    }


    public static void setDynamic(LinkedListTree modifiers, boolean value) {
        setModifierFlag(modifiers, value, AS3Parser.DYNAMIC, "dynamic");
    }

    public static void setOverride(LinkedListTree modifiers, boolean value) {
        setModifierFlag(modifiers, value, AS3Parser.OVERRIDE, "override");
    }

    public static void setFinal(LinkedListTree modifiers, boolean value) {
        setModifierFlag(modifiers, value, AS3Parser.FINAL, "final");
    }

    private static void setModifierFlag(LinkedListTree modifiers, boolean flag, int type, String text) {
        for (ASTIterator i=new ASTIterator(modifiers); i.hasNext(); ) {
            LinkedListTree mod = i.next();
            if (mod.getType() == type) {
                if (flag) return;
                else {
                    i.remove();
                    if (modifiers.getChildCount() == 0) {
                        deleteAllChildTokens(modifiers);
                    }
                }
                return;
            }
        }
        if (flag) {
            LinkedListTree node = ASTUtils.newAST(type, text);
            node.appendToken(TokenBuilder.newSpace());
            modifiers.addChildWithTokens(node);
        }
    }

    public static void setVisibility(LinkedListTree modifiers, Visibility protection) {
        ModInfo modInfo = getModInfo(protection);
        for (ASTIterator i=new ASTIterator(modifiers); i.hasNext(); ) {
            LinkedListTree mod = i.next();
            if (isVisibilityKeyword(mod)) {
                if (modInfo.keyword == null) {
                    i.remove();
                    if (modifiers.getChildCount() == 0) {
                        deleteAllChildTokens(modifiers);
                    }
                } else {
                    mod.token.setType(modInfo.tokenType);
                    mod.token.setText(modInfo.keyword);
                }
                return;
            }
        }
        LinkedListTree mod = ASTUtils.newAST(modInfo.tokenType, modInfo.keyword);
        mod.appendToken(TokenBuilder.newSpace());
        modifiers.addChildWithTokens(mod);
    }

    private static boolean isVisibilityKeyword(LinkedListTree mod) {
        return getModInfo(mod.getType()) != null;
    }

    private static void deleteAllChildTokens(LinkedListTree modifiers) {
        for (LinkedListToken tok=modifiers.getStartToken(); tok!=null && tok!=modifiers.getStopToken(); ) {
            LinkedListToken next = tok.getNext();
            tok.delete();
            tok = next;
        }
        modifiers.setStartToken(null);
        modifiers.setStopToken(null);
    }

    /**
     * Constructs a new MODIFIERS node which represents the given
     * visibility as an AST containing either "public", "private",
     * "protected", "internal" or no children (i.e. default visibility).
     */
    public static LinkedListTree toModifiers(Visibility visibility) {
        if (Visibility.DEFAULT.equals(visibility)) {
            return ASTUtils.newPlaceholderAST(AS3Parser.MODIFIERS);
        }
        LinkedListTree modifiers = ASTUtils.newImaginaryAST(AS3Parser.MODIFIERS);
        ModInfo modInfo = getModInfo(visibility);
        LinkedListTree mod = ASTUtils.newAST(modInfo.tokenType, modInfo.keyword);
        mod.appendToken(TokenBuilder.newSpace());
        modifiers.addChildWithTokens(mod);
        return modifiers;
    }
}
