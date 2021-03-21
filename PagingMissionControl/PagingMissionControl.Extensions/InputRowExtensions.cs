using PagingMissionControl.Factories;
using PagingMissionControl.Interfaces;
using PagingMissionControls.Models;

namespace PagingMissionControl.Extensions
{
    /// <summary>Helper methods to adjunct on to an instance of an object that implements the <see cref="T:PagingMissionControl.Interfaces.IInputRow" /> interface.</summary>
    public static class InputRowExtensions
    {
        /// <summary>
        /// Transforms an instance of an object that implements the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.IInputRow" />
        /// interface to an instance of an object that implements the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.IOutputRow" />
        /// interface per the requirements.
        /// </summary>
        /// <param name="row">(Required.) Reference to an instance of an object that implements the <see cref="T:PagingMissionControl.Interfaces.IInputRow" /> interface.</param>
        /// <returns></returns>
        public static IOutputRow ToOutputRow(this IInputRow row)
            => new OutputRow
            {
                Timestamp = row.Timestamp,
                Component = row.Component,
                Severity = row.ToSeverityString(),
                SatelliteId = row.SatelliteId
            };

        /// <summary>Determines the standardized severity string that corresponds to the nature of the raw reading versus the provided limits.</summary>
        /// <param name="row">(Required.) Reference to an instance of an object that implements the <see cref="T:PagingMissionControl.Interfaces.IInputRow" /> interface.</param>
        /// <returns>String containing the standardized severity level.</returns>
        private static string ToSeverityString(this IInputRow row)
            => MakeNewSeverityParams.FromData(
                                        row.RedHighLimit, row.YellowHighLimit,
                                        row.YellowLowLimit, row.RedLowLimit,
                                        row.RawValue
                                    )
                                    .GetSeverity();
    }
}