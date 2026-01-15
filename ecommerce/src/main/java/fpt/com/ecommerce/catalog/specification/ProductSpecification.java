package fpt.com.ecommerce.catalog.specification;

import fpt.com.ecommerce.catalog.entity.Product;
import fpt.com.ecommerce.catalog.entity.ProductVariant;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> hasCategory(String category) {
        return (root, query, cb) ->
                category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Product> nameContains(String keyword) {
        return (root, query, cb) ->
                keyword == null ? null : cb.like(root.get("name"), "%" + keyword + "%");
    }

    public static Specification<Product> filterByPriceRange(
            Integer minPrice,
            Integer maxPrice
    ) {
        return (root, query, cb) -> {

            Join<Product, ProductVariant> variantJoin =
                    root.join("variants", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            if (minPrice != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                variantJoin.get("price"),
                                minPrice
                        )
                );
            }

            if (maxPrice != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                variantJoin.get("price"),
                                maxPrice
                        )
                );
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
