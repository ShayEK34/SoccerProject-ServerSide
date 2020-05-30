package Domain.ClubManagement.unit;

import Domain.ClubManagement.Court;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


public class CourtTest {
    private Court testCourt;


    @Before
    public void initCourt(){
        testCourt = new Court ("Camp Nou", "Barcelona", 99000);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCourtConstructorNullName(){
        Court court1 = new Court(null,"Manchester",20000);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCourtConstructorNullCity(){
        Court court2 = new Court("San Siro",null,-20000);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCourtConstructorNegativeCapacity(){
        Court court3 = new Court("Olympiastadion ","Berlin",-20000);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCourtConstructorZeroCapacity(){
        Court court4 = new Court("Olympiastadion ","Berlin",0);
    }

    @Test
    public void testCourtGetName(){
        Assert.assertEquals(this.testCourt.getCourtName(),"Camp Nou");
    }

    @Test
    public void testCourtGetCity(){
        Assert.assertEquals(this.testCourt.getCourtCityLocation(),"Barcelona");
    }

    @Test
    public void testCourtGetCapacity(){
        Assert.assertEquals(this.testCourt.getCourtCapacity(),99000);
    }

    @Test
    public void testSetCapacity(){
        this.testCourt.setCourtCapacity(100000);
        Assert.assertEquals(this.testCourt.getCourtCapacity(),100000);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetCapacityNegative(){
        this.testCourt.setCourtCapacity(-200000);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetZeroCapacity(){
        this.testCourt.setCourtCapacity(0);
    }

    @Test
    public void testSetCity(){
        this.testCourt.setCourtCityLocation("Madrid");
        Assert.assertEquals(this.testCourt.getCourtCityLocation(),"Madrid");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetNullCity(){
        this.testCourt.setCourtCityLocation(null);
    }

    @Test
    public void testSetCourtName(){
        this.testCourt.setCourtName("Allianz Arena");
        Assert.assertEquals(this.testCourt.getCourtName(),"Allianz Arena");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetNullCourtName(){
        this.testCourt.setCourtName(null);
    }


}

