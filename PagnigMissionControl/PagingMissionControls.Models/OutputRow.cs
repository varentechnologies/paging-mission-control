using Newtonsoft.Json;
using PagingMissionControl.Interfaces;

namespace PagingMissionControls.Models
{
    /// <summary>Defines the values to be outputted into JSON format.</summary>
    public class OutputRow : IOutputRow
    {
        /// <summary>Gets or sets a string describing the component that is reporting the condition.</summary>
        [JsonProperty(PropertyName = "component", Order = 3)]
        public string Component { get; set; }

        /// <summary>Gets or sets an integer that specifies an identifier for the particular satellite to which the current record pertains.</summary>
        [JsonProperty(PropertyName = "satelliteId", Order = 1)]
        public int SatelliteId { get; set; }

        /// <summary>Gets or sets a string consisting of a description of the severity of a fault.</summary>
        [JsonProperty(PropertyName = "severity", Order = 2)]
        public string Severity { get; set; }

        /// <summary>Gets or sets a string containing the timestamp of the event.</summary>
        [JsonProperty(PropertyName = "timestamp", Order = 4)]
        public string Timestamp { get; set; }
    }
}