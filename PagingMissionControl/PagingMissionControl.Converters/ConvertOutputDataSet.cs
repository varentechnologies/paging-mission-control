using Newtonsoft.Json;
using PagingMissionControl.Interfaces;
using PagingMissionControl.Settings.Conversion;
using System.Collections.Generic;

namespace PagingMissionControl.Converters
{
    /// <summary>
    /// Converts a collection of objects that implement the
    /// <see
    ///     cref="T:PagingMissionControl.Interfaces.IOutputRow" />
    /// interface into a JSON-formatted string representation.
    /// </summary>
    public static class ConvertOutputDataSet
    {
        /// <summary>
        /// Converts a collection of objects, a reference to which is specified by the <paramref name="rows" /> parameter, that implement the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.IOutputRow" />
        /// interface into a JSON-formatted string representation.
        /// </summary>
        public static string ToJson(IEnumerable<IOutputRow> rows)
            => JsonConvert.SerializeObject(
                rows, OutputRowConversionSettingsProvider.Settings
            );
    }
}