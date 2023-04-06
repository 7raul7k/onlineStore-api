package ro.myclass.onlineStoreapi.services;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.dto.CustomerDTO;
import ro.myclass.onlineStoreapi.dto.UpdateOrderRequest;
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
    @Transactional
    public  void addCustomer(CustomerDTO customer) {
        Optional<Customer> optionalCustomer = this.customerRepo.getCustomerByEmail(customer.getEmail());

        if (optionalCustomer.isEmpty()) {
                Customer m = new Customer(customer.getEmail(), customer.getPassword(), customer.getFullName());
                customerRepo.save(m);
        }else{
            throw new CustomerWasFoundException();
        }
    }

    public Customer getCustomerByEmail(String email){
        Optional<Customer> customer = this.customerRepo.getCustomerByEmail(email);

        if(customer.isEmpty()){
            throw new CustomerNotFoundException();
        }else{
            return customer.get();
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

    public Customer returnCustomerByEmail(String email) {
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
            return customers;
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

            //ce:exista prodosul cantiatea disponibila update cantitate
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
        @Modifying
        public void updateQuantityProduct (UpdateOrderRequest updateOrderRequest){

            Optional<Customer> customer1 = this.customerRepo.findById((long)updateOrderRequest.getCustomerId());

            if(customer1.isEmpty()){
                throw new CustomerNotFoundException();
            }

           Optional<Product> productOptional = this.productRepo.getProductById(updateOrderRequest.getProductCardRequest().getProductId());

            if(productOptional.isEmpty()){
                throw new ProductNotFoundException();
            }
            Customer customer = customer1.get();
            Order order = customer.getOrder(updateOrderRequest.getOrderId());

            List<OrderDetail> orderDetailList = order.getOrderDetails();

            Product product = productOptional.get();
            orderDetailList.stream().forEach(k ->{
                int productStock= product.getStock();
                if(k.getProduct().getId() == updateOrderRequest.getProductCardRequest().getProductId()  && productStock > updateOrderRequest.getProductCardRequest().getQuantity() ){
                if(product.getStock() > updateOrderRequest.getProductCardRequest().getQuantity() ){

                 int stock = updateOrderRequest.getProductCardRequest().getQuantity() - k.getQuantity();

                    product.setStock(product.getStock()- stock);

                    k.setQuantity(updateOrderRequest.getProductCardRequest().getQuantity());

                }else if(product.getStock() > updateOrderRequest.getProductCardRequest().getQuantity() ) {


                    int stock = k.getQuantity() - updateOrderRequest.getProductCardRequest().getQuantity();

                    product.setStock(product.getStock() + stock);

                    k.setQuantity(stock);

                    orderDetailRepo.saveAndFlush(k);
                }


                }else if (k.getProduct().getId() == updateOrderRequest.getProductCardRequest().getProductId() && productStock < updateOrderRequest.getProductCardRequest().getQuantity()){
                    throw new StockNotAvailableException(product.getName());
                }

                productRepo.saveAndFlush(product);
            });

            customerRepo.saveAndFlush(customer);



        }

        public List<OrderDetail> returnAllOrdersDetailbyOrderId(long customerId){

       List<Order> orders = this.orderRepo.getOrderByCustomerId(customerId);

       if(orders.isEmpty()){
           throw new ListEmptyException();
       }

       List<OrderDetail> orderDetails = new ArrayList<>();
       for(Order m : orders){
          List<OrderDetail> list = m.getOrderDetails();

          list.stream().forEach((k)->{
              orderDetails.add(k);
          });

       }

       return orderDetails;

        }

        public List<OrderDetail> returnAllOrderDetailsByCustomerID(int customerId){
        Optional<Customer> customerOptional = this.customerRepo.getCustomerById((long) customerId);

        if(customerOptional.isEmpty()){
            throw new CustomerNotFoundException();
        }

        Customer customer = customerOptional.get();
        List<Order> orders = customer.getOrders();

        List<OrderDetail> orderDetailList = new ArrayList<>();
        orders.stream().forEach((order -> {

            orderDetailList.addAll(order.getOrderDetails());

        }));


        return orderDetailList;

        }

        public List<OrderDetail> sortOrderByLocalDate(int customerID){

        Optional<Customer> customer = this.customerRepo.getCustomerById(customerID);

        if(customer.isEmpty()){

            throw new CustomerNotFoundException();
        }

        List<Order> orders = this.orderRepo.sortOrderListByDate((long) customerID);

        if(orders.isEmpty()){
            throw new ListEmptyException();
        }

        List<OrderDetail> orderDetails = new ArrayList<>();
        orders.stream().forEach((k)->{

            orderDetails.addAll(k.getOrderDetails());

        });

        return orderDetails;
        }



}
