package ro.myclass.onlineStoreapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.myclass.onlineStoreapi.models.Order;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

    @Query("select o from order o where o.customer.id = ?1")
    List<Order> getOrderByCustomerId(int customerId);
}
