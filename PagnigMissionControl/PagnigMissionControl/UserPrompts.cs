using System;

namespace PagnigMissionControl
{
    /// <summary>
    /// Methods to prompt the user for input values.
    /// </summary>
    public static class UserPrompts
    {
        public static string GetInputFilePath()
        {
            Console.WriteLine("Please enter the path to the input file: ");
            var inputFilePath = Console.ReadLine().Replace("\"", "");
            return inputFilePath;
        }

        public static void DisplayOutputHeader()
        {
            Console.WriteLine();
            Console.WriteLine();
            Console.WriteLine("Output data:");
        }
    }
}