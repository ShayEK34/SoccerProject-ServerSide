package Domain.AssociationManagement;

public class PointsPolicy {

    private int pointsForWin;
    private int pointsForDraw;
    private int pointsForLoss;
    private boolean goalDiffTieBreaker;
    private boolean directResultsTieBreaker;

    public PointsPolicy(int pointsForWin, int pointsForDraw, int pointsForLoss, boolean goalDiffTieBreaker, boolean directResultsTieBreaker) {
        this.pointsForWin = pointsForWin;
        this.pointsForDraw = pointsForDraw;
        this.pointsForLoss = pointsForLoss;
        this.goalDiffTieBreaker = goalDiffTieBreaker;
        this.directResultsTieBreaker = directResultsTieBreaker;
    }

    public int getPointsForWin() {
        return pointsForWin;
    }

    public void setPointsForWin(int pointsForWin) {
        this.pointsForWin = pointsForWin;
    }

    public int getPointsForDraw() {
        return pointsForDraw;
    }

    public void setPointsForDraw(int pointsForDraw) {
        this.pointsForDraw = pointsForDraw;
    }

    public int getPointsForLoss() {
        return pointsForLoss;
    }

    public void setPointsForLoss(int pointsForLoss) {
        this.pointsForLoss = pointsForLoss;
    }

    public boolean isGoalDiffTieBreaker() {
        return goalDiffTieBreaker;
    }

    public void setGoalDiffTieBreaker(boolean goalDiffTieBreaker) {
        this.goalDiffTieBreaker = goalDiffTieBreaker;
    }

    public boolean isDirectResultsTieBreaker() {
        return directResultsTieBreaker;
    }

    public void setDirectResultsTieBreaker(boolean directResultsTieBreaker) {
        this.directResultsTieBreaker = directResultsTieBreaker;
    }
}

