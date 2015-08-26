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
package org.wso2.carbon.javakubernetes.tomcat;

import org.wso2.carbon.javakubernetes.tomcat.pod.PodHandler;
import org.wso2.carbon.javakubernetes.tomcat.pod.implementation.ApacheTomcatPodHandler;
import org.wso2.carbon.javakubernetes.tomcat.rc.ReplicationControllerHandler;
import org.wso2.carbon.javakubernetes.tomcat.rc.implementation.ApacheTomcatRCHandler;
import org.wso2.carbon.javakubernetes.tomcat.service.ServiceHandler;
import org.wso2.carbon.javakubernetes.tomcat.service.implementation.ApacheTomcatServiceHandler;

import java.util.HashMap;
import java.util.Map;

public class KubernetesApacheTomcatHandler {

    private final PodHandler tomcatPodHandler;

    public KubernetesApacheTomcatHandler(String masterURI) {
        tomcatPodHandler = new ApacheTomcatPodHandler(masterURI);
    }

    public PodHandler getTomcatPodHandler() {
        return tomcatPodHandler;
    }

    public static void main(String[] args) {
        KubernetesApacheTomcatHandler handler = new KubernetesApacheTomcatHandler("http://127.0.0.1:8080");
        PodHandler podHandler = handler.getTomcatPodHandler();
        ReplicationControllerHandler rcHandler = new ApacheTomcatRCHandler("http://127.0.0.1:8080");
        ServiceHandler serviceHandler = new ApacheTomcatServiceHandler("http://127.0.0.1:8080");

        Map<String, String> labels = new HashMap<>();
        labels.put("name", "helloworld-tomcat");
        try {
//            podHandler.createPod("helloworldpod", "helloworld", "helloworld");
//            podHandler.createPod("samplepod", "sample", "sample");

//            podHandler.deletePod("helloworldpod");
//            podHandler.deletePod("samplepod");

//            rcHandler.createReplicationController("helloworldrc", "helloworld", "helloworld", 3);
            rcHandler.createReplicationController("samplerc", "sample", "sample", 3);

//            rcHandler.deleteReplicationController("helloworldrc");
//            rcHandler.deleteReplicationController("samplerc");

//            serviceHandler.createService("tomcat-helloworld", "helloworld");
//            serviceHandler.createService("tomcat-sample", "sample");

//            serviceHandler.deleteService("tomcat-helloworld");
//            serviceHandler.deleteService("tomcat-sample");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
