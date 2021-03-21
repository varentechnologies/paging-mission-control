using System;

namespace PagnigMissionControl
{
    /// <summary>Methods to display information on the screen.</summary>
    public class Display
    {
        /// <summary>Displays a user-friendly header denoting the start of the output data.</summary>
        public static void OutputHeader()
        {
            Console.WriteLine();
            Console.WriteLine();
            Console.WriteLine("Output data:");
        }

        /// <summary>Displays formatted text.</summary>
        /// <param name="value">The value to write.</param>
        /// <remarks>
        /// Using a method to do this instead of directly calling, e.g.,
        /// <see
        ///     cref="M:System.Console.WriteLine" />
        /// method allows us to vary how the text is outputted without breaking calling code.
        /// </remarks>
        public static void Text(string value)
            => Console.WriteLine(value);
    }
}