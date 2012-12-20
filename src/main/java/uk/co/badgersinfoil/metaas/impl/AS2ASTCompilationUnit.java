package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.*;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class AS2ASTCompilationUnit extends ASTASCompilationUnit {

    public AS2ASTCompilationUnit(LinkedListTree ast) {
        super(ast);
    }

    public String getPackageName() {
        LinkedListTree ident = ASTUtils.findChildByType(getTypeTree(), AS3Parser.IDENTIFIER);
        String identText = ASTUtils.identText(ident);

        if (!identText.contains(".")) {
            return null;
        }

        return identText.substring(0,identText.lastIndexOf("."));
    }

    public List<ScriptElement> getAllMembers() {
        LinkedListTree block = ASTUtils.findChildByType(ast, AS3Parser.BLOCK);
        List<ScriptElement> result = new ArrayList<ScriptElement>();

        for (int i = 0; i < block.getChildCount(); i++) {
            if (block.getChild(i).getType() == AS3Parser.CLASS_DEF) {
                result.add(new ASTASClassType((LinkedListTree) block.getChild(i)));
            } else if (block.getChild(i).getType() == AS3Parser.INTERFACE_DEF) {
                result.add(new ASTASInterfaceType((LinkedListTree) block.getChild(i)));
            }
        }

        return result;
    }

    public ASMethod getGlobalFunction() {
        return null;
    }

    public ASField getGlobalField() {
        return null;
    }

    public ASType getType() {
        LinkedListTree type = getTypeTree();
        if (type == null) {
            return null;
        }
        if (type.getType() == AS3Parser.CLASS_DEF) {
            return new ASTASClassType(type);
        }
        if (type.getType() == AS3Parser.INTERFACE_DEF) {
            return new ASTASInterfaceType(type);
        }
        return null;
    }

    private LinkedListTree getTypeTree() {
        LinkedListTree type = ASTUtils.findChildByType(ast, AS3Parser.CLASS_DEF);
        if (type != null) {
            return type;
        }
        type = ASTUtils.findChildByType(ast, AS3Parser.INTERFACE_DEF);
        if (type != null) {
            return type;
        }
        return null;
    }

    public ASNamespaceDeclaration getGlobalNamespaceDeclaration() {
        return null;
    }

    public List<ASField> getOutOfPackageFields() {
        return null;
    }

    public List<ASMethod> getOutOfPackageFunctions() {
        return null;
    }

    public List<ASType> getOutOfPackageTypes() {
        return null;
    }

    public List<String> getOutOfPackageImports() {
        return null;
    }

    public List<Statement> getOutOfPackageStatements() {
        return null;
    }

    public List<ASNamespaceDeclaration> getOutOfPackageNamespaces() {
        return null;
    }

    public void setPackageName(String name) {
    }

    public ASPackage getPackage() {
        // No packages in AS2
        return null;
    }

    public List<String> findImports() {
        ASTIterator i = new ASTIterator(ast);
        LinkedListTree imp;
        List result = new ArrayList();
        while ((imp = i.search(AS3Parser.IMPORT)) != null) {
            result.add(ASTUtils.identStarText(imp.getFirstChild()));
        }

        // RE-932
        ASType type = getType();
        if (type != null) {
            result.addAll(type.findImports());
        }

        return result;
    }
}
