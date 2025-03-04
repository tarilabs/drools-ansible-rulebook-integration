package org.drools.ansible.rulebook.integration.durable.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.drools.base.facttemplates.Fact;
import org.drools.ansible.rulebook.integration.api.RulesExecutor;
import org.kie.api.runtime.rule.Match;

import static org.drools.ansible.rulebook.integration.api.domain.RuleMatch.toNestedMap;

public class DurableRuleMatch {

    public static List<Map<String, Map>> asList(Collection<Match> matches) {
        return matches.stream()
                .map(DurableRuleMatch::from)
                .collect(Collectors.toList());
    }

    public static Map<String, Map> from(Match match) {
        Map<String, Object> facts = new HashMap<>();
        for (String decl : match.getDeclarationIds()) {
            Object value = match.getDeclarationValue(decl);
            if (value instanceof Fact) {
                Fact fact = (Fact) value;
                Map<String, Object> map = toNestedMap(fact.asMap());
                facts.put(decl, map);
            } else {
                facts.put(decl, value);
            }
        }

        Map<String, Map> result = new HashMap<>();
        result.put(match.getRule().getName(), facts);
        return result;
    }
}
