package ua.skillsup.gelius.converters;

import ua.skillsup.gelius.dao.entities.Product;
import ua.skillsup.gelius.model.ProductDto;

public final class EntityDtoConverter {

    private EntityDtoConverter() {

    }
    public static Product convert(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productDto.getId());
        product.setClients(productDto.getClients());
        product.setProductsName(productDto.getProductsName());
        product.setProductsType(productDto.getProductsType());
        product.setInnerLength(productDto.getInnerLength());
        product.setInnerWidth(productDto.getInnerWidth());
        product.setInnerHeight(productDto.getInnerHeight());
        product.setInnerHeight(productDto.getInnerHeight());
        product.setGrade(productDto.getGrade());
        product.setProfile(productDto.getProfile());
        product.setColour(productDto.getColour());
        product.setActivity(productDto.getActivity());
        return product;
    }
    public static ProductDto convert(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setClients(product.getClients());
        productDto.setProductsName(product.getProductsName());
        productDto.setProductsType(product.getProductsType());
        productDto.setInnerLength(product.getInnerLength());
        productDto.setInnerWidth(product.getInnerWidth());
        productDto.setInnerHeight(product.getInnerHeight());
        productDto.setInnerHeight(product.getInnerHeight());
        productDto.setGrade(product.getGrade());
        productDto.setProfile(product.getProfile());
        productDto.setColour(product.getColour());
        productDto.setActivity(product.getActivity());
        return productDto;
    }

}