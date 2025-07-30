package aws.container.services;

import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.ListServicesRequest;
import software.amazon.awssdk.services.eks.EksClient;

public class AwsContainerServicesTestHelper {

	public static void printEcsInfo(EcsClient ecsClient) {
		for (String clusterArn : ecsClient.listClusters().clusterArns()) {
			System.out.println("Cluster: " + clusterArn);
			for (String serviceArn : ecsClient.listServices(ListServicesRequest.builder().cluster(clusterArn).build())
					.serviceArns()) {
				System.out.println("  Service: " + serviceArn);
			}
		}
	}

	public static void printEksInfo(EksClient eksClient) {
		for (String cluster : eksClient.listClusters().clusters()) {
			System.out.println("EKS Cluster: " + cluster);
		}
	}
}