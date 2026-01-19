package fpt.com.ecommerce.inventory.service.impl;

import fpt.com.ecommerce.catalog.repository.ProductVariantRepository;
import fpt.com.ecommerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class InventoryServiceImpl implements InventoryService {

    ProductVariantRepository repo;

    @Override
    public void reserve(Long variantId, int quantity) {

        int updated = repo.reserveStock(variantId, quantity);

        if (updated == 0) {
            throw new RuntimeException("Out of stock");
        }
    }

    @Override
    public void release(Long variantId, int quantity) {
        repo.releaseStock(variantId, quantity);
    }

    @Override
    public void confirm(Long variantId, int quantity) {
        repo.confirmStock(variantId, quantity);
    }
}
