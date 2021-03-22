using System;

namespace PagingMissionControl.Transformations
{
    /// <summary>Methods to prompt the user for input values.</summary>
    public static class AskUserFor
    {
        /// <summary>Prompts the user to type the path of a file from which the user wishes this software to read text.</summary>
        /// <returns>String containing the value the user typed.</returns>
        /// <remarks>
        /// Using a method such as this, as opposed to calling
        /// <see
        ///     cref="M:System.Console.ReadLine" />
        /// directly, allows us to abstract the means away by which input is gathered.
        /// <para />
        /// The implementation of this method can be varied later, e.g., to read the path from a command-line parameter etc, without breaking the caller.
        /// </remarks>
        public static string InputFilePath()
        {
            Console.WriteLine("Please enter the path to the input file: ");
            var inputFilePath = Console.ReadLine()
                                       .Replace("\"", "");
            return inputFilePath;
        }
    }
}