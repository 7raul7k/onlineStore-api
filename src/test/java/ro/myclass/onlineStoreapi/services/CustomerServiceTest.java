package ro.myclass.onlineStoreapi.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.myclass.onlineStoreapi.dto.CustomerDTO;
import ro.myclass.onlineStoreapi.dto.ProductCardRequest;
import ro.myclass.onlineStoreapi.dto.UpdateOrderRequest;
import ro.myclass.onlineStoreapi.exceptions.CustomerNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.CustomerWasFoundException;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.exceptions.ProductNotFoundException;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.OrderRepo;
import ro.myclass.onlineStoreapi.repo.ProductRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


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

    @Captor
    ArgumentCaptor<Customer> argumentCaptor;


    @Test
    public void addCustomerOk(){
        CustomerDTO customer = CustomerDTO.builder().fullName("Popa Alexandru").email("popaalexandru@gmail.com").password("popaalexandru2023").build();

        Customer m = Customer.builder().email(customer.getEmail())
                .password(customer.getPassword())
                .fullName(customer.getFullName())
                .build();
        doReturn(Optional.empty()).when(customerRepo).getCustomerByEmail("popaalexandru@gmail.com");

        customerService.addCustomer(customer);
        verify(customerRepo,times(1)).save(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(),m);
    }

    @Test
    public void addCustomerException(){
        doReturn(Optional.of(new Customer())).when(customerRepo).getCustomerByEmail("");

        assertThrows(CustomerWasFoundException.class,()->{
            this.customerService.addCustomer(CustomerDTO.builder().email("").build());
        });
    }


   //    todo:de refacut removeCustomerOk

    @Test
    public void removeCustomerOk(){
        Customer customer = Customer.builder().id(1L).fullName("Stanciu Marian").email("stanciumarian@gmail.com").password("stanciumarian2023").build();


        doReturn(Optional.of(customer)).when(customerRepo).getCustomerByEmail(customer.getEmail());

        customerService.removeCustomer(customer.getEmail());

        verify(customerRepo,times(1)).delete(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue(),customer);
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
    public void updateQuantityOk(){
        Customer customer = Customer.builder().id(1L).fullName("Popa Iulian").email("popaiulian@gmail.com").password("popaiulian2023").build();


        Product product = Product.builder().id(1L).name("Apple watch 210").stock(200).price(800).build();



        Order order = Order.builder().id(1L).customer(customer).orderDate(LocalDate.now()).build();

        OrderDetail orderDetail = OrderDetail.builder().id(1L).order(order).quantity(1).price(800).product(product).build();

        order.addOrderDetails(orderDetail);
        customer.addOrder(order);

        doReturn(Optional.of(customer)).when(customerRepo).findById(1L);
        doReturn(Optional.of(product)).when(productRepo).getProductById(1L);

       ProductCardRequest productCardRequest = ProductCardRequest.builder().productId(1).quantity(1).build();

       UpdateOrderRequest updateOrderRequest = UpdateOrderRequest.builder().orderId(1).productCardRequest(productCardRequest).customerId(1).build();

       this.customerService.updateQuantityProduct(updateOrderRequest);

       verify(customerRepo,times(1)).saveAndFlush(argumentCaptor.capture());

       assertEquals(argumentCaptor.getValue(),customer);

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


        doReturn(Optional.of(customer)).when(customerRepo).findById((long) 1);
        doReturn(Optional.empty()).when(productRepo).getProductById(updateOrderRequest.getProductCardRequest().getProductId());

        assertThrows(ProductNotFoundException.class,()->{
            this.customerService.updateQuantityProduct(updateOrderRequest);
        });

    }



    @Test
    public void returnAllOrderDetailByOrderIdOk(){

        Customer customer = Customer.builder().id(1L).fullName("Moldovan Radu").email("moldovanradu@gmail.com").password("moldomoldovan2023").build();



        Order order = Order.builder().orderDate(LocalDate.of(2023,3,4)).id(1L).build();




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
    public void getAllOrderDetailByOrderIDError(){
        doReturn(new ArrayList<>()).when(orderRepo).getOrderByCustomerId(1);

        assertThrows(ListEmptyException.class,()->{
            this.customerService.returnAllOrdersDetailbyOrderId(1L);
        });
    }

    @Test
    public void getCustomerByEmail(){
        Customer customer = Customer.builder().id(1L).email("andreivlad@gmail.com").fullName("Andrei Vlad").password("andreiandrei").build();


        doReturn(Optional.of(customer)).when(customerRepo).getCustomerByEmail("andreivlad@gmail.com");

        assertEquals(customer,this.customerService.getCustomerByEmail("andreivlad@gmail.com"));
    }

    @Test
    public void getCustomerByEmailError(){
        doReturn(Optional.empty()).when(customerRepo).getCustomerByEmail("test");

       assertThrows(CustomerNotFoundException.class, ()->{
           this.customerService.getCustomerByEmail("test");
       });
    }

    @Test
    public void returnAllOrderDetailsByCustomerIDOk(){
        Customer customer = Customer.builder().id(1L)
                .fullName("Andrei Popescu")
                .password("28138iudsya")
                .email("andreipopescu@gmail.com")
                .build();

        doReturn(Optional.of(customer)).when(customerRepo).getCustomerById(1L);

        List<OrderDetail> orderDetails =  new ArrayList<>();

        assertEquals(orderDetails,this.customerService.returnAllOrderDetailsByCustomerID(1));
    }

    @Test
    public void returnAllOrderDetailsByCustomerIDError(){
        doReturn(Optional.empty()).when(customerRepo).getCustomerById(1);

        assertThrows(CustomerNotFoundException.class,()->{
            customerService.returnAllOrderDetailsByCustomerID(1);
        });
    }

    @Test
    public void sortOrderByLocalDateOk(){
        Customer customer = Customer.builder().id(1L)
                .fullName("Andrei Popescu")
                .password("28138iudsya")
                .email("andreipopescu@gmail.com")
                .orders(new ArrayList<>())
                .build();

        doReturn(Optional.of(customer)).when(customerRepo).getCustomerById(1L);


        List<OrderDetail> orderDetails = new ArrayList<>();

        assertEquals(orderDetails,this.customerService.returnAllOrderDetailsByCustomerID(1));
    }

    @Test
    public void sortOrderByLocalDateCustomerError(){
        doReturn(Optional.empty()).when(customerRepo).getCustomerById(1);

        assertThrows(CustomerNotFoundException.class,()->{
            this.customerService.sortOrderByLocalDate(1);
        });
    }

    @Test
    public void sortOrderByLocalDateListError(){
        doReturn(Optional.of(new Customer())).when(customerRepo).getCustomerById(1);

        doReturn(new ArrayList<>()).when(orderRepo).sortOrderListByDate(1);

        assertThrows(ListEmptyException.class,()->{
            this.customerService.sortOrderByLocalDate(1);
        });
    }





}