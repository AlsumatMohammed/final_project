package com.example.final_project;

public class generalAds {

    String title;
    String publisherImage;
    String publisherUsername;
    String publisherEmail;

    public String getPublisherUsername() {
        return publisherUsername;
    }

    public void setPublisherUsername(String publisherUsername) {
        this.publisherUsername = publisherUsername;
    }

    public String getPublisherEmail() {
        return publisherEmail;
    }

    public void setPublisherEmail(String publisherEmail) {
        this.publisherEmail = publisherEmail;
    }

    public String getPublisherphoneNumber() {
        return publisherphoneNumber;
    }

    public void setPublisherphoneNumber(String publisherphoneNumber) {
        this.publisherphoneNumber = publisherphoneNumber;
    }

    String publisherphoneNumber;
    String productImage;
    String price;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public generalAds(String title, String publisherImage, String productImage, String price) {
        this.title = title;
        this.publisherImage = publisherImage;
        this.productImage = productImage;
        this.price = price;
    }

    public generalAds() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisherImage() {
        return publisherImage;
    }

    public void setPublisherImage(String publisherImage) {
        this.publisherImage = publisherImage;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
