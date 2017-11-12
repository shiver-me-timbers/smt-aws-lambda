package shiver.me.timbers.aws.apigateway.proxy;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static com.google.code.beanmatchers.BeanMatchers.isABeanWithValidGettersAndSetters;
import static nl.jqno.equalsverifier.Warning.NONFINAL_FIELDS;
import static org.junit.Assert.assertThat;

public class ProxyRequestTest {

    @Test
    public void Proxy_request_is_a_valid_java_bean() {
        assertThat(new ProxyRequest(), isABeanWithValidGettersAndSetters());
    }

    @Test
    public void Proxy_request_has_equality() {
        EqualsVerifier.forClass(ProxyRequest.class).usingGetClass().suppress(NONFINAL_FIELDS).verify();
    }
}