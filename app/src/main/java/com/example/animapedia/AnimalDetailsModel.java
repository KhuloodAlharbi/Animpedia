package com.example.animapedia;

public class AnimalDetailsModel {

    String name;
    String scientificName;
    String imageUrl;

    String kingdom;
    String phylum;
    String animalClass;
    String order;
    String family;
    String genus;
    String description;

    public AnimalDetailsModel(String name, String scientificName, String imageUrl,
                              String kingdom, String phylum, String animalClass,
                              String order, String family, String genus,
                              String description) {

        this.name = name;
        this.scientificName = scientificName;
        this.imageUrl = imageUrl;
        this.kingdom = kingdom;
        this.phylum = phylum;
        this.animalClass = animalClass;
        this.order = order;
        this.family = family;
        this.genus = genus;
        this.description = description;
    }

    public String getName() { return name; }
    public String getScientificName() { return scientificName; }
    public String getImageUrl() { return imageUrl; }
    public String getKingdom() { return kingdom; }
    public String getPhylum() { return phylum; }
    public String getAnimalClass() { return animalClass; }
    public String getOrder() { return order; }
    public String getFamily() { return family; }
    public String getGenus() { return genus; }
    public String getDescription() { return description; }
}
