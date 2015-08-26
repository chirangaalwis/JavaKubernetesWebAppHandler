/*
* Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.wso2.carbon.javakubernetes.tomcat.pod.implementation;

import io.fabric8.kubernetes.api.KubernetesClient;
import io.fabric8.kubernetes.api.KubernetesFactory;
import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.model.*;
import org.wso2.carbon.javakubernetes.tomcat.pod.PodHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApacheTomcatPodHandler implements PodHandler {

    private final KubernetesClient client;

    private static final String KUBERNETES_COMPONENT_KIND = "Pod";
    private static final String LABEL_NAME = "name";

    public ApacheTomcatPodHandler(String uri) {
        client = new KubernetesClient(new KubernetesFactory(uri));
    }

    @Override
    public void createPod(String podId, String imageName, String podLabel) throws Exception {
        Pod pod = preparePod(podId, imageName, podLabel);
        client.createPod(pod);
    }

    @Override
    public PodList getPods() {
        return client.getPods();
    }

    @Override
    public void deletePod(String podId) throws Exception {
        client.deletePod(podId);
    }

    private Pod preparePod(String podId, String imageName, String podLabel) {
        Pod pod = new Pod();

        pod.setApiVersion(Pod.ApiVersion.V_1);
        pod.setKind(KUBERNETES_COMPONENT_KIND);

        ObjectMeta metaData = new ObjectMeta();
        metaData.setName(podId);
        Map<String, String> labels = new HashMap<>();
        labels.put(LABEL_NAME, podLabel);
        metaData.setLabels(labels);

        pod.setMetadata(metaData);

        PodSpec podSpec = new PodSpec();

        Container podContainer =  new Container();
        podContainer.setName(podLabel);
        podContainer.setImage(imageName);
        List<Container> containers = new ArrayList<>();
        containers.add(podContainer);
        podSpec.setContainers(containers);
        pod.setSpec(podSpec);

        /*KubernetesHelper.setName(pod, podId);

        Map<String, String> labels = new HashMap<>();

        if((labels != null) && (!labels.isEmpty())) {
            pod.getMetadata().setLabels(labels);
        }
        PodSpec podSpec = new PodSpec();
        pod.setSpec(podSpec);

        Container manifestContainer = new Container();
        manifestContainer.setName(podName);
        manifestContainer.setImage(imageName);

        List<Container> containers = new ArrayList<>();
        containers.add(manifestContainer);
        podSpec.setContainers(containers);
*/
        return pod;
    }
}
