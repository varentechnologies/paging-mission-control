using PagingMissionControl.Formatters;
using PagingMissionControl.Interfaces;
using PagingMissionControls.Models;
using System;
using System.Collections.Generic;
using System.Linq;

namespace PagingMissionControl.Factories
{
    /// <summary>Creates a new input-data-row POCO from the tokenized version of the input line provided.</summary>
    public static class MakeNewInputRow
    {
        /// <summary>
        /// Given a <paramref name="partEnumerable" /> containing the tokenized components of a particular pipe-delimited line from the input file, initializes a new instance of an object that implements the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.IInputRow" />
        /// interface with it and returns a reference to the object.
        /// </summary>
        /// <param name="partEnumerable">(Required.) Collection of strings that contains the tokenized version of the current input file line.</param>
        /// <returns>
        /// Reference to an instance of an object that implements the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.IInputRow" />
        /// interface whose properties are initialized with the data values from the input line.
        /// </returns>
        public static IInputRow FromParts(IEnumerable<string> partEnumerable)
        {
            var parts = partEnumerable.ToArray();

            return new InputRow
            {
                Timestamp = FormatTimeString.FromTimestamp(parts[0]),
                SatelliteId = Convert.ToInt32(parts[1]),
                RedHighLimit = Convert.ToDecimal(parts[2]),
                YellowHighLimit = Convert.ToDecimal(parts[3]),
                YellowLowLimit = Convert.ToDecimal(parts[4]),
                RedLowLimit = Convert.ToDecimal(parts[5]),
                RawValue = Convert.ToDecimal(parts[6]),
                Component = parts[7]
            };
        }
    }
}