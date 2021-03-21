using System;

namespace PagnigMissionControl.Formatters
{
    /// <summary>Formats timestamp strings according to spec.</summary>
    public static class FormatTimeString
    {
        /// <summary>Given the <paramref name="timestamp" /> string provided (ostensibly from the satellite raw data), reformats it per JSON spec and then returns the result.</summary>
        /// <param name="timestamp">(Required.) String containing the timestamp from the input data.</param>
        /// <returns>String containing the timestamp formatted per spec.</returns>
        public static string FromTimestamp(string timestamp)
            => DateTime.Parse(
                           timestamp.Insert(4, "-")
                                    .Insert(7, "-")
                                    .Replace(" ", "T")
                       )
                       .ToString("yyyy-MM-ddTHH:mm:ss.fff");
    }
}