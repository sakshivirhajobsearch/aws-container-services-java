package aws.container.services;

import java.util.List;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.EcsException;
import software.amazon.awssdk.services.ecs.model.ListClustersResponse; // ✅ Keep ECS version
import software.amazon.awssdk.services.ecs.model.ListServicesRequest;
import software.amazon.awssdk.services.ecs.model.ListServicesResponse;
import software.amazon.awssdk.services.eks.EksClient;
import software.amazon.awssdk.services.eks.model.EksException;
// ❌ Removed import for EKS ListClustersResponse

public class AwsContainerServices {

	public static void main(String[] args) {

		Region region = Region.US_EAST_1;

		listEcsClusters(region);
		listEksClusters(region);
	}

	public static void listEcsClusters(Region region) {

		System.out.println("==== Amazon ECS Clusters and Services ====");

		try (EcsClient ecsClient = EcsClient.builder()
				.credentialsProvider(StaticCredentialsProvider
						.create(AwsBasicCredentials.create("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY")))
				.region(Region.US_EAST_1).build();) {

			ListClustersResponse clustersResponse = ecsClient.listClusters();
			List<String> clusterArns = clustersResponse.clusterArns();

			if (clusterArns.isEmpty()) {
				System.out.println("No ECS clusters found.");
			} else {
				for (String clusterArn : clusterArns) {
					String clusterName = clusterArn.substring(clusterArn.lastIndexOf("/") + 1);
					System.out.println("Cluster: " + clusterName);

					ListServicesResponse servicesResponse = ecsClient
							.listServices(ListServicesRequest.builder().cluster(clusterArn).build());

					List<String> serviceArns = servicesResponse.serviceArns();
					if (serviceArns.isEmpty()) {
						System.out.println("  No services found.");
					} else {
						for (String serviceArn : serviceArns) {
							String serviceName = serviceArn.substring(serviceArn.lastIndexOf("/") + 1);
							System.out.println("  Service: " + serviceName);
						}
					}
				}
			}

		} catch (EcsException e) {
			System.err.println("Error fetching ECS details: " + e.awsErrorDetails().errorMessage());
		}
	}

	public static void listEksClusters(Region region) {

		System.out.println("\n==== Amazon EKS Clusters ====");

		try (EksClient eksClient = EksClient.builder().credentialsProvider(ProfileCredentialsProvider.create("default"))
				.region(Region.US_EAST_1).build();) {

			// ✅ Use fully qualified class name
			software.amazon.awssdk.services.eks.model.ListClustersResponse response = eksClient.listClusters();

			List<String> clusters = response.clusters();
			if (clusters.isEmpty()) {
				System.out.println("No EKS clusters found.");
			} else {
				for (String cluster : clusters) {
					System.out.println("EKS Cluster: " + cluster);
				}
			}

		} catch (EksException e) {
			System.err.println("Error fetching EKS details: " + e.awsErrorDetails().errorMessage());
		}
	}
}
