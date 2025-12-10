package com.example.animapedia;

public class ProductModel {

    private String id;          // <-- Firebase key (important!)
    private String name;
    private String price;
    private String description;
    private String imageUrl;

    // Empty constructor required for Firebase
    public ProductModel() {}

    // Full constructor
    public ProductModel(String id, String name, String price, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // GETTERS
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }

    // SETTERS
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(String price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
