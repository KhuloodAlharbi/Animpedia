package com.example.animapedia;

public class AnimalModel {

    String name;
    String scientificName;
    String image;
    int id;

    public AnimalModel(String name, String scientificName, String image, int id) {
        this.name = name;
        this.scientificName = scientificName;
        this.image = image;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}
