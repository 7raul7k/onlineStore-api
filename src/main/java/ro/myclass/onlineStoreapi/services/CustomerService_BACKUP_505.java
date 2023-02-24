package ro.myclass.onlineStoreapi.services;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.dto.CustomerDTO;
import ro.myclass.onlineStoreapi.dto.ProductCardRequest;
import ro.myclass.onlineStoreapi.dto.ProductDTO;
import ro.myclass.onlineStoreapi.exceptions.*;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.OrderDetailRepo;
import ro.myclass.onlineStoreapi.repo.OrderRepo;
import ro.myclass.onlineStoreapi.repo.ProductRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepo customerRepo;

    private OrderRepo orderRepo;

    private OrderDetailRepo orderDetailRepo;

    private ProductRepo productRepo;

    public CustomerService(CustomerRepo customerRepo, OrderRepo orderRepo, OrderDetailRepo orderDetailRepo, ProductRepo productRepo) {

        this.customerRepo = customerRepo;
        this.orderRepo = orderRepo;
        this.orderDetailRepo = orderDetailRepo;
        this.productRepo = productRepo;
    }

    public void showAllCustomers() {
        List<Customer> customerList = this.customerRepo.getAllCustomers();

        if (customerList.isEmpty()) {
            throw new ListEmptyException();
        } else {
            for (Customer m : customerList) {
                System.out.println(m);
            }
        }
    }

    @Transactional
    public void addCustomer(CustomerDTO customer) {
        Optional<Customer> optionalCustomer = this.customerRepo.getCustomerByEmail(customer.getEmail());

        if (optionalCustomer.isEmpty()) {
            try {
                Customer m = new Customer().builder()
                        .email(customer.getEmail())
                        .password(customer.getPassword())
                        .fullName(customer.getFullName())
                        .build();

                this.customerRepo.save(m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new CustomerWasFoundException();
        }
    }

    public void removeCustomer(String email) {
        Optional<Customer> customer = this.customerRepo.getCustomerByEmail(email);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        } else {
            this.customerRepo.delete(customer.get());
        }
    }

<<<<<<< HEAD
    public Customer returnCustomerByEmail(String email) {

=======
    public Customer returnCustomerByEmail(String email){
>>>>>>> task-4-update-dto
        Optional<Customer> customer = this.customerRepo.getCustomerByEmail(email);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        } else {
            return customer.get();
        }
    }

    public List<Customer> getAllCustomer() {
        List<Customer> customers = this.customerRepo.getAllCustomers();

        if (customers.isEmpty()) {
            throw new ListEmptyException();
        } else {
            return this.customerRepo.getAllCustomers();
        }

    }

<<<<<<< HEAD
    @Transactional
    @Modifying
    public void addOrder(CreateOrderRequest createOrderRequest) {

        Optional<Customer> customer = this.customerRepo.getCustomerById(createOrderRequest.getCustomerId());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Customer customer1 = customer.get();
        Order order = Order.builder().orderDate(LocalDate.now())
                .customer(customer1)
                .orderDetails(new ArrayList<>())
                .build();


        createOrderRequest.getProductCardRequests().forEach(obj -> {

            //ce:exista prodosul cantiatea disponibila update cantitate
            //cream orderDetails->ptr

            Optional<Product> product = this.productRepo.getProductById(obj.getProductId());

            if (product.isEmpty() == false) {
                Product product1 = product.get();
                if (obj.getQuantity() < product1.getStock()) {

                    product1.setStock(product1.getStock() - obj.getQuantity());

                    OrderDetail orderDetail = OrderDetail.builder().product(product1)
                            .price(product1.getPrice())
                            .quantity(obj.getQuantity())
                            .build();


                    order.addOrderDetails(orderDetail);
                    this.productRepo.save(product1);
                } else {
                    throw new StockNotAvailableException();
                }

            }


        });

        customer1.addOrder(order);

        customerRepo.saveAndFlush(customer1);



    }

=======
>>>>>>> task-4-update-dto

}
