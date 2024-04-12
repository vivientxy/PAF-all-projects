package paf.day3.model;

public class Game {
    
    private String name;
    private Integer numOfReviews;
    private Double avgRating;
    
    public Game() {
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getNumOfReviews() {
        return numOfReviews;
    }
    public void setNumOfReviews(Integer numOfReviews) {
        this.numOfReviews = numOfReviews;
    }
    public Double getAvgRating() {
        return avgRating;
    }
    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
}
