using System;

namespace PagingMissionControl.Constants
{
    /// <summary>
    /// Unique identifiers for events.
    /// </summary>
    public static class Events
    {
        /// <summary>Unqiue identifier of an event that signifies that the data transformation operation is completed.</summary>
        public static readonly Guid EVENT_TRANSFORMATION_DONE = Guid.NewGuid();
    }
}