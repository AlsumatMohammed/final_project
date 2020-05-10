package com.example.final_project;

public class generalAds {



    String title;
    String category;
    String condition;
    String warranty;
    String description;
    String price;
    String priceType;
    String currency;
    String publishDate;
    String productImage;

    String key;

    String publisherImage;
    String publisherUsername;
    String publisherEmail;
    String publisherPhoneNumber;
    String publisherLatitude;

    public String getPublisherLatitude() {
        return publisherLatitude;
    }

    public void setPublisherLatitude(String publisherLatitude) {
        this.publisherLatitude = publisherLatitude;
    }

    public String getPublisherLongitude() {
        return publisherLongitude;
    }

    public void setPublisherLongitude(String publisherLongitude) {
        this.publisherLongitude = publisherLongitude;
    }

    String publisherLongitude;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

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

    public String getPublisherPhoneNumber() {
        return publisherPhoneNumber;
    }

    public void setPublisherPhoneNumber(String publisherPhoneNumber) {
        this.publisherPhoneNumber = publisherPhoneNumber;
    }



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
