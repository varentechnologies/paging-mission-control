namespace PagingMissionControl.Interfaces
{
    /// <summary>Defines the publicly-exposed methods and properties of a single row of output data.</summary>
    public interface IOutputRow
    {
        /// <summary>Gets or sets a string describing the component that is reporting the condition.</summary>
        string Component { get; set; }

        /// <summary>Gets or sets an integer that specifies an identifier for the particular satellite to which the current record pertains.</summary>
        int SatelliteId { get; set; }

        /// <summary>Gets or sets a string consisting of a description of the severity of a fault.</summary>
        string Severity { get; set; }

        /// <summary>Gets or sets a string containing the timestamp of the event.</summary>
        string Timestamp { get; set; }
    }
}