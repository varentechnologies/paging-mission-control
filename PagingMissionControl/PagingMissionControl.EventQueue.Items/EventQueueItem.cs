using PagingMissionControl.EventQueue.Interfaces;
using System;
using System.Collections.Generic;

namespace PagingMissionControl.EventQueue.Items
{
    public class EventQueueItem : IEventQueueItem
    {
        /// <summary>Instance of <see cref="T:System.Object" /> to be used for thread synchronization.</summary>
        private static readonly object SyncRoot = new object();

        /// <summary>A <see cref="T:System.Action" /> that specifies what to do in order to remove this item from the event queue.</summary>
        private Action<IEventQueueItem> _removalAction;

        /// <summary>Gets the unique identifier used to tag this event queue item.</summary>
        public Guid EventId { get; private set; }

        /// <summary>Collection of instances of <see cref="T:System.Delegate" /> that specify code to be invoked when this event queue item is published to the event queue.</summary>
        public IList<Delegate> HandlerList { get; } = new List<Delegate>();

        /// <summary>Adds an event handler delegate to the list maintained for this event queue item.</summary>
        /// <param name="handler">
        /// (Required.) Reference to an instance of a
        /// <see
        ///     cref="T:System.Delegate" />
        /// that refers to the code that is to be invoked when the event is published.
        /// </param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="handler" />, is passed a <see langword="null" /> value.</exception>
        public IEventQueueItem AndAddHandler(Delegate handler)
        {
            InternalAddHandler(handler);

            return this;
        }

        /// <summary>Adds an event handler delegate to the list maintained for this event queue item.</summary>
        /// <param name="handler">
        /// (Required.) Reference to an instance of a
        /// <see
        ///     cref="T:System.Delegate" />
        /// that refers to the code that is to be invoked when the event is published.
        /// </param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="handler" />, is passed a <see langword="null" /> value.</exception>
        public IEventQueueItem AndAddHandler(EventHandler handler)
        {
            InternalAddHandler(handler);

            return this;
        }

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
        public IEventQueueItem AndAddHandler<T>(EventHandler<T> handler)
            where T : EventArgs
        {
            InternalAddHandler(handler);

            return this;
        }

        /// <summary>Associates a removal action with this event queue item.</summary>
        /// <param name="removalAction">
        /// (Required.) Reference to an instance of
        /// <see
        ///     cref="T:System.Action" />
        /// that specifies the code to be executed in order to remove this item from the event queue upon its release from memory by the garbage collector.
        /// </param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        /// <exception cref="T:System.ArgumentNullException">Thrown if the required parameter, <paramref name="removalAction" />, is passed a <see langword="null" /> value.</exception>
        public IEventQueueItem AndRemovalAction(Action<IEventQueueItem> removalAction)
        {
            _removalAction = removalAction ??
                             throw new ArgumentNullException(
                                 nameof(removalAction)
                             );

            return this;
        }

        /// <summary>Performs application-defined tasks associated with freeing, releasing, or resetting unmanaged resources.</summary>
        public void Dispose()
            => _removalAction?.Invoke(this);

        /// <summary>Associates this event queue item with a unique identifier.</summary>
        /// <param name="eventId">(Required.) Reference to a <see cref="T:System.Guid" /> that serves to uniquely tag this event queue item with associated handler(s).</param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        /// <exception cref="T:System.ArgumentException">
        /// Thrown if the Zero GUID is specified for the
        /// <paramref
        ///     name="eventId" />
        /// parameter. This value is not allowed.
        /// </exception>
        public IEventQueueItem ForEventId(Guid eventId)
        {
            if (Guid.Empty == eventId)
                throw new ArgumentException(
                    "You may not pass the Zero GUID for the eventId parameter."
                );

            EventId = eventId;

            return this;
        }

        /// <summary>Removes all the handlers registered for this event.</summary>
        public void Clear()
            => HandlerList.Clear();

        /// <summary>Adds an event handler delegate to the list maintained for this event queue item.</summary>
        /// <param name="handler"> (Required.) Reference to an instance of a <see cref="T:System.Delegate" /> that refers to the code that is to be invoked when the event is published. </param>
        /// <returns>Reference to the same instance of the object that called this method, for fluent use.</returns>
        /// <exception
        ///     cref="T:System.ArgumentNullException">
        /// Thrown if the required parameter, <paramref name="handler" />, is passed a
        private void InternalAddHandler(Delegate handler)
        {
            if (handler == null)
                throw new ArgumentNullException(nameof(handler));

            if (HandlerList.Contains(handler))
                return;

            HandlerList.Add(handler);
        }
    }
}