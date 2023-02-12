package ro.myclass.onlineStoreapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.myclass.onlineStoreapi.models.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {

    @Query("select c from Customer c where c.email = ?1")
    Optional<Customer> getCustomerByEmail(String email);

    @Query("select c from Customer c")
    List<Customer> getAllCustomers();
}
