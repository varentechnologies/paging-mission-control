using PagingMissionControl.Interfaces;

namespace PagnigMissionControl.Factories
{
    public static class MakeNewSeverityParams
    {
        public static ISeverityParams FromData(
            decimal redHighLimit,
            decimal yellowHighLimit, decimal yellowLowLimit,
            decimal redLowLimit, decimal rawValue
            )
        {
            return new SeverityParams
            {
                RedHighLimit = redHighLimit,
                YellowHighLimit = yellowHighLimit,
                YellowLowLimit = yellowLowLimit,
                RedLowLimit = redLowLimit,
                RawValue = rawValue
            };
        }
    }
}