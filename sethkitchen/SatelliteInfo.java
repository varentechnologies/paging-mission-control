package sethkitchen;

////////////////////////////////////////////////////////////// Class Def
//
//                          SatelliteInfo
// An attempt to further abstract the telemetry data into a
// model with only data the an actual real life satellite could
// have. The satellite could have an id stamped on it, it is
// the thing that actually has limits and a raw value.
//
//////////////////////////////////////////////////////////////
public class SatelliteInfo {
    /////////////////////////////// Member vars
    private float mRawValue;
    private int mRedHighLimit;
    private int mRedLowLimit;
    private String mSatelliteId;
    private int mYellowHighLimit;
    private int mYellowLowLimit;


    ///////////////////////////// Functions
    // Used for Hashmap
    public String getId() {
        return mSatelliteId;
    }

    // Check if raw value is higher than the red high limit
    public boolean isHighOk() {
        if(mRawValue > mRedHighLimit) {
            return false;
        }
        return true;
    }

    //Check if the raw value is lower than the red low limit
    public boolean isLowOk() {
        if(mRawValue < mRedLowLimit) {
            return false;
        }
        return true;
    }

    // Constructor
    public SatelliteInfo(String satelliteId, int redHighLimit, int yellowHighLimit, int yellowLowLimit, int redLowLimit, float rawValue) {
        mSatelliteId = satelliteId;
        mRedHighLimit = redHighLimit;
        mYellowHighLimit = yellowHighLimit;
        mYellowLowLimit = yellowLowLimit;
        mRedLowLimit = redLowLimit;
        mRawValue = rawValue;
    }
}
