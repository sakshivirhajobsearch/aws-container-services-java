package aws.container.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.ListClustersResponse;
import software.amazon.awssdk.services.ecs.model.ListServicesRequest;
import software.amazon.awssdk.services.ecs.model.ListServicesResponse;
import software.amazon.awssdk.services.eks.EksClient;

public class AwsContainerServicesTest {

	@Test
	@DisplayName("Test ECS cluster and service listing without exception")
	public void testListEcsClusters() {
		EcsClient ecsMock = mock(EcsClient.class);

		ListClustersResponse mockClustersResponse = ListClustersResponse.builder()
				.clusterArns(Collections.singletonList("arn:aws:ecs:us-east-1:123456789012:cluster/test-ecs-cluster"))
				.build();

		ListServicesResponse mockServicesResponse = ListServicesResponse.builder()
				.serviceArns(Collections.singletonList("arn:aws:ecs:us-east-1:123456789012:service/test-service"))
				.build();

		when(ecsMock.listClusters()).thenReturn(mockClustersResponse);
		when(ecsMock.listServices(any(ListServicesRequest.class))).thenReturn(mockServicesResponse);

		assertDoesNotThrow(() -> AwsContainerServicesTestHelper.printEcsInfo(ecsMock));

		verify(ecsMock).listClusters();
		verify(ecsMock).listServices(any(ListServicesRequest.class));
	}

	@Test
	@DisplayName("Test EKS cluster listing without exception")
	public void testListEksClusters() {
		EksClient eksMock = mock(EksClient.class);

		software.amazon.awssdk.services.eks.model.ListClustersResponse mockResponse = software.amazon.awssdk.services.eks.model.ListClustersResponse
				.builder().clusters(Collections.singletonList("test-eks-cluster")).build();

		when(eksMock.listClusters()).thenReturn(mockResponse);

		assertDoesNotThrow(() -> AwsContainerServicesTestHelper.printEksInfo(eksMock));

		verify(eksMock).listClusters();
	}
}
