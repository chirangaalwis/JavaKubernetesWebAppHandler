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
package org.wso2.carbon.javakubernetes.tomcat.service.implementation;

import org.apache.stratos.kubernetes.client.KubernetesApiClient;
import org.apache.stratos.kubernetes.client.KubernetesConstants;
import org.apache.stratos.kubernetes.client.exceptions.KubernetesClientException;
import org.apache.stratos.kubernetes.client.interfaces.KubernetesAPIClientInterface;
import org.wso2.carbon.javakubernetes.tomcat.service.ServiceHandler;

public class ApacheTomcatServiceHandler implements ServiceHandler {

    private final KubernetesAPIClientInterface client;

    public ApacheTomcatServiceHandler(String uri) {
        client = new KubernetesApiClient(uri);
    }

    @Override
    public void createService(String serviceId, String serviceName) throws Exception {
        client.createService(serviceId, serviceName, 30001, KubernetesConstants.NODE_PORT, "http-1", 8080, "None");
    }

    @Override
    public void deleteService(String serviceId) throws KubernetesClientException {
        client.deleteService(serviceId);
    }
}
