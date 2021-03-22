namespace PagingMissionControl.Constants
{
    /// <summary>Codes that describe the severity of a faulty value.</summary>
    public static class Severity
    {
        /// <summary>Value exceeds the maximum error tolerance.</summary>
        public const int RedHigh = 0;

        /// <summary>Value is lower than the minimum tolerance.</summary>
        public const int RedLow = 3;

        /// <summary>Value is between the minimum and maximum error tolerances.</summary>
        public const int YellowHigh = 1;

        /// <summary>Value is between the maximum and minimum error tolerances on the low side of the range.</summary>
        public const int YellowLow = 2;
    }
}