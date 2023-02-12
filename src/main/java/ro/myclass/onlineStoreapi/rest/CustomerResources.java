package ro.myclass.onlineStoreapi.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.services.CustomerService;

@RestController
@RequestMapping("/api/v1")
public class CustomerResources {
    private CustomerService customerService;

    public CustomerResources(CustomerService customerService) {
        this.customerService = customerService;
    }

 @PostMapping("/addCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer){
        this.customerService.addCustomer(customer);

        return new ResponseEntity<>("Customer was added!", HttpStatus.ACCEPTED);
 }
}
