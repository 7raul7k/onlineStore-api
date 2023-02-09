package ro.myclass.onlineStoreapi;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.ProductRepo;

import java.util.Optional;

@SpringBootApplication
public class OnlineStoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineStoreApiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(CustomerRepo customerRepo, ProductRepo productRepo){
		return args -> {

			Optional<Customer> customer = customerRepo.getCustomerByEmail("mechalie2h@desdev.cn");

			System.out.println(customer.get());

			Optional<Product> product = productRepo.getProductByName("Pork - Bacon Cooked Slcd");

			System.out.println(product);

		};
	}
}
