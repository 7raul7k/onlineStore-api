package ro.myclass.onlineStoreapi;


import org.aspectj.weaver.ast.Or;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.ProductRepo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class OnlineStoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineStoreApiApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(CustomerRepo customerRepo, ProductRepo productRepo){
		return args -> {

			//getCustomerByEmail
//			Optional<Customer> customer = customerRepo.getCustomerByEmail("mechalie2h@desdev.cn");
//
//			System.out.println(customer.get());
//
			//getProductbyName
//			Optional<Product> product = productRepo.getProductByName("Pork - Bacon Cooked Slcd");
//
//			System.out.println(product);


			//findallCustomers

//			List<Customer> customers = customerRepo.findAll();
//
//			for (Customer m : customers){
//				System.out.println(m);
//			}

//			//findAllProducts
//			List<Product> products = productRepo.findAll();
//
//			for (Product p :products){
//				System.out.println(p);
//			}


			//saveCustomer
			Customer customer = new Customer("andreipopescu@gmail.com","andreipopescu@gmail.com2023","Andrei Popescu");

//			customerRepo.save(customer);

			Optional<Product> product = productRepo.getProductByName("Glass Clear 8 Oz");


			//add OrderDetails

			Date date = new Date(2022,2,2);
			Order order = new Order(1900,date);

			customer.addOrder(order);

			OrderDetail orderDetail = new OrderDetail(1900,4,order,product.get());

			order.addOrderDetails(orderDetail);


			Customer


		};
	}
}
