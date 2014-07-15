package com.sealion.serviceassistant.entity;

/**
 * 问题类型
 */
public class ProductEntity
{
	private int id;
	private int productId;
	private String productName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                '}';
    }
}
