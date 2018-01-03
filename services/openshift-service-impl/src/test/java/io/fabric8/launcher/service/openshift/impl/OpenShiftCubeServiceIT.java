package io.fabric8.launcher.service.openshift.impl;

import org.arquillian.cube.openshift.api.Template;
import org.arquillian.cube.openshift.api.TemplateParameter;
import org.arquillian.cube.openshift.impl.client.OpenShiftAssistant;
import org.arquillian.cube.openshift.impl.requirement.RequiresOpenshift;
import org.arquillian.cube.requirement.ArquillianConditionalRunner;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(ArquillianConditionalRunner.class)
@RequiresOpenshift
@Template(url = "https://raw.githubusercontent.com/redhat-kontinuity/jboss-eap-quickstarts/kontinu8/helloworld/.openshift-ci_cd/pipeline-template.yaml",
        parameters = {
                @TemplateParameter(name = "GIT_URL", value = "https://github.com/tnozicka/jboss-eap-quickstarts.git"),
                @TemplateParameter(name = "GIT_REF", value = "kontinu8")})
public class OpenShiftCubeServiceIT {

    @ArquillianResource
    OpenShiftAssistant openShiftAssistant;

    @Test
    public void shouldInjectOpenshiftAssistant() {
        assertThat(openShiftAssistant).isNotNull();
    }

    @Test
    public void createProjectAndApplyTemplate() throws IOException {
        // given
        openShiftAssistant.deployApplication("helloworld");

        // when
        final String project = openShiftAssistant.project();

        // then
        assertThat(project).isNotNull();
    }

    @Test
    public void getServiceURL() throws Exception {
        // given
        openShiftAssistant.deployApplication(getClass().getClassLoader().getResource("foo-service-template.yaml"));

        // when
        final URL serviceURL = openShiftAssistant.getRoute().get();

        //then
        assertThat(serviceURL).isNotNull();
    }
}