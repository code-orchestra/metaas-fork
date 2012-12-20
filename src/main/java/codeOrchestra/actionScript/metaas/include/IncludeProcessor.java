package codeOrchestra.actionScript.metaas.include;

import org.antlr.runtime.RecognitionException;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASIncludeDirective;
import uk.co.badgersinfoil.metaas.dom.ASPackage;
import uk.co.badgersinfoil.metaas.dom.ASType;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.ASTASPackage;
import uk.co.badgersinfoil.metaas.impl.ASTASType;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

import java.io.*;

/**
 * @author Alexander Eliseyev
 */
public class IncludeProcessor {

    private ASCompilationUnit compilationUnit;
    private File sourceFile;
    private IncludeProcessResult processResult;

    public IncludeProcessor(ASCompilationUnit compilationUnit, File sourceFile) {
        this.sourceFile = sourceFile;
        this.compilationUnit = compilationUnit;
    }

    public IncludeProcessResult process() {
        if (processResult != null) {
            throw new IllegalStateException("Can't run twice");
        }
        processResult = new IncludeProcessResult();

        ASPackage asPackage = compilationUnit.getPackage();
        if (asPackage == null) {
            return processResult;
        }

        // Package includes
        for (ASIncludeDirective packageIncludeDirective : asPackage.getIncludeDirectives()) {
            processPackageInclude(asPackage, packageIncludeDirective);
        }

        // Package type includes
        ASType type = compilationUnit.getType();
        if (type != null) {
            for (ASIncludeDirective typeIncludeDirective : type.getIncludeDirectives()) {
                processTypeInclude(type, typeIncludeDirective);
            }
        }

        // Out-of-package types
        for (ASType outOfPackageType : compilationUnit.getOutOfPackageTypes()) {
            for (ASIncludeDirective oopTypeIncludeDirective : outOfPackageType.getIncludeDirectives()) {
                processTypeInclude(type, oopTypeIncludeDirective);
            }
        }

        return processResult;
    }

    private String getIncludePath(ASIncludeDirective includeDirective) {
        return sourceFile.getParent() + File.separator + includeDirective.getPath();
    }

    private void processTypeInclude(ASType type, ASIncludeDirective includeDirective) {
        ASTASType typeImpl = (ASTASType) type;
        String includeFilePath = getIncludePath(includeDirective);

        String includeContent = null;
        try {
            includeContent = "{\n" + IncludeProcessorUtil.readFile(includeFilePath) + "\n}";
        } catch (IOException e) {
            processResult.addNotFoundFile(includeFilePath);
            return;
        }

        LinkedListTree typeTypeBlockAST = (LinkedListTree) typeImpl.getAST().getFirstChildWithType(AS3Parser.TYPE_BLOCK);
        if (typeTypeBlockAST == null) {
            return;
        }

        AS3Parser as3Parser = ASTUtils.parse(new StringReader(includeContent));
        try {
            LinkedListTree includedTree = AS3FragmentParser.tree(as3Parser.typeBlock());
            moveChildren(includedTree, typeTypeBlockAST);
        } catch (RecognitionException e) {
            processResult.addParsingError(includeFilePath, e);
            return;
        }

        processResult.addProcessedFile(includeFilePath);
    }

    private void processPackageInclude(ASPackage asPackage, ASIncludeDirective includeDirective) {
        ASTASPackage packageImpl = (ASTASPackage) asPackage;
        String includeFilePath = getIncludePath(includeDirective);

        String includeContent = null;
        try {
            includeContent = "{\n" + IncludeProcessorUtil.readFile(includeFilePath) + "\n}";
        } catch (IOException e) {
            processResult.addNotFoundFile(includeFilePath);
            return;
        }

        LinkedListTree packageBlockAST = (LinkedListTree) packageImpl.getAST().getFirstChildWithType(AS3Parser.BLOCK);
        if (packageBlockAST == null) {
            return;
        }

        AS3Parser as3Parser = ASTUtils.parse(new StringReader(includeContent));
        try {
            LinkedListTree includedTree = AS3FragmentParser.tree(as3Parser.packageBlock());
            moveChildren(includedTree, packageBlockAST);
        } catch (RecognitionException e) {
            processResult.addParsingError(includeFilePath, e);
            return;
        }

        processResult.addProcessedFile(includeFilePath);
    }

    private void moveChildren(LinkedListTree sourceTree, LinkedListTree targetTree) {
        for (int i = 0; i < sourceTree.getChildCount(); i++) {
            LinkedListTree child = (LinkedListTree) sourceTree.getChild(i);
            targetTree.addChildWithTokens(child);
        }
    }


}
