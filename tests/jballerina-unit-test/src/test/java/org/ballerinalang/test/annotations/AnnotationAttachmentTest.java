/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinalang.test.annotations;

import org.ballerinalang.model.tree.NodeKind;
import org.ballerinalang.test.util.BCompileUtil;
import org.ballerinalang.test.util.CompileResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.ballerinalang.compiler.tree.BLangAnnotationAttachment;
import org.wso2.ballerinalang.compiler.tree.BLangFunction;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangConstant;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangExpression;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangLiteral;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangRecordLiteral;
import org.wso2.ballerinalang.compiler.tree.expressions.BLangSimpleVarRef;

import java.util.List;

/**
 * Class to test annotation attachments.
 *
 * @since 0.995.0
 */
public class AnnotationAttachmentTest {

    private CompileResult compileResult;

    @BeforeClass
    public void setup() {
        compileResult = BCompileUtil.compile("test-src/annotations/annot_attachments.bal");
        Assert.assertEquals(compileResult.getErrorCount(), 0);
    }

    @Test
    public void testAnnotOnType() {
        List<BLangAnnotationAttachment> attachments = (List<BLangAnnotationAttachment>)
                compileResult.getAST().getTypeDefinitions().get(2).getAnnotationAttachments();
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v1", "val", "v1 value");
    }

    @Test
    public void testAnnotOnObjectType() {
        List<BLangAnnotationAttachment> attachments = (List<BLangAnnotationAttachment>)
                compileResult.getAST().getTypeDefinitions().get(3).getAnnotationAttachments();
        Assert.assertEquals(attachments.size(), 2);
        assertNameAndKeyValuePair(attachments.get(0), "v1", "val", "v1 value object");
        assertNameAndKeyValuePair(attachments.get(1), "v2", "val", "v2 value");
    }

    @Test
    public void testAnnotOnObjectFunction() {
        BLangFunction function = getFunction("T2.setName");
        List<BLangAnnotationAttachment> attachments = function.annAttachments;
        Assert.assertEquals(attachments.size(), 2);
        assertNameAndKeyValuePair(attachments.get(0), "v3", "val", "v31 value");
        assertNameAndKeyValuePair(attachments.get(1), "v4", "val", 41L);

        attachments = function.requiredParams.get(0).annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v6", "val", "v61 value required");

        attachments = function.defaultableParams.get(0).getVariable().annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v6", "val", "v61 value defaultable");

        attachments = function.restParam.annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v6", "val", "v61 value rest");

        attachments = function.returnTypeAnnAttachments;
        Assert.assertEquals(attachments.size(), 1);
        BLangAnnotationAttachment attachment = attachments.get(0);
        Assert.assertEquals(attachment.annotationName.getValue(), "v7");
        Assert.assertNull(attachment.expr);
    }

    @Test
    public void testAnnotOnFunction() {
        BLangFunction function = getFunction("func");
        List<BLangAnnotationAttachment> attachments = function.annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v3", "val", "v33 value");

        attachments = function.requiredParams.get(0).annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v6", "val", "v63 value required");

        attachments = function.defaultableParams.get(0).getVariable().annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v6", "val", "v63 value defaultable");

        attachments = function.restParam.annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v6", "val", "v63 value rest");

        attachments = function.returnTypeAnnAttachments;
        Assert.assertEquals(attachments.size(), 1);
        BLangAnnotationAttachment attachment = attachments.get(0);
        Assert.assertEquals(attachment.annotationName.getValue(), "v7");
        Assert.assertNull(attachment.expr);
    }

    @Test
    public void testAnnotOnListener() {
        List<BLangAnnotationAttachment> attachments = (List<BLangAnnotationAttachment>)
                compileResult.getAST().getGlobalVariables().stream()
                        .filter(globalVar -> globalVar.getName().getValue().equals("lis"))
                        .findFirst()
                        .get().getAnnotationAttachments();
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v9", "val", "v91");
    }

    @Test
    public void testAnnotOnServiceOne() {
        List<BLangAnnotationAttachment> attachments = (List<BLangAnnotationAttachment>)
                compileResult.getAST().getServices().stream()
                        .filter(serviceNode -> serviceNode.getName().getValue().equals("ser"))
                        .findFirst()
                        .get().getAnnotationAttachments();
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v8", "val", "v8");
    }

