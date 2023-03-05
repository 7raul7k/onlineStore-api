package ro.myclass.onlineStoreapi;


import org.aspectj.weaver.ast.Or;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.dto.CustomerDTO;
import ro.myclass.onlineStoreapi.dto.ProductCardRequest;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.ProductRepo;
import ro.myclass.onlineStoreapi.services.CustomerService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class OnlineStoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineStoreApiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(CustomerService customerService,ProductRepo productRepo){
		return args -> {
//			Customer customer = customerService.returnCustomerByEmail("rjell2a@utexas.edu");
//
//			CreateOrderRequest createOrderRequest = new CreateOrderRequest();
//			createOrderRequest.setCustomerId(customer.getId());
//
//
//
//			Optional<Product> product = productRepo.getProductByName("Mushrooms - Honey");
//
//			ProductCardRequest productCardRequest = new ProductCardRequest(5,20);
//			ProductCardRequest productCardRequest1 = new ProductCardRequest(10,4);
//
//			List<ProductCardRequest> products = new ArrayList<>();
//			products.add(productCardRequest);
//			products.add(productCardRequest1);
//
//			createOrderRequest.setProductCardRequests(products);
//
//			customerService.addOrder(createOrderRequest);

//			List<Product> products = productRepo.getAllProductsFromOrder(1);
//
//			products.stream().forEach((k) ->{
//
//				System.out.println(k);
//			});

//			CancelOrderRequest cancelOrderRequest = CancelOrderRequest.builder().customerId(20)
//							.orderId(4)
//									.build();
//
//			customerService.cancelOrder(cancelOrderRequest);

		};
	}
}
