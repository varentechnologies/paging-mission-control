using Newtonsoft.Json;

namespace PagingMissionControl.Settings.Serialization
{
    /// <summary>
    /// Provides settings for serialization of objects to/from JSON.
    /// </summary>
    internal static class OutputRowSerializationSettingsProvider
    {
        /// <summary>
        /// Gets the settings for serialization of objects to/from JSON.
        /// </summary>
        public static readonly JsonSerializerSettings Settings =
            new JsonSerializerSettings
            {
                MetadataPropertyHandling = MetadataPropertyHandling.Ignore,
                Formatting = Formatting.Indented,                               // pretty print
                DateParseHandling = DateParseHandling.None
            };
    }
}