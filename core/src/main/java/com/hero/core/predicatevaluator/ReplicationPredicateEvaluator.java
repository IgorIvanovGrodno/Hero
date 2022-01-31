package com.hero.core.predicatevaluator;

import com.day.cq.search.Predicate;
import com.day.cq.search.eval.AbstractPredicateEvaluator;
import com.day.cq.search.eval.EvaluationContext;
import org.osgi.service.component.annotations.Component;
//import org.apache.felix.scr.annotations.Component;


//@Component(metatype = false, factory = "com.day.cq.search.eval.PredicateEvaluator/replic")
@Component(factory = "com.day.cq.search.eval.PredicateEvaluator/replic")
public class ReplicationPredicateEvaluator extends AbstractPredicateEvaluator {
    static final String PE_NAME = "replic";

    static final String PN_LAST_REPLICATED_BY = "cq:lastReplicatedBy";
    static final String PN_LAST_REPLICATED = "cq:lastReplicated";
    static final String PN_LAST_REPLICATED_ACTION = "cq:lastReplicationAction";

    static final String PREDICATE_BY = "by";
    static final String PREDICATE_SINCE = "since";
    static final String PREDICATE_SINCE_OP = " >= ";
    static final String PREDICATE_ACTION = "action";

    public String getXPathExpression(Predicate predicate,
                                     EvaluationContext context) {

        String date = predicate.get(PREDICATE_SINCE);
        String user = predicate.get(PREDICATE_BY);
        String action = predicate.get(PREDICATE_ACTION);

        StringBuilder sb = new StringBuilder();

        if (date != null) {
            sb.append(PN_LAST_REPLICATED).append(PREDICATE_SINCE_OP);
            sb.append("xs:dateTime('").append(date).append("')");
        }
        if (user != null) {
            addAndOperator(sb);
            sb.append(PN_LAST_REPLICATED_BY);
            sb.append("='").append(user).append("'");
        }
        if (action != null) {
            addAndOperator(sb);
            sb.append(PN_LAST_REPLICATED_ACTION);
            sb.append("='").append(action).append("'");
        }
        String xpath = sb.toString();

        return xpath;
    }

    private void addAndOperator(StringBuilder sb) {
        if (sb.length() != 0) {
            sb.append(" and ");
        }
    }
}
