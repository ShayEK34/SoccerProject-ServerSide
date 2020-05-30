package Domain.AssociationManagement.unit;

import Domain.AssociationManagement.PointsPolicy;
import org.junit.Assert;
import org.junit.Test;

public class PointsPolicyTest {

    @Test
    public void testPP(){

        int pointsForWin = 3;
        int pointsForDraw = 1;
        int pointsForLoss = 1;
        boolean goalDiffTieBreaker = true;
        boolean directResultsTieBreaker = false;

        PointsPolicy pp = new PointsPolicy(pointsForWin, pointsForDraw, pointsForLoss, goalDiffTieBreaker, directResultsTieBreaker);

        int win = pp.getPointsForWin();
        Assert.assertEquals(win , pp.getPointsForWin());
        pp.setPointsForWin(4);
        Assert.assertNotSame(win, pp.getPointsForWin());

        int draw = pp.getPointsForDraw();
        Assert.assertEquals(draw , pp.getPointsForDraw());
        pp.setPointsForDraw(2);
        Assert.assertNotSame(draw, pp.getPointsForDraw());

        int loss = pp.getPointsForLoss();
        Assert.assertEquals(loss , pp.getPointsForLoss());
        pp.setPointsForLoss(2);
        Assert.assertNotSame(loss, pp.getPointsForLoss());

        boolean goalDiff = pp.isGoalDiffTieBreaker();
        Assert.assertTrue(goalDiff);
        pp.setGoalDiffTieBreaker(false);
        Assert.assertFalse(pp.isGoalDiffTieBreaker());

        boolean dirRes = pp.isDirectResultsTieBreaker();
        Assert.assertFalse(dirRes);
        pp.setDirectResultsTieBreaker(true);
        Assert.assertTrue(pp.isDirectResultsTieBreaker());
    }


}

