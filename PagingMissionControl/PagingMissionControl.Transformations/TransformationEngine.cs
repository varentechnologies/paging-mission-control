using PagingMissionControl.Constants;
using PagingMissionControl.Converters;
using PagingMissionControl.Parsers;
using PagingMissionControl.Transforms;
using System.IO;
using System.Linq;

namespace PagingMissionControl.Transformations
{
    /// <summary>Engine that performs the data transformation.</summary>
    public class TransformationEngine
    {
        /// <summary>Empty, static constructor to prohibit direct allocation of this class.</summary>
        static TransformationEngine() { }

        /// <summary>Empty, protected constructor to prohibit direct allocation of this class.</summary>
        protected TransformationEngine() { }

        /// <summary>Gets a reference to the one and only instance of <see cref="T:PagingMissionControl.Transformations.TransformationEngine" />.</summary>
        public static TransformationEngine Instance { get; } =
            new TransformationEngine();

        /// <summary>
        /// Loads the data from the file with path specified in the
        /// <paramref
        ///     name="inputFilePath" />
        /// parameter, transforms the data, and then signals an <c>EVENT_TRANSFORMATION_DONE</c> event handler with the result.
        /// </summary>
        /// <param name="inputFilePath">(Required.) String containing the fully-qualified pathname of the input file. The path may contain quotes (such as is sometimes the case on the Windows operating system.)</param>
        public void DoConversion(string inputFilePath)
        {
            var lines = File.ReadAllLines(inputFilePath);
            if (!lines.Any()) return;

            Display.OutputHeader();

            var result = ConvertOutputDataSet.ToJson(
                TransformInputDataSet.ToOutputRows(
                                         ParseInput.FromPipeDelimitedInputLines(
                                             lines
                                         )
                                     )
                                     .Where(row => row.Severity.Contains("RED"))
            );

            EventQueue.EventQueue.Instance.PublishEvent(
                Events.EVENT_TRANSFORMATION_DONE, result
            );
        }
    }
}