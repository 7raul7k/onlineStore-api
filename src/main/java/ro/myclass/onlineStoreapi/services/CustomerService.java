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
    public  boolean addCustomer(CustomerDTO customer) {
        Optional<Customer> optionalCustomer = this.customerRepo.getCustomerByEmail(customer.getEmail());

        if (optionalCustomer.isEmpty()) {
                Customer m = new Customer().builder()
                        .email(customer.getEmail())
                        .password(customer.getPassword())
                        .fullName(customer.getFullName())
                        .build();
                return true;
        }else{
            throw new CustomerWasFoundException();
        }
    }

    public boolean removeCustomer(String email) {
        Optional<Customer> customer = this.customerRepo.getCustomerByEmail(email);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        } else {
            this.customerRepo.delete(customer.get());
            return true;
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
    public boolean addOrder(CreateOrderRequest createOrderRequest) {


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
                throw new ProductNotFoundException();
            }


        });

        customer1.addOrder(order);

        customerRepo.saveAndFlush(customer1);
        return true;


    }
    @Transactional
    @Modifying
    public boolean cancelOrder(CancelOrderRequest cancelOrderRequest) {

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
        return true;

    }




        @Transactional
        @Modifying
        public boolean updateQuantityProduct (UpdateOrderRequest updateOrderRequest){

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

            });

            return true;

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




}
