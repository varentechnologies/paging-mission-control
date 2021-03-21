using PagingMissionControl.Factories;
using PagingMissionControl.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;

namespace PagingMissionControl.Parsers
{
    /// <summary>Provides helper methods that parse input from a file.</summary>
    public static class ParseInput
    {
        /// <summary>
        /// Given a collection of strings, each element of which represents a single line in an ASCII-text input file, which is assumed to be in pipe-delimited format, transforms each line into an instance of an object that implements the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.IInputRow" />
        /// interface whose properties are initialized with the values from the text fields.
        /// <para />
        /// A collection of all the corresponding references to such objects is then returned to the caller.
        /// </summary>
        /// <param name="lines">(Required.) Collection of strings, each of which corresponds to a line read in from an input file.</param>
        /// <returns>Collection of references to instances of objects that implement the <see cref="T:PagingMissionControl.Interfaces.IInputRow" /> interface, each of which is initialized with the corresponding data values from the text fields.</returns>
        public static IEnumerable<IInputRow> FromPipeDelimitedInputLines(
            IEnumerable<string> lines)
            => lines.Select(
                line => MakeNewInputRow.FromParts(
                    line.Split(new[] {'|'}, StringSplitOptions.RemoveEmptyEntries)
                )
            );
    }
}