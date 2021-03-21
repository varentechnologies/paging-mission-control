namespace PagingMissionControl.Interfaces
{
    /// <summary>Defines the methods and properties of an object that encapsulates the data elements in each input record.</summary>
    public interface IInputRow
    {
        /// <summary>Gets or sets a string that labels which component -- currently TSTAT for Thermostat or BATT for Battery -- that this data refers to.</summary>
        string Component { get; set; }

        /// <summary>Gets or sets the actual value of the observable quantity.</summary>
        decimal RawValue { get; set; }

        /// <summary>Gets or sets the value of the physical observable, above which or at, puts the satellite in the high red-alert zone.</summary>
        /// <remarks>This value is different for each particular record.</remarks>
        /// f
        decimal RedHighLimit { get; set; }

        /// <summary>Gets or sets the value of the physical observable, lower than which or at, puts the satellite in the low part of the red-alert zone.</summary>
        /// <remarks>This value is different for each particular record.
        /// <para />
        /// For example, if the voltage of the battery is at or below this value, then the voltage is in the yellow-alert zone.
        /// <para />
        /// Still lower, and the voltage now falls into the severely-low, or red-alert low, zone.</remarks>
        decimal RedLowLimit { get; set; }

        /// <summary>Gets or sets an integer that specifies an identifier for the particular satellite to which the current record pertains.</summary>
        int SatelliteId { get; set; }

        /// <summary>Gets or sets a string containing the timestamp of the current entry.</summary>
        string Timestamp { get; set; }

        /// <summary>Gets or sets the value of the physical observable, above which or at, puts the satellite in the high yellow-alert zone.</summary>
        /// <remarks>This value is different for each particular record.</remarks>
        decimal YellowHighLimit { get; set; }

        /// <summary>Gets or sets the value of the physical observable, lower than which or at, puts the satellite in the low part of the yellow-alert zone.</summary>
        /// <remarks>This value is different for each particular record.
        /// <para />
        /// For example, if the voltage of the battery is at or below this value, then the voltage is in the yellow-alert zone.
        /// <para />
        /// Still lower, and the voltage now falls into the severely-low, or red-alert low, zone.</remarks>
        decimal YellowLowLimit { get; set; }
    }
}