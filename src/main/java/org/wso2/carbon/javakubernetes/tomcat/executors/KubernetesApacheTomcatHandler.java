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
package org.wso2.carbon.javakubernetes.tomcat.executors;

import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerException;
import io.fabric8.kubernetes.api.model.Pod;
import org.wso2.carbon.javadocker.tomcat.IDockerImageBuilder;
import org.wso2.carbon.javadocker.tomcat.implementation.DockerWebAppImageBuilder;
import org.wso2.carbon.javakubernetes.tomcat.pod.PodHandler;
import org.wso2.carbon.javakubernetes.tomcat.pod.implementation.ApacheTomcatPodHandler;
import org.wso2.carbon.javakubernetes.tomcat.rc.ReplicationControllerHandler;
import org.wso2.carbon.javakubernetes.tomcat.rc.implementation.ApacheTomcatRCHandler;
import org.wso2.carbon.javakubernetes.tomcat.service.ServiceHandler;
import org.wso2.carbon.javakubernetes.tomcat.service.implementation.ApacheTomcatServiceHandler;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class KubernetesApacheTomcatHandler {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String ENDPOINT_URL = "http://127.0.0.1:8080";
    private static final PodHandler POD_HANDLER = new ApacheTomcatPodHandler(ENDPOINT_URL);
    private static final ReplicationControllerHandler REPLICATION_CONTROLLER_HANDLER = new ApacheTomcatRCHandler(ENDPOINT_URL);
    private static final ServiceHandler SERVICE_HANDLER = new ApacheTomcatServiceHandler(ENDPOINT_URL);

    public static void main(String... args) {
        System.out.print("Enter the war file path: ");
        String warFilePath = scanner.nextLine();
        Path warFile = Paths.get(warFilePath);

        int choice = -1;
        do {
            showMenu();
            choice = scanner.nextInt();
            System.out.println();
        }
        while(choice < 1 && choice > 3);

        process(choice, warFile);
    }

    private static void build(Path warFilePath) throws DockerCertificateException,
            DockerException, InterruptedException {
        IDockerImageBuilder imageBuilder = new DockerWebAppImageBuilder();
        imageBuilder.buildImage(warFilePath);
    }

    private static void create(Path warFilePath) throws Exception {
        String imageName = generateImageIdentifier(warFilePath);
        REPLICATION_CONTROLLER_HANDLER.createReplicationController(imageName + "rc", imageName, imageName, 3);
        SERVICE_HANDLER.createService("tomcat-" + imageName, imageName);
    }

    private static void delete(Path warFilePath) throws Exception{
        REPLICATION_CONTROLLER_HANDLER.deleteReplicationController(generateImageIdentifier(warFilePath) + "rc");
        SERVICE_HANDLER.deleteService("tomcat-" + generateImageIdentifier(warFilePath));
        deletePods(warFilePath);
    }

    private static void deletePods(Path warFilePath) throws Exception {
        String imageName = generateImageIdentifier(warFilePath);

        for(Pod pod : POD_HANDLER.getPods().getItems()) {
            if(pod.getMetadata().getName().contains(imageName)) {
                POD_HANDLER.deletePod(pod.getMetadata().getName());
            }
        }
    }

    private static void showMenu() {
        String menuContent = "***KUBERNETES WAR FILE HANDLER***\n"
                + "1 - Deploy war file\n"
                + "2 - Undeploy war file\n"
                + "3 - Exit\n"
                + "Please enter your choice: ";

        System.out.print(menuContent);
    }

    private static void process(int choice, Path warFilepath) {
        try {
            switch (choice) {
            case 1 :
                build(warFilepath);
                Thread.sleep(5000);
                create(warFilepath);
                break;
            case 2:
                delete(warFilepath);
                break;
            case 3:
                System.exit(0);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    private static String generateImageIdentifier(Path warFilePath) {
        String fileName = warFilePath.getFileName().toString();
        return fileName.substring(0, fileName.length() - 4);
    }

}
