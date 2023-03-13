package ro.myclass.onlineStoreapi.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.myclass.onlineStoreapi.models.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    @Query("select p from Product p where p.name = ?1")
    Optional<Product> getProductByName(String name);



    @Query("select p from Product p ")
    List<Product> getAllProducts();

    @Query("select p from Product p where p.id = ?1")
    Optional<Product> getProductById(long id);

    @Query("select p from Product  p where p.id in (select od.product.id from OrderDetail  od where  od.order.id = ?1)")
    List<Product> getAllProductsFromOrder(long orderDetailId);

}
