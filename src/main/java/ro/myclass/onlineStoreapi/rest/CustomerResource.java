package ro.myclass.onlineStoreapi.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderResponse;
import ro.myclass.onlineStoreapi.dto.CustomerDTO;
import ro.myclass.onlineStoreapi.dto.ProductCardRequest;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.CustomerService;
import ro.myclass.onlineStoreapi.services.ProductService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( value = "/api/v1/customer")
public class CustomerResource {
    private CustomerService customerService;

    private ProductService productService;

    public CustomerResource(CustomerService customerService,ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

 @PostMapping("/addCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customer){
        this.customerService.addCustomer(customer);

        return new ResponseEntity<>("Customer was added!", HttpStatus.ACCEPTED);
 }

 @DeleteMapping("/deleteCustomer/{email}")
    public ResponseEntity<String> removeCustomer(@PathVariable String email){
        this.customerService.removeCustomer(email);

        return new ResponseEntity<>("Customer was deleted!",HttpStatus.OK);
 }

 @GetMapping("/getCustomerByEmail/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email){
        Customer m = this.customerService.returnCustomerByEmail(email);

        return new ResponseEntity<>(m,HttpStatus.FOUND);
 }

 @GetMapping("/getAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customerList = this.customerService.getAllCustomer();

        return new ResponseEntity<>(customerList,HttpStatus.OK);
 }

    @PostMapping(path = "/addOrder/{id}")
    public ResponseEntity<CreateOrderResponse> addOrder(@PathVariable int id,@Valid @RequestBody List<ProductCardRequest> productCardRequest) throws JsonProcessingException {



     CreateOrderRequest createOrderRequest = CreateOrderRequest.builder().productCardRequests(productCardRequest)
             .customerId(id)
             .build();



     CreateOrderResponse createOrderResponse = CreateOrderResponse.builder().message("adaugat cu succes!")
             .build();

     this.customerService.addOrder(createOrderRequest);

     return ResponseEntity.ok(createOrderResponse);

 }


    @DeleteMapping(path = "/deleteOrder/{customerId}",consumes = MediaType.ALL_VALUE, produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<CreateOrderResponse> deleteOrder(@PathVariable int customerId,@RequestParam int productId){

        this.customerService.removeOrder((long )customerId,(long)productId);

        CreateOrderResponse createOrderResponse = CreateOrderResponse.builder().message("sters cu succes!")
                .build();

        return new ResponseEntity<>(createOrderResponse, HttpStatus.CREATED);
    }

   @GetMapping("/updateQuantityProduct/{customerId}")
   public ResponseEntity<CreateOrderResponse> updateQuantity(@PathVariable int customerId,@RequestParam int quantity,@RequestParam int productID){
     this.customerService.updateQuantityProduct(customerId,quantity,productID);

     return new ResponseEntity<>(new CreateOrderResponse("cantitatea  a fost actualizata!"),HttpStatus.OK);
    }

    @GetMapping("/getOrderDetails/{customerId}")
        public ResponseEntity<List<OrderDetail>> getOrderDetails ( @PathVariable int customerId){
            List<OrderDetail> orderDetails = this.customerService.returnAllOrdersDetailbyOrderId(customerId);

            return new ResponseEntity<>(orderDetails,HttpStatus.OK);
        }
}


//@PostMapping("/{customerId}")
//public ResponseEntity<CreateOrderResponse> addOrder(@PathVariable int customerId, ArrayList<ProductCardRequest> productCardRequests){
//
//        Customer customer = this.customerService.getCustomerbyId(customerId);
//        CreateOrderRequest createOrderRequest = new CreateOrderRequest((int)customerId,productCardRequests);
//
//        this.customerService.addOrder(createOrderRequest);
//    return new ResponseEntity<>(new CreateOrderResponse("adaugat cu succes!"),HttpStatus.OK);
//
//    }
//}

//todo:check error with list ?????


