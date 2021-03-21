using Newtonsoft.Json;
using PagingMissionControl.Interfaces;
using PagingMissionControl.Settings.Conversion;
using System.Collections.Generic;

namespace PagingMissionControl.Converters
{
    public static class ConvertOutputDataSet
    {
        public static string ToJson(IEnumerable<IOutputRow> row)
            => JsonConvert.SerializeObject(
                row, OutputRowConversionSettingsProvider.Settings
            );
    }
}