package ro.myclass.onlineStoreapi.repo.services;

import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.myclass.onlineStoreapi.dto.*;
import ro.myclass.onlineStoreapi.exceptions.*;
import ro.myclass.onlineStoreapi.models.*;
import ro.myclass.onlineStoreapi.repo.*;
import ro.myclass.onlineStoreapi.services.CustomerService;

import java.time.LocalDate;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepo customerRepo;

    @Mock
    ProductRepo productRepo;

    @Mock
    OrderRepo orderRepo;

    @InjectMocks
    CustomerService customerService;


    @Test
    public void addCustomerOk(){
        CustomerDTO customerDTO = CustomerDTO.builder().fullName("Popescu Andrei").email("popescuandrei@gmail.com").password("andreiandrei2023").build();

        doReturn(Optional.empty()).when(customerRepo).getCustomerByEmail("popescuandrei@gmail.com");

        assertEquals(true,this.customerService.addCustomer(customerDTO));
    }

    @Test
    public void addCustomerException(){
        doReturn(Optional.of(new Customer())).when(customerRepo).getCustomerByEmail("");

        assertThrows(CustomerWasFoundException.class,()->{
            this.customerService.addCustomer(CustomerDTO.builder().email("").build());
        });
    }

    @Test
    public void removeCustomerOk(){
        Customer customer = Customer.builder().fullName("Rusu Cristian").email("rusucristian@gmail.com").password("cristicristian2023").build();
        customerRepo.save(customer);

        doReturn(Optional.of(customer)).when(customerRepo).getCustomerByEmail("rusucristian@gmail.com");

        assertEquals(true,this.customerService.removeCustomer("rusucristian@gmail.com"));
    }

    @Test
    public void removeCustomerException(){

        doReturn(Optional.empty()).when(customerRepo).getCustomerByEmail("");

        assertThrows(CustomerNotFoundException.class,()->{
            this.customerService.removeCustomer("");
        });
    }

    @Test
    public void returnCustomerByEmailOk(){
        Customer customer = Customer.builder().fullName("Popa Alexandru").email("popaalexandru@gmail.com").password("popapopa").id(1L).build();

        customerRepo.save(customer);
        doReturn(Optional.of(customer)).when(customerRepo).getCustomerByEmail("popaalexandru@gmail.com");

        assertEquals(customer,this.customerService.returnCustomerByEmail("popaalexandru@gmail.com"));

    }

    @Test
    public void returnCustomerByEmailException(){

        doReturn(Optional.empty()).when(customerRepo).getCustomerByEmail("");

        assertThrows(CustomerNotFoundException.class,()->{
            this.customerService.returnCustomerByEmail("");
        });
    }

    @Test
    public void getAllCustomersOk(){
        Customer customer = Customer.builder().fullName("Fabia Dascalu").email("dascalufabiana@gmail.com").password("tatianatatiana2023").build();
        Customer customer1 = Customer.builder().fullName("Lucia Lungu").email("lucia.lungu@gmail.com").password("lucialungu!2023").build();
        Customer customer2 = Customer.builder().fullName("Luiza Martin").email("martinluiza@gmail.com").password("tN*gE5MA^8~8Z)q").build();

        customerRepo.save(customer);
        customerRepo.save(customer1);
        customerRepo.save(customer2);
        List<Customer> list = new ArrayList<>();
        list.add(customer);
        list.add(customer1);
        list.add(customer2);

        doReturn(list).when(customerRepo).getAllCustomers();

        assertEquals(list,this.customerService.getAllCustomer());
    }

    @Test
    public void getAllCustomersException(){
        doReturn(new ArrayList<>()).when(customerRepo).getAllCustomers();

        assertThrows(ListEmptyException.class,()->{
            this.customerService.getAllCustomer();
        });
    }

    @Test
    public void addOrderOk(){
        Customer customer = Customer.builder().fullName("Victor Costache").email("victorcostache@gmail.com").password("victorvictor").id(1L).build();

        customerRepo.save(customer);

        Product product = Product.builder().id(1L).price(250).name("casti gaming").stock(500).build();

        ProductCardRequest cardRequest = ProductCardRequest.builder().productId(Math.toIntExact(product.getId())).quantity(2).build();

        List<ProductCardRequest> list = new ArrayList<>();
        list.add(cardRequest);

        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder().customerId(Math.toIntExact(customer.getId())).productCardRequests(list).build();

        doReturn(Optional.of(customer)).when(customerRepo).findById(1L);

        doReturn(Optional.of(product)).when(productRepo).getProductById(product.getId());
        assertEquals(true,this.customerService.addOrder(createOrderRequest));

    }

    @Test
    public void addOrderCustomerNotFoundException(){
        doReturn(Optional.empty()).when(customerRepo).findById(0L);

        assertThrows(CustomerNotFoundException.class,()->{
            this.customerService.addOrder(new CreateOrderRequest());
        });
    }

    @Test
    public void addOrderProductNotFoundException(){
        Customer customer = Customer.builder().id(1L).fullName("Popescu Alex").build();
        customerRepo.save(customer);

        doReturn(Optional.of(customer)).when(customerRepo).findById(1L);

        List<ProductCardRequest> list = new ArrayList<>();

        Product product = Product.builder().stock(500).id(1L).build();

        productRepo.save(product);
        list.add(ProductCardRequest.builder().productId(Math.toIntExact(product.getId())).quantity(0).build());

        doReturn(Optional.empty()).when(productRepo).getProductById(1L);

        assertThrows(ProductNotFoundException.class,()->{

            this.customerService.addOrder(CreateOrderRequest.builder().customerId(1).productCardRequests(list).build());

           });
    }

    @Test
    public void addOrderStockNotAvailableException(){
        Customer customer = Customer.builder().id(1L).fullName("Popescu Alex").build();
        customerRepo.save(customer);

        doReturn(Optional.of(customer)).when(customerRepo).findById(1L);

        List<ProductCardRequest> list = new ArrayList<>();

        Product product = Product.builder().stock(0).id(1L).build();

        productRepo.save(product);
        list.add(ProductCardRequest.builder().productId(Math.toIntExact(product.getId())).quantity(0).build());

        doReturn(Optional.of(product)).when(productRepo).getProductById(1L);

        assertThrows(StockNotAvailableException.class,()->{

            this.customerService.addOrder(CreateOrderRequest.builder().customerId(1).productCardRequests(list).build());

        });

    }

    @Test
    public void cancelOrderOk(){
        Customer customer = Customer.builder().fullName("Fiodor Stan").email("fiodorstan@gmail.com").password("fiodorstan@gmail.com").id(1L).build();

        customerRepo.save(customer);

        Order order = Order.builder().orderDate(LocalDate.now()).id(1L).build();

        customer.addOrder(order);

        Product product = Product.builder().name("tv samsung 90 inch").price(3000).id(1L).stock(700).build();

        OrderDetail orderDetail = OrderDetail.builder().id(1L).product(product).price(6000).quantity(2).build();

        order.addOrderDetails(orderDetail);

        CancelOrderRequest cancelOrderRequest = CancelOrderRequest.builder().orderId(Math.toIntExact(order.getId())).customerId(Math.toIntExact(customer.getId())).build();

        doReturn(Optional.of(customer)).when(customerRepo).getCustomerById(cancelOrderRequest.getCustomerId());

        doReturn(Optional.of(product)).when(productRepo).getProductById(1L);

        assertEquals(true,this.customerService.cancelOrder(cancelOrderRequest));
    }

    @Test
    public void cancelOrderException(){
        doReturn(Optional.empty()).when(customerRepo).getCustomerById(1L);

        CancelOrderRequest cancelOrderRequest = CancelOrderRequest.builder().orderId(1).customerId(1).build();

        assertThrows(CustomerNotFoundException.class,()->{
            this.customerService.cancelOrder(cancelOrderRequest);

        });

    }

    @Test
    public void updateQuantityOk(){

        Customer customer = Customer.builder().fullName("Petru Razvan").email("petrurazvan@gmail.com").password("petrurazvanpetru2023!").id(1L).build();

        customerRepo.save(customer);

        Product product = Product.builder().name("tv lg 40 inch").stock(654).price(2600).id(1L).build();

        productRepo.save(product);

        Order order = Order.builder().orderDate(LocalDate.now()).customer(customer).id(1L).build();

        OrderDetail orderDetail = OrderDetail.builder().order(order).quantity(2).product(product).price(5200).id(1L).build();

        order.addOrderDetails(orderDetail);
        customer.addOrder(order);

        ProductCardRequest productCardRequest = ProductCardRequest.builder().productId(1).quantity(4).build();


        UpdateOrderRequest orderRequest = UpdateOrderRequest.builder().orderId(Math.toIntExact(order.getId())).productCardRequest(productCardRequest).customerId(1).build();

        doReturn(Optional.of(customer)).when(customerRepo).findById((long) 1);
        doReturn(Optional.of(product)).when(productRepo).getProductById(product.getId());


        assertEquals(true,this.customerService.updateQuantityProduct(orderRequest));


    }

    @Test
    public void updateQuantityCustomerNotFoundException(){

        doReturn(Optional.empty()).when(customerRepo).findById(1L);

        assertThrows(CustomerNotFoundException.class,()->{
            this.customerService.updateQuantityProduct(UpdateOrderRequest.builder().customerId(1).build());
        });
    }

    @Test
    public void updateQuantityProductException(){
        Customer customer = Customer.builder().id(1L).fullName("Adrian").build();

        UpdateOrderRequest updateOrderRequest = UpdateOrderRequest.builder().customerId(1).orderId(1).productCardRequest(ProductCardRequest.builder().productId(1).quantity(1).build()).build();
        customerRepo.save(customer);

        doReturn(Optional.of(customer)).when(customerRepo).findById((long) 1);
        doReturn(Optional.empty()).when(productRepo).getProductById(updateOrderRequest.getProductCardRequest().getProductId());

        assertThrows(ProductNotFoundException.class,()->{
            this.customerService.updateQuantityProduct(updateOrderRequest);
        });

    }

    @Test
    public void updateQuantityStockNotAvailable(){
        Customer customer = Customer.builder().fullName("Bogdan").id(1L).email("bogdanbogdan@gmail.com").password("20232023").build();

        Product product = Product.builder().id(1L).name("masina de spalat").stock(2).price(1000).build();

        productRepo.save(product);

        Order order = Order.builder().orderDate(LocalDate.now()).id(1L).build();

        customer.addOrder(order);

        OrderDetail orderDetail = OrderDetail.builder().id(1L).order(order).quantity(1).product(product).build();

        order.addOrderDetails(orderDetail);

        customerRepo.save(customer);

        UpdateOrderRequest updateOrderRequest = UpdateOrderRequest.builder().orderId(1).productCardRequest(ProductCardRequest.builder().productId(1).quantity(300).build()).build();

        doReturn(Optional.of(customer)).when(customerRepo).findById((long)updateOrderRequest.getCustomerId());
        doReturn(Optional.of(product)).when(productRepo).getProductById(updateOrderRequest.getProductCardRequest().getProductId());

         assertThrows(StockNotAvailableException.class,()->{

            this.customerService.updateQuantityProduct(updateOrderRequest);

        });
    }

    @Test
    public void returnAllOrderDetailByOrderIdOk(){

        Customer customer = Customer.builder().id(1L).fullName("Moldovan Radu").email("moldovanradu@gmail.com").password("moldomoldovan2023").build();

        customerRepo.save(customer);

        Order order = Order.builder().orderDate(LocalDate.of(2023,3,4)).id(1L).build();


        customer.addOrder(order);

        Product product = Product.builder().stock(500).price(3000).id(1L).name("tv lg").build();
        Product product1 = Product.builder().stock(700).price(300).id(2L).name("mousepad").build();

        OrderDetail orderDetail = OrderDetail.builder().order(order).quantity(2).price(600).product(product1).build();
        OrderDetail orderDetail1 = OrderDetail.builder().order(order).quantity(1).price(300).product(product).build();

        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail);
        orderDetails.add(orderDetail1);

        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        order.addOrderDetails(orderDetail);
        order.addOrderDetails(orderDetail1);
        doReturn(orderList).when(orderRepo).getOrderByCustomerId(customer.getId());

        assertEquals(orderDetails,this.customerService.returnAllOrdersDetailbyOrderId(customer.getId()));
    }

    @Test
    public void returnAllOrderDetailByOrderIdException(){

        doReturn(new ArrayList<>()).when(orderRepo).getOrderByCustomerId(1L);

        assertThrows(ListEmptyException.class,()->{
            this.customerService.returnAllOrdersDetailbyOrderId(1L);
        });
    }




}