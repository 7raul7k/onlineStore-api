package ro.myclass.onlineStoreapi.services;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.dto.CustomerDTO;
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
            return this.customerRepo.getAllCustomers();
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
                    throw new StockNotAvailableException(product.get().getName());
                }

            }else{
                throw new ProductNotFoundException(obj.getProductId());
            }


        });

        customer1.addOrder(order);

        customerRepo.saveAndFlush(customer1);


    }

    public Customer getCustomerbyId(long id) {
        return this.customerRepo.getCustomerById(id).get();
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


       //todo:logica de update al stocului
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
        public void updateQuantityProduct ( int customerId, int newQuantity, int productId){

            Optional<Customer> customer1 = this.customerRepo.findById((long)customerId);

            if(customer1.isEmpty()){
                throw new CustomerNotFoundException();
            }

            Optional<Product> product = this.productRepo.getProductById((long) productId);

            if(product.isEmpty()){
                throw new ProductNotFoundException(productId);
            }

            List<Order> orders = this.orderRepo.getOrderByCustomerId(customerId);

            for(Order m : orders){

                Optional<OrderDetail> orderDetail = this.orderDetailRepo.findOrderDetailByProductIdAndOrderId(productId,m.getId());

                if(orderDetail.isEmpty()==false){
                    if(newQuantity > orderDetail.get().getQuantity()){
                        int q = newQuantity - orderDetail.get().getQuantity();
                        orderDetail.get().setQuantity(q);
                        product.get().setStock(product.get().getStock() - q);

                        double price = product.get().getPrice() * newQuantity;

                        orderDetail.get().setPrice(price);


                        productRepo.saveAndFlush(product.get());
                    }
                }

            }



        }

        public List<OrderDetail> orderDetails (int customerId) {
            Optional<Customer> customer = this.customerRepo.findById((long) customerId);

            if (customer.isEmpty()) {
                throw new CustomerNotFoundException();
            }
            List<Order> order = this.orderRepo.getOrderByCustomerId(customerId);

            List<OrderDetail> orderDetails = new ArrayList<>();

            for(Order m : order){
                List<OrderDetail> list = this.returnAllOrdersDetailbyOrderId((long) m.getId());

                orderDetails.addAll(list);
            }

            return orderDetails;

        }

        public List<OrderDetail> returnAllOrdersDetailbyOrderId(long customerId){

       List<Order> orders = this.orderRepo.getOrderByCustomerId(customerId);

       List<OrderDetail> orderDetails = new ArrayList<>();
       for(Order m : orders){
          List<OrderDetail> list = m.getOrderDetails();

          orderDetails.addAll(list);

       }

       return orderDetails;
        }




}
