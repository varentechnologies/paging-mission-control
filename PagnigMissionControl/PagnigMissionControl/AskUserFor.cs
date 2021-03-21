using System;

namespace PagnigMissionControl
{
    /// <summary>
    /// Methods to prompt the user for input values.
    /// </summary>
    public static class AskUserFor
    {
        public static string InputFilePath()
        {
            Console.WriteLine("Please enter the path to the input file: ");
            var inputFilePath = Console.ReadLine().Replace("\"", "");
            return inputFilePath;
        }
    }
}