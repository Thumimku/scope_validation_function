package com.wso2.custom.scope.validation.xacml.function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.balana.attr.AttributeValue;
import org.wso2.balana.attr.BagAttribute;
import org.wso2.balana.attr.StringAttribute;
import org.wso2.balana.cond.EvaluationResult;
import org.wso2.balana.attr.BooleanAttribute;
import org.wso2.balana.cond.FunctionBase;
import org.wso2.balana.ctx.EvaluationCtx;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This is a custom function to return a boolean that indicates the set of scope requested for token issuance
 * is sub set of allowed scopes. Even if one scope if requested out of allowed scopes function will deny.
 *
 * This function consumes two input values.
 *
 * 1. The list of scopes requested (Bag of Strings)
 * 2. Comma separated allowed scopes (String)
 *
 * This function returns the list of departments (Bag of Strings)
 */

public class IsSubSetFunction extends FunctionBase {

    public static final String FUNCTION_NAME = "Is-Sub-Set-Function";
    private static final int FUNCTION_ID = 0;
    private static final String params[] = {StringAttribute.identifier, StringAttribute.identifier};
    private static final boolean[] bagParams = new boolean[]{true, false};
    private static Log log = LogFactory.getLog(IsSubSetFunction.class);

    public IsSubSetFunction() {

        super(FUNCTION_NAME, FUNCTION_ID, params, bagParams, BooleanAttribute.identifier, false);
    }

    public static Set getSupportedIdentifiers() {

        Set set = new HashSet();
        set.add(FUNCTION_NAME);
        return set;
    }

    @Override
    public EvaluationResult evaluate(List inputs, EvaluationCtx context) {

        AttributeValue[] argValues = new AttributeValue[inputs.size()];
        EvaluationResult result = this.evalArgs(inputs, context, argValues);

        if (result != null) {
            return result;
        } else {
            BagAttribute bag = (BagAttribute) argValues[0];
            AttributeValue item = argValues[1];
            String allowedScopes = ((StringAttribute) item).getValue();
            List<String> allowedScopesList = Arrays.asList(allowedScopes.split(","));
            boolean allowed = true;

            if (log.isDebugEnabled()) {
                log.debug("Received a bag of strings of size " + bag.size() + " containing the list of scopes." +
                        "Allowed scopes are : " + allowedScopes + ".");
            }
            for (Iterator i = bag.iterator(); i.hasNext(); ) {

                StringAttribute scopeAttribute = (StringAttribute) i.next();
                String requestedScope = scopeAttribute.getValue();
                if (!allowedScopesList.contains(requestedScope)){
                    allowed = false;
                    BooleanAttribute tokenIssuranceAllowed = BooleanAttribute.getInstance(allowed);
                    return new EvaluationResult(tokenIssuranceAllowed);
                }
            }
            BooleanAttribute tokenIssuranceAllowed = BooleanAttribute.getInstance(allowed);
            return new EvaluationResult(tokenIssuranceAllowed);
        }
    }
}
