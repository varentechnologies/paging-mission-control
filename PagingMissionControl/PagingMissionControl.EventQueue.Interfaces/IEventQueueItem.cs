using System;
using System.Collections.Generic;

namespace PagingMissionControl.EventQueue.Interfaces
{
    /// <summary>Defines the publicly-exposed methods and properties of an item in an event queue.</summary>
    public interface IEventQueueItem : IDisposable
    {
        /// <summary>Gets the unique identifier used to tag this event queue item.</summary>
        /// <remarks>The value of this property may never be set to the Zero GUID.</remarks>
        Guid EventId { get; }

        /// <summary>Collection of instances of <see cref="T:System.Delegate" /> that specify code to be invoked when this event queue item is published to the event queue.</summary>
        IList<Delegate> HandlerList { get; }

        /// <summary>Adds an event handler delegate to the list maintained for this event queue item.</summary>
        /// <param name="handler">
        /// (Required.) Reference to an instance of a
        /// <see
        ///     cref="T:System.Delegate" />
        /// that refers to the code that is to be invoked when the event is published.
        /// </param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="handler" />, is passed a <see langword="null" /> value.</exception>
        IEventQueueItem AndAddHandler(Delegate handler);

        /// <summary>Adds an event handler delegate to the list maintained for this event queue item.</summary>
        /// <param name="handler">
        /// (Required.) Reference to an instance of a
        /// <see
        ///     cref="T:System.Delegate" />
        /// that refers to the code that is to be invoked when the event is published.
        /// </param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="handler" />, is passed a <see langword="null" /> value.</exception>
        IEventQueueItem AndAddHandler(EventHandler handler);

        /// <summary>Adds an event handler delegate to the list maintained for this event queue item.</summary>
        /// <typeparam name="T">Name of the object that contains the event data.</typeparam>
        /// <param name="handler">
        /// (Required.) Reference to an instance of a
        /// <see
        ///     cref="T:System.Delegate" />
        /// that refers to the code that is to be invoked when the event is published.
        /// </param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="handler" />, is passed a <see langword="null" /> value.</exception>
        IEventQueueItem AndAddHandler<T>(EventHandler<T> handler)
            where T : EventArgs;

        /// <summary>Associates a removal action with this event queue item.</summary>
        /// <param name="removalAction">
        /// (Required.) Reference to an instance of
        /// <see
        ///     cref="T:System.Action" />
        /// that specifies the code to be executed in order to remove this item from the event queue upon its release from memory by the garbage collector.
        /// </param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="removalAction" />, is passed a <see langword="null" /> value.</exception>
        IEventQueueItem AndRemovalAction(Action<IEventQueueItem> removalAction);

        /// <summary>Associates this event queue item with a unique identifier.</summary>
        /// <param name="eventId">(Required.) Reference to a <see cref="T:System.Guid" /> that serves to uniquely tag this event queue item with associated handler(s).</param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        IEventQueueItem ForEventId(Guid eventId);

        /// <summary>Removes all the handlers registered for this event.</summary>
        void Clear();
    }
}