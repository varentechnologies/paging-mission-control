using PagingMissionControl.Converters;
using PagingMissionControl.Parsers;
using PagingMissionControl.Transforms;
using System;
using System.IO;

namespace PagnigMissionControl
{
    /// <summary>
    /// Provides the program's global functionality.
    /// </summary>
    public static class Program
    {
        /// <summary>
        /// Application entry point.
        /// </summary>
        public static void Main()
        {
            var inputFilePath = AskUserFor.InputFilePath();

            var lines = File.ReadAllLines(inputFilePath);

            Display.OutputHeader();

            Display.Text(
                ConvertOutputDataSet.ToJson(
                    TransformInputDataSet.ToOutputRows(
                        ParseInput.FromPipeDelimitedInputLines(lines)
                    )
                )
            );

            Operations
                .Pause(); // to keep the console window from disappearing on Windows
        }
    }
}