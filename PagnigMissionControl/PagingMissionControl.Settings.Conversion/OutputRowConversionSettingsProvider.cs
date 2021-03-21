using Newtonsoft.Json;

namespace PagingMissionControl.Settings.Conversion
{
    /// <summary>
    /// Provides settings for conversion (i.e., serialization) of objects to/from JSON.
    /// </summary>
    internal static class OutputRowConversionSettingsProvider
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