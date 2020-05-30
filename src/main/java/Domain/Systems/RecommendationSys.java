package Domain.Systems;

/**
 * The class represent the Recommendation system.
 */
public class RecommendationSys {
    private boolean active;

    /**
     * constructor.
     */
    public RecommendationSys() {
        this.active = false;
    }

    /**
     * return the status of the Recommendation system.
     * @return
     */
    public boolean isActive() {
        return active;
    }
    /**
     * turn on the Recommendation system
     */
    public void activate() {
        this.active=true;
    }
}
