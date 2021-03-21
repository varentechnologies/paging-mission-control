using PagingMissionControl.Interfaces;
using PagingMissionControls.Models;
using PagnigMissionControl.Factories;

namespace PagingMissionControl.Extensions
{
    public static class InputRowExtensions
    {
        public static IOutputRow ToOutputRow(this IInputRow row)
            => new OutputRow
            {
                Timestamp = row.Timestamp,
                Component = row.Component,
                Severity = row.ToSeverityString(),
                SatelliteId = row.SatelliteId
            };

        private static string ToSeverityString(this IInputRow row)
            => MakeNewSeverityParams.FromData(
                                        row.RedHighLimit, row.YellowHighLimit,
                                        row.YellowLowLimit, row.RedLowLimit,
                                        row.RawValue
                                    )
                                    .GetSeverity();
    }
}