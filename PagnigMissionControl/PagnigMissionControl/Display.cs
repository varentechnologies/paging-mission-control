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
        /// <param name="format">A composite format string.</param>
        /// <param name="arg">An array of objects to write using <paramref name="format" />.</param>
        /// <remarks>
        /// Using a method to do this instead of directly calling, e.g.,
        /// <see
        ///     cref="M:System.Console.WriteLine" />
        /// method allows us to vary how the text is outputted without breaking calling code.
        /// </remarks>
        public static void Text(string format, params object[] arg)
            => Console.WriteLine(format, arg);
    }
}