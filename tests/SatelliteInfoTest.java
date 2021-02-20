package tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import sethkitchen.SatelliteInfo;

public class SatelliteInfoTest {

    @Test
    public void idShouldMatchConstructor() {
        SatelliteInfo si=new SatelliteInfo("0",0,0,0,0,0);
        assertEquals("0",si.getId());
    }

    @Test
    public void fiveRawIsLowerThanNineLimitIsLowOkFalse() {
        SatelliteInfo si=new SatelliteInfo("0",0,0,0,9,5);
        assertEquals(false,si.isLowOk());
    }

    @Test
    public void fifteenRawIsHigherThanNineLimitIsLowOkTrue() {
        SatelliteInfo si=new SatelliteInfo("0",0,0,0,9,15);
        assertEquals(true,si.isLowOk());
    }

    @Test
    public void fiveRawIsLowerThanNineLimitIsHighOkTrue() {
        SatelliteInfo si=new SatelliteInfo("0",9,0,0,0,5);
        assertEquals(true,si.isHighOk());
    }

    @Test
    public void fifteenRawIsHigherThanNineLimitIsHighOkFalse() {
        SatelliteInfo si=new SatelliteInfo("0",9,0,0,0,15);
        assertEquals(false,si.isHighOk());
    }
}
