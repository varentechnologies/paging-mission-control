using Newtonsoft.Json;
using PagingMissionControl.Interfaces;

namespace PagingMissionControls.Models
{
    public class OutputRow : IOutputRow
    {
        [JsonProperty(PropertyName = "component", Order = 3)]
        public string Component { get; set; }

        [JsonProperty(PropertyName = "satelliteId", Order = 1)]
        public int SatelliteId { get; set; }

        [JsonProperty(PropertyName = "severity", Order = 2)]
        public string Severity { get; set; }

        [JsonProperty(PropertyName = "timestamp", Order = 4)]
        public string Timestamp { get; set; }
    }
}