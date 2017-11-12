package shiver.me.timbers.aws.apigateway.proxy;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static com.google.code.beanmatchers.BeanMatchers.isABeanWithValidGettersAndSetters;
import static nl.jqno.equalsverifier.Warning.NONFINAL_FIELDS;
import static org.junit.Assert.assertThat;

public class ProxyResponseTest {

    @Test
    public void Proxy_response_is_a_valid_java_bean() {
        assertThat(new ProxyResponse(), isABeanWithValidGettersAndSetters());
    }

    @Test
    public void Proxy_response_has_equality() {
        EqualsVerifier.forClass(ProxyResponse.class).usingGetClass().suppress(NONFINAL_FIELDS).verify();
    }
}