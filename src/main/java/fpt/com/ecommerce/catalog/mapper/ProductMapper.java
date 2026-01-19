package fpt.com.ecommerce.catalog.mapper;

import fpt.com.ecommerce.catalog.dto.ProductDetailResponse;
import fpt.com.ecommerce.catalog.dto.ProductResponse;
import fpt.com.ecommerce.catalog.dto.ProductVariantResponse;
import fpt.com.ecommerce.catalog.entity.Product;
import fpt.com.ecommerce.catalog.entity.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

    // Detail
    ProductDetailResponse toResponseDetail(Product product);

    // Variant
    @Mapping(target = "stock", source = ".", qualifiedByName = "availableStock")
    ProductVariantResponse toResponseVariant(ProductVariant variant);

    @Named("availableStock")
    default Integer availableStock(ProductVariant variant) {
        return variant.getStock() - variant.getReservedStock();
    }
}
