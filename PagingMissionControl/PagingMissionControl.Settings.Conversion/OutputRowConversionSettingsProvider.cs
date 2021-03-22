using Newtonsoft.Json;

namespace PagingMissionControl.Settings.Conversion
{
    /// <summary>
    /// Provides settings for conversion (i.e., serialization) of objects to/from JSON.
    /// </summary>
    public static class OutputRowConversionSettingsProvider
    {
        /// <summary>
        /// Gets the <see cref="T:Newtonsoft.Json.JsonSerializerSettings"/> for serialization of objects to/from JSON.
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