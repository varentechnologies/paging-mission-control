using PagingMissionControl.Constants;
using PagingMissionControl.Interfaces;

namespace PagingMissionControl.Params
{
    /// <summary>Parameters that indicate the severity of a reading.</summary>
    public class SeverityParams : ISeverityParams
    {
        /// <summary>Array of strings that a severity level gets displayed as in the output.</summary>
        private static readonly string[] ServityStrings =
        {
            "RED HIGH", "YELLOW HIGH", "YELLOW LOW", "RED LOW"
        };

        /// <summary>Gets or sets the actual value of the observable physical quantity that is currently being reported by the component.</summary>
        public decimal RawValue { get; set; }

        /// <summary>Maximum high-end fault tolerance value for the physical quantity being observed.</summary>
        public decimal RedHighLimit { get; set; }

        /// <summary>Gets or sets the minimum low-end fault tolerance value for the physical quantity being observed.</summary>
        public decimal RedLowLimit { get; set; }

        /// <summary>Minimum high-end fault tolerance value for the physical quantity being observed.</summary>
        public decimal YellowHighLimit { get; set; }

        /// <summary>Maximum low-end fault tolerance value for the physical quantity being observed.</summary>
        public decimal YellowLowLimit { get; set; }

        /// <summary>Gets a string of text that indicates the severity of the fault in a standardized manner.</summary>
        /// <returns>String containing the values 'RED HIGH', 'YELLOW HIGH', 'YELLOW LOW', or 'RED LOW', or 'NORM' for normal (i.e., value is between YELLOW_HIGH and YELLOW_LOW.</returns>
        public string GetSeverity()
        {
            if (RawValue - RedHighLimit >= 0)
                return ServityStrings[Severity.RedHigh];
            if (RawValue - YellowHighLimit >= 0)
                return ServityStrings[Severity.YellowHigh];
            if (RedLowLimit - RawValue >= 0)
                return ServityStrings[Severity.RedLow];
            return YellowLowLimit - RawValue >= 0
                ? ServityStrings[Severity.YellowLow]
                : "NORM";
        }
    }
}