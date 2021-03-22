using PagingMissionControl.EventQueue.Interfaces;
using PagingMissionControl.EventQueue.Items;

namespace PagingMissionControl.EventQueue.Factories
{
    /// <summary>
    /// Creates new instances of objects that implement the
    /// <see
    ///     cref="T:PagingMissionControl.EventQueue.Interfaces.IEventQueueItem" />
    /// interface.
    /// </summary>
    public static class MakeNewEventQueueItem
    {
        /// <summary>
        /// Creates a new instance of an object that implements the
        /// <see
        ///     cref="T:PagingMissionControl.EventQueue.Interfaces.IEventQueueItem" />
        /// interface and returns a reference to it.
        /// </summary>
        /// <returns>
        /// Reference to an instance of an object that implements the
        /// <see
        ///     cref="T:PagingMissionControl.EventQueue.Interfaces.IEventQueueItem" />
        /// interface.
        /// </returns>
        public static IEventQueueItem FromScratch()
            => new EventQueueItem();
    }
}