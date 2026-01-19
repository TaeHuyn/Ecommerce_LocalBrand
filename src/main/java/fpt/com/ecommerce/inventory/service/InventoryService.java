package fpt.com.ecommerce.inventory.service;

public interface InventoryService {

    void reserve(Long variantId, int quantity);

    void release(Long variantId, int quantity);

    void confirm(Long variantId, int quantity);
}
