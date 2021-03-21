namespace PagingMissionControl.Interfaces
{
    /// <summary>Defines the publicly-exposed methods and properties of parameters for the severity limits and indicator strings.</summary>
    public interface ISeverityParams
    {
        /// <summary>Gets or sets the actual value of the observable physical quantity that is currently being reported by the component.</summary>
        decimal RawValue { get; set; }

        /// <summary>Maximum high-end fault tolerance value for the physical quantity being observed.</summary>
        decimal RedHighLimit { get; set; }

        /// <summary>Gets or sets the minimum low-end fault tolerance value for the physical quantity being observed.</summary>
        decimal RedLowLimit { get; set; }

        /// <summary>Minimum high-end fault tolerance value for the physical quantity being observed.</summary>
        decimal YellowHighLimit { get; set; }

        /// <summary>Maximum low-end fault tolerance value for the physical quantity being observed.</summary>
        decimal YellowLowLimit { get; set; }

        /// <summary>Gets a string of text that indicates the severity of the fault in a standardized manner.</summary>
        /// <returns>String containing the values RED_HIGH, YELLOW_HIGH, YELLOW_LOW, or RED_LOW, or NORM for normal (i.e., value is between YELLOW_HIGH and YELLOW_LOW.</returns>
        string GetSeverity();
    }
}