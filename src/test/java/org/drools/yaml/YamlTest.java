package org.drools.yaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.drools.yaml.domain.RulesSet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YamlTest {

    private static final String YAML1 =
            "  host_rules:\n" +
            "    - name:\n" +
            "      condition: sensu.data.i == 1\n" +
            "      action:\n" +
            "        assert_fact:\n" +
            "          ruleset: Test rules4\n" +
            "          fact:\n" +
            "            j: 1\n" +
            "    - name:\n" +
            "      condition: sensu.data.i == 2\n" +
            "      action:\n" +
            "        run_playbook:\n" +
            "          - name: hello_playbook.yml\n" +
            "    - name:\n" +
            "      condition: sensu.data.i == 3\n" +
            "      action:\n" +
            "        retract_fact:\n" +
            "          ruleset: Test rules4\n" +
            "          fact:\n" +
            "            j: 3\n" +
            "    - name:\n" +
            "      condition: j == 1\n" +
            "      action:\n" +
            "        post_event:\n" +
            "          ruleset: Test rules4\n" +
            "          fact:\n" +
            "            j: 4\n\n";


    @Test
    public void testReadYaml() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        RulesSet rulesSet = mapper.readValue(YAML1, RulesSet.class);
        System.out.println(rulesSet);
    }

    @Test
    public void testExecuteRules() {
        RulesExecutor rulesExecutor = RulesExecutor.createFromYaml(YAML1);
        int executedRules = rulesExecutor.execute( "{ \"sensu\": { \"data\": { \"i\":1 } } }" );
        assertEquals( 2, executedRules );
    }
}