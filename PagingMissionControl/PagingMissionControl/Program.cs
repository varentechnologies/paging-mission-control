using PagingMissionControl.Converters;
using PagingMissionControl.Parsers;
using PagingMissionControl.Transforms;
using System.IO;
using System.Linq;

namespace PagingMissionControl
{
    /// <summary>Provides the program's global functionality.</summary>
    public static class Program
    {
        /// <summary>Application entry point.</summary>
        public static void Main()
        {
            var inputFilePath = AskUserFor.InputFilePath();

            var lines = File.ReadAllLines(inputFilePath);

            Display.OutputHeader();

            Display.Text(
                ConvertOutputDataSet.ToJson(
                    TransformInputDataSet.ToOutputRows(
                                             ParseInput
                                                 .FromPipeDelimitedInputLines(
                                                     lines
                                                 )
                                         )
                                         .Where(
                                             row => row.Severity.Contains("RED")
                                         )
                )
            );

            Wait.ForUserToPressAnyKey(); // to keep the console window from disappearing on Windows
        }
    }
}