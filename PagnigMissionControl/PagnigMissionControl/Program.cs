using PagingMissionControl.Converters;
using PagingMissionControl.Extensions;
using PagingMissionControl.Parsers;
using PagingMissionControl.Transforms;
using PagnigMissionControl.Factories;
using System;
using System.IO;
using System.Linq;

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
            var inputFilePath = UserPrompts.GetInputFilePath();

            // ReSharper disable once AssignNullToNotNullAttribute
            var lines = File.ReadAllLines(inputFilePath);
            
            UserPrompts.DisplayOutputHeader();

            Console.WriteLine(
                ConvertOutputDataSet.ToJson(
                    TransformInputDataSet.ToOutputRows(
                        ParseInput.FromPipeDelimitedInputLines(lines)
                    )
                )
            );

            Console.ReadKey();  // to keep the console window from disappearing on Windows
        }
    }
}