    @Test
    public void testAnnotOnResourceOne() {
        BLangFunction function = getFunction("ser$$service$0.res");
        List<BLangAnnotationAttachment> attachments = function.annAttachments;
        Assert.assertEquals(attachments.size(), 2);
        assertNameAndKeyValuePair(attachments.get(0), "v3", "val", "v34");
        assertNameAndKeyValuePair(attachments.get(1), "v5", "val", "54");

        attachments = function.requiredParams.get(0).annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v6", "val", "v64");

        attachments = function.returnTypeAnnAttachments;
        Assert.assertEquals(attachments.size(), 1);
        BLangAnnotationAttachment attachment = attachments.get(0);
        Assert.assertEquals(attachment.annotationName.getValue(), "v7");
        Assert.assertNull(attachment.expr);
    }

    @Test
    public void testAnnotOnServiceTwo() {
        List<BLangAnnotationAttachment> attachments = (List<BLangAnnotationAttachment>)
                compileResult.getAST().getServices().stream()
                        .filter(serviceNode -> serviceNode.getName().getValue().equals("$anonService$1"))
                        .findFirst()
                        .get().getAnnotationAttachments();
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v8", "val", "v82");
    }

    @Test
    public void testAnnotOnResourceTwo() {
        BLangFunction function = getFunction("$anonService$1$$service$2.res");
        List<BLangAnnotationAttachment> attachments = function.annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v5", "val", "542");

        attachments = function.requiredParams.get(0).annAttachments;
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v6", "val", "v642");

        attachments = function.returnTypeAnnAttachments;
        Assert.assertEquals(attachments.size(), 1);
        BLangAnnotationAttachment attachment = attachments.get(0);
        Assert.assertEquals(attachment.annotationName.getValue(), "v7");
        Assert.assertNull(attachment.expr);
    }

    @Test
    public void testAnnotOnAnnot() {
        List<BLangAnnotationAttachment> attachments = (List<BLangAnnotationAttachment>)
                compileResult.getAST().getAnnotations().stream()
                .filter(annotationNode ->  annotationNode.getName().toString().equals("v14"))
                .findFirst()
                .get().getAnnotationAttachments();
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v10", "str", "v10 value");
    }

    @Test
    public void testAnnotOnVar() {
        List<BLangAnnotationAttachment> attachments = (List<BLangAnnotationAttachment>)
                compileResult.getAST().getGlobalVariables().stream()
                        .filter(variableNode ->  variableNode.getName().toString().equals("i"))
                        .findFirst()
                        .get().getAnnotationAttachments();
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v11", "val", 11L);
    }

    @Test
    public void testAnnotOnConst() {
        List<BLangAnnotationAttachment> attachments =
                ((BLangConstant) compileResult.getAST().getConstants().stream()
                        .filter(constant -> ((BLangConstant) constant).name.toString().equals("F"))
                        .findFirst()
                        .get())
                        .getAnnotationAttachments();
        Assert.assertEquals(attachments.size(), 1);
        assertNameAndKeyValuePair(attachments.get(0), "v12", "str", "v12 value");
    }

    private void assertNameAndKeyValuePair(BLangAnnotationAttachment attachment, String annotName, String fieldName,
                                           Object value) {
        Assert.assertEquals(attachment.annotationName.getValue(), annotName);
        Assert.assertEquals(attachment.expr.getKind(), NodeKind.RECORD_LITERAL_EXPR);

        BLangRecordLiteral recordLiteral = (BLangRecordLiteral) attachment.expr;
        Assert.assertEquals(recordLiteral.getKeyValuePairs().size(), 1);

        BLangRecordLiteral.BLangRecordKeyValue keyValuePair = recordLiteral.getKeyValuePairs().get(0);
        BLangExpression keyExpression = keyValuePair.getKey();
        String key = keyExpression instanceof BLangSimpleVarRef ?
                ((BLangSimpleVarRef) keyValuePair.key.expr).variableName.value :
                ((BLangLiteral) keyExpression).value.toString();
        Assert.assertEquals(key, fieldName);
        Assert.assertEquals(((BLangLiteral) keyValuePair.getValue()).value, value);
    }

    private BLangFunction getFunction(String functionName) {
        return (BLangFunction) compileResult.getAST().getFunctions().stream()
                .filter(function ->  functionName.equals(((BLangFunction) function).symbol.getName().toString()))
                .findFirst()
                .get();
    }
}
