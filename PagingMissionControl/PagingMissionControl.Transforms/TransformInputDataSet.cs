using PagingMissionControl.Extensions;
using PagingMissionControl.Interfaces;
using System.Collections.Generic;
using System.Linq;

namespace PagingMissionControl.Transforms
{
    /// <summary>This whole program is a classic extract-transform-load application. This class provides methods that transform data from an input data set to an output data set.</summary>
    public static class TransformInputDataSet
    {
        /// <summary>
        /// Transforms a collection of input rows, expressed as an enumerable set of references to instances of objects that implements the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.IInputRow" />
        /// interface to instances of objects that implement the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.IOutputRow" />
        /// interface and returns a collection of the corresponding objects.
        /// </summary>
        /// <param name="inputRows">
        /// (Required.) Collection of references to instances of objects that implement the
        /// <see
        ///     cref="T:PagingMissionControl.Interfaces.IInputRow" />
        /// interface.
        /// </param>
        /// <returns>Collection of references to instances of objects that implement the <see cref="T:PagingMissionControl.Interfaces.IOutputRow" /> interface, whose properties are initialized with the new data values as transformed per the requirements.</returns>
        public static IEnumerable<IOutputRow> ToOutputRows(
            IEnumerable<IInputRow> inputRows)
            => inputRows.Select(r => r.ToOutputRow());
    }
}