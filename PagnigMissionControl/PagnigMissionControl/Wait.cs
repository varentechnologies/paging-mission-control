using System;

namespace PagnigMissionControl
{
    /// <summary>Methods to direct the operation of the program.</summary>
    public static class Wait
    {
        /// <summary>Makes the program pause and wait for input from the user.</summary>
        /// <remarks>This is especially useful, e.g., on the Microsoft Windows, on which console applications tend to disappear from the screen as soon as they are finished being executed (especially if the user launches the application by clicking on the <c>.exe</c> file.
        /// <para />
        /// Instead of calling <see cref="M:System.Console.ReadKey" /> directly, we abstract the pause operation away behind this method.
        /// <para />
        /// This allows us to vary the manner in which the application pauses -- or make it not pause at all -- without breaking calling code.</remarks>
        public static void ForUserToPressAnyKey()
            => Console.ReadKey();
    }
}