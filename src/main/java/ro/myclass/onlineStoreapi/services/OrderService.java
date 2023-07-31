package ro.myclass.onlineStoreapi.services;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.exceptions.*;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.OrderRepo;
import ro.myclass.onlineStoreapi.repo.ProductRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    public OrderRepo orderRepo;

    public CustomerRepo customerRepo;

    public ProductRepo productRepo;

    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<Order> getAllOrder(){

        List<Order> orders = this.orderRepo.findAll();

        if(orders.isEmpty()){
            throw new ListEmptyException();
        }else{
            return orders;
        }

    }

    @Transactional
    @Modifying
    public void addOrder(CreateOrderRequest createOrderRequest) {

        Optional<Customer> customer = this.customerRepo.findById((long) createOrderRequest.getCustomerId());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Customer customer1 = customer.get();
        Order order = Order.builder().orderDate(LocalDate.now())
                .customer(customer1)
                .orderDetails(new ArrayList<>())
                .build();

        createOrderRequest.getProductCardRequests().forEach(obj -> {

            //ce:exista produsul cantiatea disponibila update cantitate
            //cream orderDetails->ptr

            Optional<Product> product = this.productRepo.findById((long) obj.getProductId());

            if (product.isEmpty() == false) {
                Product product1 = product.get();
                OrderDetail orderDetail = OrderDetail.builder().product(product1)
                        .price(product1.getPrice())
                        .quantity(obj.getQuantity())
                        .build();
                if (obj.getQuantity() < product1.getStock()) {

                    product1.setStock(product1.getStock() - obj.getQuantity());

                    order.addOrderDetails(orderDetail);
                    this.productRepo.saveAndFlush(product1);
                } else {
                    throw new StockNotAvailableException(product.get().getName());
                }

            }else{
                throw new ProductNotFoundException();
            }

        });

        customer1.addOrder(order);
        this.orderRepo.save(order);

        customerRepo.saveAndFlush(customer1);

    }

    @Transactional
    @Modifying
    public void cancelOrder(CancelOrderRequest cancelOrderRequest) {

        Optional<Customer> customerOptional = this.customerRepo.getCustomerById(cancelOrderRequest.getCustomerId());

        if(customerOptional.isEmpty()){
            throw new CustomerNotFoundException();
        }

        Customer customer = customerOptional.get();
        Order order =customer.getOrder(cancelOrderRequest.getOrderId());



        order.getOrderDetails().forEach(od->{
            Product product= productRepo.getProductById(od.getProduct().getId()).get();
            product.setStock(product.getStock()+od.getQuantity());
            productRepo.saveAndFlush(product);
        });

        customer.getOrders().remove(order);

        customerRepo.saveAndFlush(customer);

    }

    @Transactional
    public void updateOrder(Order orderDTO){

        Optional<Order> order = this.orderRepo.getOrderByIdAndCustomerId(orderDTO.getId(),orderDTO.getCustomer().getId());

        if(order.isEmpty()){
            throw new OrderNotFoundException();
        }

        if(orderDTO.getOrderDate() !=null){

            order.get().setOrderDate(orderDTO.getOrderDate());
        }if(orderDTO.getOrderDetails() !=null){
            order.get().setOrderDetails(orderDTO.getOrderDetails());
        }

        this.orderRepo.saveAndFlush(orderDTO);


    }



}
